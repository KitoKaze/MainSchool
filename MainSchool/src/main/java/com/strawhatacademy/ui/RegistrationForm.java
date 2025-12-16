package com.strawhatacademy.ui;

import com.strawhatacademy.dao.UserDAO;
import com.strawhatacademy.model.User;
import com.strawhatacademy.model.Role;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException; // Explicitly import SQLException for better handling

public class RegistrationForm extends JFrame {

    // UI Components for input
    private JTextField txtFirstName, txtLastName, txtRegUsername;
    private JPasswordField txtRegPassword;
    private JComboBox<Role> cmbRole;
    private JButton btnRegister;
    private JLabel lblStatus;
    
    // DAO instance for database interaction (declared final as recommended)
    private final UserDAO userDAO = new UserDAO(); 

    public RegistrationForm() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Straw Hat Academy - New User Registration");
    }
    
    /**
     * Initializes the UI components (Hard-coded layout).
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel lblTitle = new JLabel("New User Registration", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Input Panel (Center)
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        txtFirstName = new JTextField(20);
        txtLastName = new JTextField(20);
        txtRegUsername = new JTextField(20);
        txtRegPassword = new JPasswordField(20);
        cmbRole = new JComboBox<>(Role.values()); // Populates the dropdown with Role enum values
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setForeground(Color.BLUE);

        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(txtFirstName);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(txtLastName);
        inputPanel.add(new JLabel("Desired Username:"));
        inputPanel.add(txtRegUsername);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtRegPassword);
        inputPanel.add(new JLabel("Select Role:"));
        inputPanel.add(cmbRole);

        add(inputPanel, BorderLayout.CENTER);
        
        // Status and Button Panel (South)
        JPanel southPanel = new JPanel(new BorderLayout());
        
        btnRegister = new JButton("Complete Registration");
        btnRegister.addActionListener(e -> registerNewUser());
        
        southPanel.add(lblStatus, BorderLayout.NORTH);
        southPanel.add(btnRegister, BorderLayout.SOUTH);
        
        add(southPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Handles the registration button action, validating and sending data to the DAO.
     */
    private void registerNewUser() {
        try {
            // 1. Collect and Validate Input
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String username = txtRegUsername.getText().trim();
            String password = new String(txtRegPassword.getPassword());
            Role role = (Role) cmbRole.getSelectedItem();
            
            if (firstName.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                lblStatus.setText("Error: All fields are required.");
                return;
            }

            // 2. Create the User Model object
            User newUser = new User(0, username, role, firstName, lastName);
            
            // 3. Call DAO to insert data
            boolean success = userDAO.registerUser(newUser, password);
            
            if (success) {
                lblStatus.setText("Registration successful! You can now log in.");
                JOptionPane.showMessageDialog(this, 
                    "Registration Successful for " + role + "!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); 
            } else {
                // If DAO returns false, it often implies a constraint violation (like duplicate username)
                lblStatus.setText("Registration failed. Username may already exist or connection failed.");
            }
        } catch (Exception ex) {
            // Catch any unexpected runtime errors (Keep printStackTrace for debugging purposes)
            lblStatus.setText("An unexpected system error occurred. Check console for details.");
            ex.printStackTrace(); 
        }
    }
}