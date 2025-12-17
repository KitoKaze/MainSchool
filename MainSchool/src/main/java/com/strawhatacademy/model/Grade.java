package com.strawhatacademy.model;

import java.time.LocalDate;

public class Grade {
    private final int gradeId;
    private final int studentId;
    private final int subjectId;
    private double gradeValue;
    private LocalDate dateRecorded;
    private String type;

    public Grade(int gradeId, int studentId, int subjectId, double gradeValue, LocalDate dateRecorded, String type) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.gradeValue = gradeValue;
        this.dateRecorded = dateRecorded;
        this.type = type;
    }

    public int getGradeId() { return gradeId; }
    public int getStudentId() { return studentId; }
    public int getSubjectId() { return subjectId; }
    public double getGradeValue() { return gradeValue; }
    public LocalDate getDateRecorded() { return dateRecorded; }
    public String getType() { return type; }

    public void setGradeValue(double gradeValue) { this.gradeValue = gradeValue; }
    public void setDateRecorded(LocalDate date) { this.dateRecorded = date; }
    public void setType(String type) { this.type = type; }
}