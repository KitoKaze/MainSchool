package com.strawhatacademy.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the JDBC connection to the MySQL database.
 */
public class DatabaseConnection {

    // IMPORTANT: Replace these with your actual XAMPP MySQL credentials
    private static final String URL = "jdbc:mysql://localhost:3306/strawhat_academy";
    private static final String USER = "root"; // Default XAMPP username
    private static final String PASSWORD = ""; // Default XAMPP password

    /**
     * Establishes and returns a database connection.
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Safely closes the database connection.
     * @param conn The Connection object to close.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}