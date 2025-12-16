package com.strawhatacademy.model;

import java.util.List;

/**
 * Represents a student in the academy, inheriting core details from User.
 * Holds a list of their grades.
 */
public class Student extends User {
    
    private List<Grade> grades;

    /**
     * Constructor for the Student object.
     * @param userId The unique ID from the database (inherited from User).
     * @param username The student's login username.
     * @param firstName The student's first name.
     * @param lastName The student's last name.
     */
    public Student(int userId, String username, String firstName, String lastName) {
        // Role is implicitly STUDENT
        super(userId, username, Role.STUDENT, firstName, lastName);
    }

    // --- Getters and Setters for Grades ---
    public List<Grade> getGrades() { return grades; }

    public void setGrades(List<Grade> grades) { this.grades = grades; }
    
    /**
     * Calculates the average grade for this student.
     * @return The calculated average grade value, or 0.0 if no grades exist.
     */
    public double calculateAverageGrade() {
        if (grades == null || grades.isEmpty()) return 0.0;
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getGradeValue();
        }
        return sum / grades.size();
    }
}