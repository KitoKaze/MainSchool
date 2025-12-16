package com.strawhatacademy.dao;

import com.strawhatacademy.model.Grade;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Grade records in the database.
 * Handles CRUD operations related to student grades.
 */
public class GradeDAO {

    /**
     * Retrieves all grades for a specific student.
     * @param studentId The user_id of the student whose grades are to be fetched.
     * @return A list of Grade objects for the specified student.
     */
    public List<Grade> getStudentGrades(int studentId) {
        List<Grade> grades = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT grade_id, subject_id, grade_value, date_recorded, type " +
                     "FROM grades WHERE student_id = ?";

        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, studentId);
                rs = ps.executeQuery();

                while (rs.next()) {
                    LocalDate date = rs.getDate("date_recorded").toLocalDate();
                    
                    Grade grade = new Grade(
                        rs.getInt("grade_id"),
                        studentId, 
                        rs.getInt("subject_id"),
                        rs.getDouble("grade_value"),
                        date,
                        rs.getString("type")
                    );
                    grades.add(grade);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student grades: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return grades;
    }

    /**
     * Adds a new grade record to the database (used primarily by Teachers).
     * @param grade The Grade object containing studentId, subjectId, gradeValue, and type.
     * @return true if the grade was added successfully, false otherwise.
     */
    public boolean addGrade(Grade grade) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO grades (student_id, subject_id, grade_value, date_recorded, type) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, grade.getStudentId());
                ps.setInt(2, grade.getSubjectId());
                ps.setDouble(3, grade.getGradeValue());
                ps.setDate(4, Date.valueOf(grade.getDateRecorded()));
                ps.setString(5, grade.getType());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error adding grade: " + e.getMessage());
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    /**
     * Updates an existing grade record in the database (used by Teachers).
     * It uses the Grade object's gradeId to locate the record.
     * @param grade The Grade object containing the gradeId, new gradeValue, and dateRecorded.
     * @return true if the grade was updated successfully, false otherwise.
     */
    public boolean updateGrade(Grade grade) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "UPDATE grades SET grade_value = ?, date_recorded = ?, type = ? WHERE grade_id = ?";
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                
                ps.setDouble(1, grade.getGradeValue());
                ps.setDate(2, Date.valueOf(grade.getDateRecorded()));
                ps.setString(3, grade.getType());
                ps.setInt(4, grade.getGradeId());

                int affectedRows = ps.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error updating grade ID " + grade.getGradeId() + ": " + e.getMessage());
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    /**
     * Retrieves a single Grade object by its unique ID.
     * @param gradeId The unique ID of the grade to retrieve.
     * @return A Grade object if a record is found, or null if no matching ID exists.
     */
    public Grade getGradeById(int gradeId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Grade grade = null;
        
        String sql = "SELECT * FROM grades WHERE grade_id = ?";
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, gradeId);
                rs = ps.executeQuery();
                
                if (rs.next()) {
                    LocalDate date = rs.getDate("date_recorded").toLocalDate();
                    
                    grade = new Grade(
                        rs.getInt("grade_id"),
                        rs.getInt("student_id"),
                        rs.getInt("subject_id"),
                        rs.getDouble("grade_value"),
                        date,
                        rs.getString("type")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching grade by ID: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return grade;
    }
}