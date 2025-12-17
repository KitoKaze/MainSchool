package com.strawhatacademy.ui;

import com.strawhatacademy.dao.UserDAO;
import com.strawhatacademy.model.User;
import com.strawhatacademy.model.Role;
import javax.swing.*;
import java.awt.*;

public class RegistrationForm extends JFrame {

    // UI Components for input
    private JTextField txtFirstName, txtLastName, txtRegUsername;
    private JPasswordField txtRegPassword;
    private JComboBox<Role> cmbRole;
    private JButton btnRegister;
    private JLabel lblStatus;
    
    // DAO instance for database interaction
    private final UserDAO userDAO = new UserDAO(); 

    public RegistrationForm() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Straw Hat Academy - New User Registration");
    }
    
    /**
     * Initializes the UI components.
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
        
        // MODIFIED: Only includes TEACHER and STUDENT. ADMIN is removed.
        cmbRole = new JComboBox<>(new Role[]{Role.TEACHER, Role.STUDENT}); 
        
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);

        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(txtFirstName);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(txtLastName);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtRegUsername);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtRegPassword);
        inputPanel.add(new JLabel("Select Role:"));
        inputPanel.add(cmbRole);

        add(inputPanel, BorderLayout.CENTER);

        // Footer Panel (Buttons)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegister = new JButton("Register Account");
        btnRegister.addActionListener(e -> registerUser());
        footer.add(btnRegister);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(lblStatus, BorderLayout.NORTH);
        southPanel.add(footer, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void registerUser() {
        try {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String username = txtRegUsername.getText().trim();
            String password = new String(txtRegPassword.getPassword());
            Role role = (Role) cmbRole.getSelectedItem();
            
            if (firstName.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                lblStatus.setText("Error: All fields are required.");
                return;
            }

            User newUser = new User(0, username, role, firstName, lastName);
            boolean success = userDAO.registerUser(newUser, password);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration Successful for " + role + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); 
            } else {
                lblStatus.setText("Registration failed. Username may exist or connection failed.");
            }
        } catch (Exception ex) {
            lblStatus.setText("An unexpected system error occurred.");
            ex.printStackTrace(); 
        }
    }
}