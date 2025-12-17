package com.strawhatacademy.ui;

import com.strawhatacademy.dao.GradeDAO;
import com.strawhatacademy.dao.SubjectDAO;
import com.strawhatacademy.model.*;
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
    private Map<Integer, String> subjectNameMap;

    public GradesViewForm(User user) {
        this.loggedInUser = user;
        loadSubjectCache();
        initComponents();
        loadGradesData();
        this.setSize(900, 500);
        this.setLocationRelativeTo(null);
    }

    public Map<Integer, String> getSubjectNameMap() { return subjectNameMap; }

    private void loadSubjectCache() {
        subjectNameMap = subjectDAO.getAllSubjects().stream()
            .collect(Collectors.toMap(Subject::getSubjectId, Subject::getSubjectName));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] cols = (loggedInUser.getRole() == Role.STUDENT) ? 
            new String[]{"ID", "Subject", "Value", "Date", "Type"} : 
            new String[]{"ID", "Student Name", "Student ID", "Subject", "Value", "Date", "Type"};

        tableModel = new DefaultTableModel(cols, 0);
        gradeTable = new JTable(tableModel);
        add(new JScrollPane(gradeTable), BorderLayout.CENTER);

        if (loggedInUser.getRole() != Role.STUDENT) {
            JPanel bp = new JPanel();
            JButton btnAdd = new JButton("Add Grade");
            JButton btnEdit = new JButton("Edit Grade");
            btnAdd.addActionListener(e -> openEditor(null));
            btnEdit.addActionListener(e -> editSelected());
            bp.add(btnAdd); bp.add(btnEdit);
            add(bp, BorderLayout.SOUTH);
        }
    }

    private void loadGradesData() {
        tableModel.setRowCount(0);
        if (loggedInUser.getRole() == Role.STUDENT) {
            for (Grade g : gradeDAO.getStudentGrades(loggedInUser.getUserId())) {
                tableModel.addRow(new Object[]{g.getGradeId(), subjectNameMap.get(g.getSubjectId()), g.getGradeValue(), g.getDateRecorded(), g.getType()});
            }
        } else {
            for (Object[] row : gradeDAO.getTeacherAssignedGrades(loggedInUser.getUserId())) {
                Grade g = (Grade) row[0];
                tableModel.addRow(new Object[]{g.getGradeId(), row[1], g.getStudentId(), subjectNameMap.get(g.getSubjectId()), g.getGradeValue(), g.getDateRecorded(), g.getType()});
            }
        }
    }

    private void openEditor(Grade g) {
        new GradeEditorForm(this, g, loggedInUser).setVisible(true);
        loadGradesData();
    }

    private void editSelected() {
        int row = gradeTable.getSelectedRow();
        if (row != -1) {
            Grade g = gradeDAO.getGradeById((int) tableModel.getValueAt(row, 0));
            if (g != null) openEditor(g);
        }
    }
}