package com.strawhatacademy.ui;

import com.strawhatacademy.dao.GradeDAO;
import com.strawhatacademy.dao.SubjectDAO;
import com.strawhatacademy.model.Grade;
import com.strawhatacademy.model.Subject;
import com.strawhatacademy.model.User;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GradeEditorForm extends JDialog {

    private final GradeDAO gradeDAO = new GradeDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final User teacher; 
    private Grade gradeToEdit; 
    private GradesViewForm parentForm; 

    // UI Components
    private JTextField txtGradeValue, txtDateRecorded;
    private JComboBox<String> cmbSubject, cmbType;
    private JTextField txtStudentId; 
    private JButton btnSave;

    /**
     * Constructor for the Grade Editor Form.
     * @param parent
     * @param grade
     * @param teacher
     */
    public GradeEditorForm(GradesViewForm parent, Grade grade, User teacher) {
        super(parent, grade == null ? "Add New Grade" : "Edit Grade", true); 
        this.parentForm = parent;
        this.gradeToEdit = grade;
        this.teacher = teacher;
        
        initComponents();
        populateFields();
        
        this.setSize(400, 450);
        this.setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        // ... (Layout initialization is the same as before) ...
        setLayout(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Student ID
        txtStudentId = new JTextField(10);
        txtStudentId.setEnabled(gradeToEdit == null); 
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(txtStudentId);
        
        // 2. Subject
        cmbSubject = new JComboBox<>();
        populateSubjectComboBox();
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(cmbSubject);
        
        // 3. Grade Type
        cmbType = new JComboBox<>(new String[]{"Quiz", "Midterm", "Final", "Project"});
        inputPanel.add(new JLabel("Assessment Type:"));
        inputPanel.add(cmbType);

        // 4. Grade Value
        txtGradeValue = new JTextField(10);
        inputPanel.add(new JLabel("Grade Value:"));
        inputPanel.add(txtGradeValue);

        // 5. Date Recorded
        txtDateRecorded = new JTextField(LocalDate.now().toString()); 
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(txtDateRecorded);
        
        add(inputPanel, BorderLayout.CENTER);

        // 6. Save Button
        btnSave = new JButton(gradeToEdit == null ? "Save New Grade" : "Update Grade");
        btnSave.addActionListener(e -> saveOrUpdateGrade());
        add(btnSave, BorderLayout.SOUTH);
    }

    private void populateSubjectComboBox() {
        // Only load subjects taught by the teacher
        List<Subject> subjects = subjectDAO.getTeacherSubjects(teacher.getUserId());
        for (Subject subject : subjects) {
            cmbSubject.addItem(subject.getSubjectName() + " (ID:" + subject.getSubjectId() + ")");
        }
    }

    private void populateFields() {
        if (gradeToEdit != null) {
            // Editing mode: Pre-fill fields
            txtStudentId.setText(String.valueOf(gradeToEdit.getStudentId()));
            txtStudentId.setEnabled(false); 
            
            txtGradeValue.setText(String.valueOf(gradeToEdit.getGradeValue()));
            txtDateRecorded.setText(gradeToEdit.getDateRecorded().toString());
            
            cmbType.setSelectedItem(gradeToEdit.getType());

            // CHANGED: Access subjectNameMap via the public getter method
            String subjectName = parentForm.getSubjectNameMap().getOrDefault(gradeToEdit.getSubjectId(), "Unknown");
            cmbSubject.setSelectedItem(subjectName + " (ID:" + gradeToEdit.getSubjectId() + ")");

        }
    }

    /**
     * Handles form submission to either add a new grade or update an existing one.
     */
    private void saveOrUpdateGrade() {
        try {
            // 1. Validate and Parse Input
            double gradeValue = Double.parseDouble(txtGradeValue.getText().trim());
            int studentId = Integer.parseInt(txtStudentId.getText().trim());
            
            // Extract Subject ID from the combo box string: "Subject Name (ID:X)"
            String selectedItem = (String) cmbSubject.getSelectedItem();
            if (selectedItem == null || selectedItem.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Please select a subject.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            int start = selectedItem.indexOf("(ID:") + 4;
            int end = selectedItem.indexOf(")");
            int subjectId = Integer.parseInt(selectedItem.substring(start, end));
            
            LocalDate dateRecorded = LocalDate.parse(txtDateRecorded.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            String type = (String) cmbType.getSelectedItem();
            
            boolean success;
            
            if (gradeToEdit == null) {
                // ADD NEW GRADE
                Grade newGrade = new Grade(0, studentId, subjectId, gradeValue, dateRecorded, type);
                success = gradeDAO.addGrade(newGrade);
            } else {
                // UPDATE EXISTING GRADE
                gradeToEdit.setGradeValue(gradeValue);
                gradeToEdit.setDateRecorded(dateRecorded);
                gradeToEdit.setType(type);
                success = gradeDAO.updateGrade(gradeToEdit);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Grade saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Operation failed. Check if Student ID is valid and data is correct.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number or ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred. Check date format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}