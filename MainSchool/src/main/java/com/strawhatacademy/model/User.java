package com.strawhatacademy.model;

/**
 * The base class for any person who logs into the system.
 * Contains core identity and authentication information.
 */
public class User {
    
    // User ID is final as it is a unique, immutable identifier from the database
    private final int userId; 
    
    private String username;
    private Role role; 
    private String firstName;
    private String lastName;

    /**
     * Constructor for the User object.
     * @param userId The unique ID from the database.
     * @param username The user's login username.
     * @param role The user's role (Admin, Teacher, or Student).
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     */
    public User(int userId, String username, Role role, String firstName, String lastName) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // --- Getters ---
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    
    // --- Setters (For updating profile information) ---
    public void setUsername(String username) { this.username = username; }
    public void setRole(Role role) { this.role = role; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}