package com.strawhatacademy.ui;

import com.strawhatacademy.dao.SubjectDAO;
import com.strawhatacademy.model.Role;
import com.strawhatacademy.model.Subject;
import com.strawhatacademy.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubjectsViewForm extends JFrame {

    private final User loggedInUser;
    private final SubjectDAO subjectDAO = new SubjectDAO();

    private JTable subjectTable;
    private DefaultTableModel tableModel;
    private JLabel lblTitle;
    private JButton btnViewDetails; // Placeholder for future feature

    public SubjectsViewForm(User user) {
        this.loggedInUser = user;
        initComponents();
        loadSubjectData(); // Load data immediately
        
        this.setLocationRelativeTo(null);
        this.setTitle("Subjects View");
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        lblTitle = new JLabel("Subjects Taught", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Define Columns based on Role
        String[] columnNames;
        if (loggedInUser.getRole() == Role.STUDENT) {
            columnNames = new String[]{"Subject ID", "Subject Name", "Assigned Teacher ID"};
            lblTitle.setText("All Available Subjects");
        } else {
            // Teacher/Admin
            columnNames = new String[]{"Subject ID", "Subject Name", "Enrollment Count (Future)"};
            lblTitle.setText("Subjects Assigned to You");
        }
        
        // Table Setup
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel (South)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnViewDetails = new JButton("View Details (Future)");
        buttonPanel.add(btnViewDetails);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Loads subject data into the JTable based on the user's role.
     */
    private void loadSubjectData() {
        tableModel.setRowCount(0); // Clear existing data
        List<Subject> subjects;

        if (loggedInUser.getRole() == Role.TEACHER || loggedInUser.getRole() == Role.ADMIN) {
            // Teacher/Admin View: Load only the subjects assigned to the user
            subjects = subjectDAO.getTeacherSubjects(loggedInUser.getUserId());
            
            // Populate the table (Note: Enrollment Count is mocked for now)
            for (Subject subject : subjects) {
                tableModel.addRow(new Object[]{
                    subject.getSubjectId(),
                    subject.getSubjectName(),
                    "N/A" // Placeholder for enrollment count
                });
            }
        } else {
            // Student View: Load all subjects available in the academy
            subjects = subjectDAO.getAllSubjects();
            
            // Populate the table
            for (Subject subject : subjects) {
                tableModel.addRow(new Object[]{
                    subject.getSubjectId(),
                    subject.getSubjectName(),
                    subject.getTeacherId()
                });
            }
        }
    }
}