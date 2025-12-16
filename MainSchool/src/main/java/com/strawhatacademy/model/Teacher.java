package com.strawhatacademy.model;

import java.util.List;

/**
 * Represents a teacher in the academy, inheriting core details from User.
 * Holds a list of the subjects they are assigned to teach.
 */
public class Teacher extends User {
    
    private List<Subject> subjectsTaught;

    /**
     * Constructor for the Teacher object.
     * @param userId The unique ID from the database (inherited from User).
     * @param username The teacher's login username.
     * @param firstName The teacher's first name.
     * @param lastName The teacher's last name.
     */
    public Teacher(int userId, String username, String firstName, String lastName) {
        // Role is implicitly TEACHER
        super(userId, username, Role.TEACHER, firstName, lastName);
    }

    // --- Getters and Setters for Subjects ---
    public List<Subject> getSubjectsTaught() { return subjectsTaught; }

    public void setSubjectsTaught(List<Subject> subjectsTaught) { this.subjectsTaught = subjectsTaught; }
}