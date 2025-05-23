package com.vku.ui;

import com.vku.auth.AuthenticationManager;
import com.vku.ui.components.ModernButton;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.components.RoundedTextField;

import javax.swing.*;
import java.awt.*;

/**
 * Login panel for the application with role selection, email and password fields.
 */
public class LoginPanel extends ModernPanel {
    private JComboBox<String> roleComboBox;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    
    public LoginPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // Center panel with login form
        JPanel centerPanel = new ModernPanel();
        centerPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        // Add logo/title
        JLabel titleLabel = new JLabel("VKU Student Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 42, 120)); // Dark blue
        
        // Logo placeholder
        ImageIcon logoIcon = createLogoIcon();
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Role selection
        JLabel roleLabel = new JLabel("Select Role:");
        roleComboBox = new JComboBox<>(new String[]{"Teacher", "Student"});
        roleComboBox.setPreferredSize(new Dimension(200, 30));
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailField = new RoundedTextField(15);
        emailField.setPreferredSize(new Dimension(200, 30));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 30));
        
        // Login button
        ModernButton loginButton = new ModernButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setBackground(new Color(20, 184, 166)); // Teal
        
        // Error label
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(logoLabel, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(titleLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        centerPanel.add(roleLabel, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        centerPanel.add(roleComboBox, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        centerPanel.add(emailLabel, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 10, 10);
        centerPanel.add(emailField, gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 5, 10);
        centerPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 10, 20, 10);
        centerPanel.add(passwordField, gbc);
        
        gbc.gridy = 8;
        centerPanel.add(loginButton, gbc);
        
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(errorLabel, gbc);
        
        // Add login action
        loginButton.addActionListener(e -> {
            String role = (String) roleComboBox.getSelectedItem();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter email and password");
                return;
            }
            
            if (AuthenticationManager.authenticate(role, email, password)) {
                // Open main application window
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                loginFrame.dispose();
                
                SwingUtilities.invokeLater(() -> {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                });
            } else {
                errorLabel.setText("Invalid credentials. Please try again.");
            }
        });
        
        // Add the form panel to the main panel
        add(centerPanel, BorderLayout.CENTER);
        
        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(30, 42, 120)); // Dark blue
        footerPanel.setPreferredSize(new Dimension(400, 40));
        JLabel footerLabel = new JLabel("© 2025 VKU University");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates a simple logo icon for the login screen
     */
    private ImageIcon createLogoIcon() {
        // Create a simple icon with text as a placeholder
        JLabel iconLabel = new JLabel("VKU");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(30, 42, 120)); // Dark blue
        iconLabel.setPreferredSize(new Dimension(80, 80));
        
        BufferedImage image = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Draw a rounded rectangle
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(30, 42, 120)); // Dark blue
        g2d.fillRoundRect(0, 0, 79, 79, 20, 20);
        
        // Draw the text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth("VKU");
        int textHeight = fm.getHeight();
        g2d.drawString("VKU", (80 - textWidth) / 2, 40 + textHeight / 4);
        
        g2d.dispose();
        
        return new ImageIcon(image);
    }
}