package com.strawhatacademy.ui;

import com.strawhatacademy.dao.UserDAO;
import com.strawhatacademy.model.User;
import com.strawhatacademy.model.Role;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblMessage;
    
    // DAO instance (made final as recommended)
    private final UserDAO userDAO = new UserDAO(); 

    public LoginForm() {
        initComponents();
        this.setLocationRelativeTo(null); 
        this.setTitle("Straw Hat Academy - Login");
    }
    
    /**
     * Initializes the UI components, now correctly adding input fields.
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("Welcome to Straw Hat Academy");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 18));
        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        // Input Panel (Center)
        // **THIS SECTION IS KEY: We use a GridLayout to stack labels and inputs**
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setForeground(Color.RED);

        // --- ADDING THE MISSING COMPONENTS ---
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtUsername);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtPassword);
        inputPanel.add(new JLabel("")); // Empty cell for alignment
        inputPanel.add(lblMessage);     // Message label for status updates
        // ------------------------------------

        add(inputPanel, BorderLayout.CENTER);
        
        // Button Panel (South)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        
        // --- Setting up Actions ---
        btnLogin.addActionListener(e -> loginUser());
        btnRegister.addActionListener(e -> openRegistration());
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the login button action.
     */
    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter both username and password.");
            return;
        }

        // Call DAO to authenticate
        User loggedInUser = userDAO.login(username, password);
        
        if (loggedInUser != null) {
            lblMessage.setText("Login successful! Redirecting...");
            
            // Open the appropriate Dashboard
            SwingUtilities.invokeLater(() -> {
                new DashboardForm(loggedInUser).setVisible(true);
                this.dispose(); // Close the login window
            });
        } else {
            lblMessage.setText("Error: Invalid username or password.");
            txtPassword.setText(""); // Clear password field on failure
        }
    }
    
    /**
     * Opens the Registration form.
     */
    private void openRegistration() {
        new RegistrationForm().setVisible(true);
    }
}