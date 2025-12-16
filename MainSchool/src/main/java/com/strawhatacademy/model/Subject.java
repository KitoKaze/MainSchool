package com.strawhatacademy.model;

/**
 * Represents a course or subject offered in the academy.
 */
public class Subject {
    
    private final int subjectId;
    private String subjectName;
    private int teacherId; 

    /**
     * Constructor for the Subject object.
     * @param subjectId The unique ID of the subject.
     * @param subjectName The name of the course.
     * @param teacherId The ID of the teacher assigned to the subject.
     */
    public Subject(int subjectId, String subjectName, int teacherId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.teacherId = teacherId;
    }

    // --- Getters ---
    public int getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public int getTeacherId() { return teacherId; }
    
    // --- Setters ---
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
}