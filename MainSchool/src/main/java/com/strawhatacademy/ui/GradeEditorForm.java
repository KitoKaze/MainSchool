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

    private JTextField txtGradeValue, txtDateRecorded, txtStudentId;
    private JComboBox<String> cmbSubject, cmbType;
    private JButton btnSave;

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
        setLayout(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtStudentId = new JTextField(10);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(txtStudentId);
        
        cmbSubject = new JComboBox<>();
        populateSubjectComboBox();
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(cmbSubject);
        
        cmbType = new JComboBox<>(new String[]{"Quiz", "Exam", "Project", "Final"});
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(cmbType);

        txtGradeValue = new JTextField(10);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(txtGradeValue);

        txtDateRecorded = new JTextField(LocalDate.now().toString()); 
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(txtDateRecorded);
        
        add(inputPanel, BorderLayout.CENTER);

        btnSave = new JButton(gradeToEdit == null ? "Save" : "Update");
        btnSave.addActionListener(e -> saveOrUpdateGrade());
        add(btnSave, BorderLayout.SOUTH);
    }

    private void populateSubjectComboBox() {
        List<Subject> subjects = subjectDAO.getTeacherSubjects(teacher.getUserId());
        for (Subject s : subjects) {
            cmbSubject.addItem(s.getSubjectName() + " (ID:" + s.getSubjectId() + ")");
        }
    }

    private void populateFields() {
        if (gradeToEdit != null) {
            txtStudentId.setText(String.valueOf(gradeToEdit.getStudentId()));
            txtStudentId.setEnabled(false);
            txtGradeValue.setText(String.valueOf(gradeToEdit.getGradeValue()));
            txtDateRecorded.setText(gradeToEdit.getDateRecorded().toString());
            cmbType.setSelectedItem(gradeToEdit.getType());
        }
    }

    private void saveOrUpdateGrade() {
        try {
            double val = Double.parseDouble(txtGradeValue.getText().trim());
            int sId = Integer.parseInt(txtStudentId.getText().trim());
            
            String selected = (String) cmbSubject.getSelectedItem();
            int subId = Integer.parseInt(selected.substring(selected.indexOf("(ID:") + 4, selected.indexOf(")")));
            
            LocalDate date = LocalDate.parse(txtDateRecorded.getText().trim());
            String type = (String) cmbType.getSelectedItem();
            
            boolean success;
            if (gradeToEdit == null) {
                // Line 103: Now finds the addGrade method
                success = gradeDAO.addGrade(new Grade(0, sId, subId, val, date, type));
            } else {
                gradeToEdit.setGradeValue(val);
                gradeToEdit.setDateRecorded(date);
                gradeToEdit.setType(type);
                success = gradeDAO.updateGrade(gradeToEdit);
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Operation Successful");
                this.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}