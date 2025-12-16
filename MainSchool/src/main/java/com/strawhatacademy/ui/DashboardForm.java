package com.strawhatacademy.ui;

import com.strawhatacademy.model.User;
import com.strawhatacademy.model.Role;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;

public class DashboardForm extends JFrame {

    private final User loggedInUser;
    
    // UI Components
    private JLabel lblWelcome;
    private JButton btnGrade;
    private JButton btnSubject;
    private JButton btnLogout;

    public DashboardForm(User user) {
        this.loggedInUser = user;
        initComponents();
        personalizeDashboard();
        this.setLocationRelativeTo(null);
        this.setTitle("Straw Hat Academy - Dashboard (" + user.getRole() + ")");
    }

    /**
     * Initializes the UI components with a hard-coded layout (mimicking the screenshot).
     */
    private void initComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout(10, 10)); // Outer layout

        // 1. Header Panel (Top - Teal)
        JPanel headerPanel = createHeaderPanel();
        this.add(headerPanel, BorderLayout.NORTH);

        // 2. Main Content Panel (Center - Holds features and calendar)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 240, 240)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 2a. Feature Buttons (Left)
        JPanel featurePanel = createFeaturePanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        contentPanel.add(featurePanel, gbc);

        // 2b. Calendar (Right)
        JPanel calendarPanel = createCalendarPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        contentPanel.add(calendarPanel, gbc);

        this.add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 153, 153)); // Teal color
        panel.setPreferredSize(new Dimension(800, 80));

        // Logo (Left)
        JLabel lblLogo = new JLabel("🏴‍☠️", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Serif", Font.BOLD, 40));
        lblLogo.setPreferredSize(new Dimension(80, 80));
        panel.add(lblLogo, BorderLayout.WEST);

        // Welcome Label (Center) - Updated by personalizeDashboard()
        lblWelcome = new JLabel("Welcome To Straw Hat Academy", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Serif", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        panel.add(lblWelcome, BorderLayout.CENTER);

        // Logout Button (Right)
        btnLogout = new JButton("Logout ▼");
        btnLogout.setPreferredSize(new Dimension(100, 30));
        btnLogout.addActionListener(e -> logout());
        
        JPanel logoutContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutContainer.setBackground(new Color(0, 153, 153));
        logoutContainer.add(btnLogout);
        panel.add(logoutContainer, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFeaturePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20)); // Two rows for buttons
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        // Grade Button
        btnGrade = createFeatureButton("Grade", "📝");
        btnGrade.addActionListener(e -> openGradesView());
        panel.add(btnGrade);

        // Subject Button
        btnSubject = createFeatureButton("Subject", "📖");
        btnSubject.addActionListener(e -> openSubjectsView());
        panel.add(btnSubject);

        return panel;
    }
    
    private JButton createFeatureButton(String text, String icon) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>");
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Header
        LocalDate today = LocalDate.now();
        JLabel lblMonth = new JLabel(today.getMonth().toString() + " " + today.getYear(), SwingConstants.CENTER);
        lblMonth.setFont(new Font("Serif", Font.BOLD, 16));
        panel.add(lblMonth, BorderLayout.NORTH);

        // Calendar Grid (Simplified/Mocked for UI)
        JPanel grid = new JPanel(new GridLayout(7, 7, 1, 1)); 
        grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        grid.setBackground(Color.LIGHT_GRAY);

        String[] days = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("Serif", Font.BOLD, 12));
            lbl.setBackground(Color.GRAY);
            lbl.setOpaque(true);
            grid.add(lbl);
        }
        
        // Mock dates (just to fill the grid)
        int date = 1;
        for (int i = 0; i < 35; i++) { 
            if (date <= 30) {
                JLabel lblDate = new JLabel(String.valueOf(date++), SwingConstants.CENTER);
                lblDate.setBackground(Color.WHITE);
                lblDate.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                lblDate.setOpaque(true);
                grid.add(lblDate);
            } else {
                grid.add(new JLabel(""));
            }
        }
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Updates the welcome message based on the logged-in user's name and role.
     */
    private void personalizeDashboard() {
        String role = loggedInUser.getRole().toString();
        // Set the personalized welcome message
        lblWelcome.setText("Welcome, " + loggedInUser.getFirstName() + " (" + role + ")");
        
        // Optional: Hide/Disable features for certain roles (e.g., if a role didn't need grades)
        if (loggedInUser.getRole() == Role.ADMIN) {
            // Admins might have an additional 'User Management' button
        }
    }

    /**
     * Navigation: Opens the Grades View Form.
     */
    private void openGradesView() {
        // Pass the logged-in User object to the Grades view
        SwingUtilities.invokeLater(() -> new GradesViewForm(loggedInUser).setVisible(true));
    }
    
    /**
     * Navigation: Opens the Subjects View Form.
     */
    private void openSubjectsView() {
        // Pass the logged-in User object to the Subjects view
        SwingUtilities.invokeLater(() -> new SubjectsViewForm(loggedInUser).setVisible(true));
    }

    /**
     * Logs the user out and returns to the LoginForm.
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to log out?", "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new LoginForm().setVisible(true); // Open Login form
                this.dispose(); // Close Dashboard
            });
        }
    }
}