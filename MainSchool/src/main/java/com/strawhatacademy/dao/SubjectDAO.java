package com.strawhatacademy.dao;

import com.strawhatacademy.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles fetching and managing subject (course) information.
 */
public class SubjectDAO {

    /**
     * Retrieves all subjects available in the academy.
     * @return A list of all Subject objects.
     */
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT subject_id, subject_name, teacher_id FROM subjects";
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Subject subject = new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        rs.getInt("teacher_id")
                    );
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all subjects: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return subjects;
    }

    /**
     * Retrieves subjects taught by a specific teacher.
     * @param teacherId The user_id of the teacher.
     * @return A list of Subject objects taught by the teacher.
     */
    public List<Subject> getTeacherSubjects(int teacherId) {
        List<Subject> subjects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT subject_id, subject_name FROM subjects WHERE teacher_id = ?";
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, teacherId);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Subject subject = new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        teacherId 
                    );
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching teacher subjects: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return subjects;
    }
}