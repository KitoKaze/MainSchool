package com.strawhatacademy.dao;

import com.strawhatacademy.model.Role;
import com.strawhatacademy.model.User;
import java.sql.*;
import java.time.LocalDate;

/**
 * Handles all user authentication and profile creation/retrieval.
 */
public class UserDAO {

    /**
     * Authenticates a user based on username and password.
     * NOTE: In a real system, the password must be compared using a secure hash library (e.g., BCrypt).
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return A fully populated User object if login is successful, null otherwise.
     */
    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        String sql = "SELECT u.user_id, u.role, p.first_name, p.last_name " +
                     "FROM users u JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE u.username = ? AND u.password_hash = ?";

        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password); 
                
                rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    // Convert String role from DB to Java Enum
                    Role role = Role.valueOf(rs.getString("role").toUpperCase());
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    
                    user = new User(id, username, role, firstName, lastName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Login Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return user;
    }
    
    /**
     * Registers a new user, inserting records into both the 'users' and 'profiles' tables.
     * @param user The User object containing username, role, and names.
     * @param password The plain text password to be hashed and stored.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerUser(User user, String password) {
        Connection conn = null;
        PreparedStatement psUser = null;
        PreparedStatement psProfile = null;
        boolean success = false;
        
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            
            // 1. Insert into users table
            String sqlUser = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
            psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, user.getUsername());
            psUser.setString(2, password); 
            psUser.setString(3, user.getRole().toString());

            int affectedRows = psUser.executeUpdate();
            if (affectedRows == 0) return false;

            int userId = -1;
            try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }
            }
            if (userId == -1) return false;

            // 2. Insert into profiles table
            String sqlProfile = "INSERT INTO profiles (user_id, first_name, last_name, registration_date) VALUES (?, ?, ?, ?)";
            psProfile = conn.prepareStatement(sqlProfile);
            psProfile.setInt(1, userId);
            psProfile.setString(2, user.getFirstName());
            psProfile.setString(3, user.getLastName());
            psProfile.setDate(4, Date.valueOf(LocalDate.now()));

            if (psProfile.executeUpdate() > 0) {
                success = true; 
            }
            
        } catch (SQLException e) {
            System.err.println("Registration Error: " + e.getMessage());
        } finally {
            try { if (psUser != null) psUser.close(); } catch (SQLException e) {}
            try { if (psProfile != null) psProfile.close(); } catch (SQLException e) {}
            DatabaseConnection.closeConnection(conn);
        }
        return success;
    }
}