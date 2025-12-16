package com.strawhatacademy.ui;

import com.strawhatacademy.dao.GradeDAO;
import com.strawhatacademy.dao.SubjectDAO;
import com.strawhatacademy.model.Grade;
import com.strawhatacademy.model.Role;
import com.strawhatacademy.model.User;
import com.strawhatacademy.model.Subject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradesViewForm extends JFrame {

    private final User loggedInUser;
    private final GradeDAO gradeDAO = new GradeDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();

    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JButton btnAddGrade, btnEditGrade;
    private JLabel lblTitle;

    // CHANGED: Made private and access via getter for encapsulation
    private Map<Integer, String> subjectNameMap; 

    public GradesViewForm(User user) {
        this.loggedInUser = user;
        loadSubjectCache(); 
        
        initComponents();
        loadGradesData(); 
        
        this.setLocationRelativeTo(null);
        this.setTitle("Grades View");
    }
    
    // NEW: Public getter for the subject map, needed by GradeEditorForm
    public Map<Integer, String> getSubjectNameMap() {
        return subjectNameMap;
    }
    
    // NEW: Public method to refresh the table data
    public void refreshGradesView() {
        loadGradesData();
    }
    
    private void loadSubjectCache() {
        List<Subject> allSubjects = subjectDAO.getAllSubjects();
        subjectNameMap = allSubjects.stream()
            .collect(Collectors.toMap(Subject::getSubjectId, Subject::getSubjectName));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        lblTitle = new JLabel("Your Grades Summary", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"Grade ID", "Subject", "Assessment Type", "Grade Value", "Date Recorded"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel (South)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnAddGrade = new JButton("Add New Grade");
        btnEditGrade = new JButton("Edit Selected Grade");
        
        if (loggedInUser.getRole() == Role.TEACHER || loggedInUser.getRole() == Role.ADMIN) {
            buttonPanel.add(btnAddGrade);
            buttonPanel.add(btnEditGrade);
            
            lblTitle.setText("Manage Grades (Teacher Mode)");
            
            // ACTION LISTENERS FOR TEACHER FEATURES
            btnAddGrade.addActionListener(e -> openGradeEditor(null)); 
            btnEditGrade.addActionListener(e -> editSelectedGrade());
            
        } 
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadGradesData() {
        tableModel.setRowCount(0); 
        
        List<Grade> grades;
        if (loggedInUser.getRole() == Role.STUDENT) {
            // Student View: Load only their grades
            grades = gradeDAO.getStudentGrades(loggedInUser.getUserId());
        } else {
            // Teacher View: Placeholder/Temporary view for ALL grades for management/testing
            // In a production app, this would be a filtered view.
            grades = gradeDAO.getStudentGrades(1); // Temporary mock student 1's data for teachers
            lblTitle.setText("Grades for Mock Student 1 (Teacher View)");
        }
        
        // Populate the table
        for (Grade grade : grades) {
            String subjectName = subjectNameMap.getOrDefault(grade.getSubjectId(), "Unknown Subject");
            
            tableModel.addRow(new Object[]{
                grade.getGradeId(),
                subjectName,
                grade.getType(),
                grade.getGradeValue(),
                grade.getDateRecorded().toString()
            });
        }
    }
    
    // --- Teacher Actions ---

    /**
     * Opens a new modal form to add or edit a grade.
     * @param grade The existing Grade object to edit, or null to add a new one.
     */
    private void openGradeEditor(Grade grade) {
        // Launch the editor and ensure the parent form is refreshed upon closure
        GradeEditorForm editor = new GradeEditorForm(this, grade, loggedInUser);
        editor.setVisible(true);
        refreshGradesView(); // Refresh the table after the editor closes
    }
    
    /**
     * Handles the Edit button click.
     */
    private void editSelectedGrade() {
        int selectedRow = gradeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade record to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get the Grade ID from the selected row (index 0)
            int gradeId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Fetch the full Grade object from the DAO
            Grade gradeToEdit = gradeDAO.getGradeById(gradeId);
            
            if (gradeToEdit != null) {
                // Open the editor with the fetched object
                openGradeEditor(gradeToEdit);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not retrieve grade data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(this, "Internal error reading Grade ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}