package com.strawhatacademy.dao;

import com.strawhatacademy.model.Grade;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    public List<Grade> getStudentGrades(int studentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT grade_id, subject_id, grade_value, date_recorded, type " +
                     "FROM grades WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grades.add(new Grade(
                        rs.getInt("grade_id"),
                        studentId, 
                        rs.getInt("subject_id"),
                        rs.getDouble("grade_value"),
                        rs.getDate("date_recorded").toLocalDate(),
                        rs.getString("type")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student grades: " + e.getMessage());
        }
        return grades;
    }

    /**
     * Retrieves grades for subjects taught by a teacher, including student names.
     */
    public List<Object[]> getTeacherAssignedGrades(int teacherId) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT g.*, p.first_name, p.last_name " +
                     "FROM grades g " +
                     "JOIN subjects s ON g.subject_id = s.subject_id " +
                     "JOIN profiles p ON g.student_id = p.user_id " +
                     "WHERE s.teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grade grade = new Grade(
                        rs.getInt("grade_id"),
                        rs.getInt("student_id"),
                        rs.getInt("subject_id"),
                        rs.getDouble("grade_value"),
                        rs.getDate("date_recorded").toLocalDate(),
                        rs.getString("type")
                    );
                    String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                    results.add(new Object[]{grade, studentName});
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching teacher grades: " + e.getMessage());
        }
        return results;
    }

    public boolean addGrade(Grade grade) {
        String sql = "INSERT INTO grades (student_id, subject_id, grade_value, date_recorded, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grade.getStudentId());
            ps.setInt(2, grade.getSubjectId());
            ps.setDouble(3, grade.getGradeValue());
            ps.setDate(4, Date.valueOf(grade.getDateRecorded()));
            ps.setString(5, grade.getType());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateGrade(Grade grade) {
        String sql = "UPDATE grades SET grade_value = ?, date_recorded = ?, type = ? WHERE grade_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, grade.getGradeValue());
            ps.setDate(2, Date.valueOf(grade.getDateRecorded()));
            ps.setString(3, grade.getType());
            ps.setInt(4, grade.getGradeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Grade getGradeById(int gradeId) {
        String sql = "SELECT * FROM grades WHERE grade_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gradeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Grade(
                        rs.getInt("grade_id"),
                        rs.getInt("student_id"),
                        rs.getInt("subject_id"),
                        rs.getDouble("grade_value"),
                        rs.getDate("date_recorded").toLocalDate(),
                        rs.getString("type")
                    );
                }
            }
        } catch (SQLException e) {}
        return null;
    }
}