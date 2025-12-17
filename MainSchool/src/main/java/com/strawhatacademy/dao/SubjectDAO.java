package com.strawhatacademy.dao;

import com.strawhatacademy.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT subject_id, subject_name, teacher_id FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                subjects.add(new Subject(rs.getInt("subject_id"), rs.getString("subject_name"), rs.getInt("teacher_id")));
            }
        } catch (SQLException e) { 
            System.err.println("Error fetching all subjects: " + e.getMessage()); 
        }
        return subjects;
    }

    public List<Subject> getTeacherSubjects(int teacherId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT subject_id, subject_name FROM subjects WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subjects.add(new Subject(rs.getInt("subject_id"), rs.getString("subject_name"), teacherId));
                }
            }
        } catch (SQLException e) { 
            System.err.println("Error fetching teacher subjects: " + e.getMessage()); 
        }
        return subjects;
    }

    /**
     * Enrolls a student in a subject by creating an initial record in the grades table.
     * Changed status from 'Enrolled' to 'Registered' to fix ENUM constraint errors.
     */
    public boolean enrollStudent(int studentId, int subjectId) {
        // SQL now uses 'Registered' which must be added to your MySQL ENUM list
        String sql = "INSERT INTO grades (student_id, subject_id, grade_value, date_recorded, type) VALUES (?, ?, 0.0, ?, 'Registered')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Updated error message to help with debugging
            System.err.println("Enrollment Error: " + e.getMessage());
            return false;
        }
    }

    public boolean addSubject(String name, int teacherId) {
        String sql = "INSERT INTO subjects (subject_name, teacher_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, teacherId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println("Error adding subject: " + e.getMessage());
            return false; 
        }
    }

    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println("Error deleting subject: " + e.getMessage());
            return false; 
        }
    }
}