package com.strawhatacademy.ui;

import com.strawhatacademy.model.User;
import com.strawhatacademy.model.Role;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

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
        
        this.setTitle("Straw Hat Academy - Dashboard (ID: " + user.getUserId() + " | Role: " + user.getRole() + ")");
    }

    private void initComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout(10, 10)); 

        // 1. Header Panel (Contains the Straw Hat Logo)
        JPanel headerPanel = createHeaderPanel();
        this.add(headerPanel, BorderLayout.NORTH);

        // 2. Main Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 240, 240)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 2a. Feature Buttons
        JPanel featurePanel = createFeaturePanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        contentPanel.add(featurePanel, gbc);

        // 2b. Calendar
        JPanel calendarPanel = createCalendarPanel();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        contentPanel.add(calendarPanel, gbc);

        this.add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * UPDATED: Logo changed from Pirate Flag to Straw Hat
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 153, 153)); 
        panel.setPreferredSize(new Dimension(800, 80));

        // STRAW HAT LOGO HERE
        JLabel lblLogo = new JLabel("👒", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Serif", Font.PLAIN, 50)); // Slightly larger for the hat
        lblLogo.setPreferredSize(new Dimension(100, 80));
        panel.add(lblLogo, BorderLayout.WEST);

        lblWelcome = new JLabel("Welcome To Straw Hat Academy", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Serif", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        panel.add(lblWelcome, BorderLayout.CENTER);

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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(10, 0, 10, 0); 

        btnGrade = new JButton("<html><center><font size='10'>📝</font><br><br>Grade</center></html>");
        styleBigButton(btnGrade);
        btnGrade.addActionListener(e -> openGradesView());
        gbc.gridy = 0;
        panel.add(btnGrade, gbc);

        btnSubject = new JButton("<html><center><font size='10'>📖</font><br><br>Subject</center></html>");
        styleBigButton(btnSubject);
        btnSubject.addActionListener(e -> openSubjectsView());
        gbc.gridy = 1;
        panel.add(btnSubject, gbc);

        return panel;
    }

    private void styleBigButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int daysInMonth = today.lengthOfMonth();
        LocalDate firstOfMonth = today.withDayOfMonth(1);
        int startingDayIndex = (firstOfMonth.getDayOfWeek().getValue() % 7); 

        JLabel lblMonth = new JLabel(
            today.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + today.getYear(), 
            SwingConstants.CENTER
        );
        lblMonth.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(lblMonth, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 7, 2, 2));
        grid.setBackground(Color.LIGHT_GRAY);

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            lbl.setBackground(new Color(230, 230, 230)); 
            lbl.setOpaque(true);
            grid.add(lbl);
        }
        
        for (int i = 0; i < startingDayIndex; i++) grid.add(new JLabel("")); 

        for (int day = 1; day <= daysInMonth; day++) {
            JLabel lblDate = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            lblDate.setOpaque(true);
            lblDate.setBackground(Color.WHITE);
            
            if (day == currentDay) {
                lblDate.setBackground(new Color(0, 153, 153));
                lblDate.setForeground(Color.WHITE);
                lblDate.setFont(new Font("SansSerif", Font.BOLD, 14));
            }
            grid.add(lblDate);
        }

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private void personalizeDashboard() {
        String role = loggedInUser.getRole().toString();
        lblWelcome.setText("Welcome, " + loggedInUser.getFirstName() + " (" + role + ")");
    }

    private void openGradesView() {
        SwingUtilities.invokeLater(() -> new GradesViewForm(loggedInUser).setVisible(true));
    }
    
    private void openSubjectsView() {
        SwingUtilities.invokeLater(() -> new SubjectsViewForm(loggedInUser).setVisible(true));
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to log out?", "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new LoginForm().setVisible(true);
                this.dispose();
            });
        }
    }
}