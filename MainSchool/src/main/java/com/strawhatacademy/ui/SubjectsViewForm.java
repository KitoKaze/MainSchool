package com.strawhatacademy.ui;

import com.strawhatacademy.dao.SubjectDAO;
import com.strawhatacademy.dao.UserDAO;
import com.strawhatacademy.model.Role;
import com.strawhatacademy.model.Subject;
import com.strawhatacademy.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubjectsViewForm extends JFrame {

    private final User loggedInUser;
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final UserDAO userDAO = new UserDAO();

    private JTable subjectTable;
    private DefaultTableModel tableModel;
    private JLabel lblTitle;
    private Map<Integer, String> teacherNameMap;

    public SubjectsViewForm(User user) {
        this.loggedInUser = user;
        loadTeacherCache(); 
        initComponents();
        loadSubjectData();
        this.setLocationRelativeTo(null);
        this.setTitle("Subjects View");
    }

    private void loadTeacherCache() {
        teacherNameMap = userDAO.getAllUsers().stream()
            .collect(Collectors.toMap(User::getUserId, u -> u.getFirstName() + " " + u.getLastName()));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));

        lblTitle = new JLabel("Subjects", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        String[] columnNames;
        if (loggedInUser.getRole() == Role.STUDENT) {
            columnNames = new String[]{"Subject ID", "Subject Name", "Assigned Teacher ID"};
            lblTitle.setText("All Available Subjects");
        } else {
            columnNames = new String[]{"Subject ID", "Subject Name", "Instructor", "Status"};
            lblTitle.setText(loggedInUser.getRole() == Role.ADMIN ? "Admin: Subject Management" : "Your Assigned Subjects");
        }

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        subjectTable = new JTable(tableModel);
        add(new JScrollPane(subjectTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Show Admin/Teacher buttons
        if (loggedInUser.getRole() == Role.ADMIN || loggedInUser.getRole() == Role.TEACHER) {
            JButton btnAdd = new JButton("Add Subject");
            JButton btnRemove = new JButton("Remove Subject");
            btnAdd.addActionListener(e -> addNewSubject());
            btnRemove.addActionListener(e -> removeSelectedSubject());
            buttonPanel.add(btnAdd);
            buttonPanel.add(btnRemove);
        } 
        
        // --- NEW: Show Student Enroll Button ---
        if (loggedInUser.getRole() == Role.STUDENT) {
            JButton btnEnroll = new JButton("Enroll in Selected Subject");
            btnEnroll.addActionListener(e -> handleEnrollment());
            buttonPanel.add(btnEnroll);
        }

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleEnrollment() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to enroll in.");
            return;
        }

        int subjectId = (int) tableModel.getValueAt(selectedRow, 0);
        String subjectName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Enroll in " + subjectName + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (subjectDAO.enrollStudent(loggedInUser.getUserId(), subjectId)) {
                JOptionPane.showMessageDialog(this, "Successfully enrolled in " + subjectName + "!");
            } else {
                JOptionPane.showMessageDialog(this, "Enrollment failed. You might already be enrolled.");
            }
        }
    }

    private void addNewSubject() {
        String name = JOptionPane.showInputDialog(this, "Enter Subject Name:");
        if (name != null && !name.trim().isEmpty()) {
            if (subjectDAO.addSubject(name.trim(), loggedInUser.getUserId())) {
                loadSubjectData();
            }
        }
    }

    private void removeSelectedSubject() {
        int row = subjectTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (subjectDAO.deleteSubject(id)) {
                loadSubjectData();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot remove subject with existing grades.");
            }
        }
    }

    private void loadSubjectData() {
        tableModel.setRowCount(0);
        List<Subject> subjects = (loggedInUser.getRole() == Role.STUDENT || loggedInUser.getRole() == Role.ADMIN) 
                                ? subjectDAO.getAllSubjects() : subjectDAO.getTeacherSubjects(loggedInUser.getUserId());
        for (Subject s : subjects) {
            if (loggedInUser.getRole() == Role.STUDENT) {
                tableModel.addRow(new Object[]{s.getSubjectId(), s.getSubjectName(), s.getTeacherId()});
            } else {
                tableModel.addRow(new Object[]{s.getSubjectId(), s.getSubjectName(), teacherNameMap.get(s.getTeacherId()), "N/A"});
            }
        }
    }
}