package com.strawhatacademy;

import com.strawhatacademy.ui.LoginForm;
import javax.swing.SwingUtilities;

/**
 * The main entry point for the Straw Hat Academy Information System.
 * This class ensures the UI is started correctly on the Event Dispatch Thread (EDT).
 */
public class App {
    public static void main(String[] args) {
        
        // 1. Ensure the JDBC Driver is loaded (best practice, though often automatic)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            // Handle case where the MySQL JAR is not in the NetBeans project libraries
            System.err.println("FATAL ERROR: MySQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }
        
        // 2. Start the Swing UI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Initialize the main login form
                new LoginForm().setVisible(true);
            }
        });
        
        // --- OOP Design Visualization ---
        // The structure we built follows a classic layered architecture:
        // 
    }
}