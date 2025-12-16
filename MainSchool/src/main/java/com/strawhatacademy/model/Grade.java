package com.strawhatacademy.model;

import java.time.LocalDate;

/**
 * Represents a student's mark for a specific type of assessment in a subject.
 */
public class Grade {
    
    private final int gradeId;
    private final int studentId;
    private final int subjectId;
    private double gradeValue;
    private LocalDate dateRecorded;
    private String type; // e.g., "Final", "Quiz", "Exam"

    /**
     * Constructor for the Grade object.
     * @param gradeId The unique ID of the grade record.
     * @param studentId The ID of the student who received the grade.
     * @param subjectId The ID of the subject the grade is for.
     * @param gradeValue The numeric value of the grade.
     * @param dateRecorded The date the grade was officially recorded.
     * @param type The type of assessment (e.g., 'Final', 'Quiz').
     */
    public Grade(int gradeId, int studentId, int subjectId, double gradeValue, LocalDate dateRecorded, String type) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.gradeValue = gradeValue;
        this.dateRecorded = dateRecorded;
        this.type = type;
    }

    // --- Getters ---
    public int getGradeId() { return gradeId; }
    public int getStudentId() { return studentId; }
    public int getSubjectId() { return subjectId; }
    public double getGradeValue() { return gradeValue; }
    public LocalDate getDateRecorded() { return dateRecorded; }
    public String getType() { return type; }

    // --- Setters (Used by teachers when updating grades) ---
    public void setGradeValue(double gradeValue) { this.gradeValue = gradeValue; }
    public void setDateRecorded(LocalDate dateRecorded) { this.dateRecorded = dateRecorded; }
    public void setType(String type) { this.type = type; } 
}