package com.vku;

import com.vku.auth.AuthenticationManager;
import com.vku.ui.LoginPanel;
import com.vku.ui.MainFrame;
import com.vku.data.DataManager;

import javax.swing.*;
import java.awt.*;

/**
 * Main class for the Student Management Application.
 * This serves as the entry point for the application.
 */
public class StudentManagementApp {

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize custom UI settings
        initUISettings();

        // Initialize data
        DataManager.getInstance().initializeData();

        // Start the application with the login screen
        SwingUtilities.invokeLater(() -> {
            LoginPanel loginPanel = new LoginPanel();
            JFrame loginFrame = new JFrame("VKU Student Management System");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 500);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.add(loginPanel);
            loginFrame.setVisible(true);

            // Set application icon
            ImageIcon appIcon = new ImageIcon(StudentManagementApp.class.getResource("/resources/app_icon.png"));
            if (appIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                loginFrame.setIconImage(appIcon.getImage());
            }
        });
    }

    /**
     * Initialize custom UI settings for the application
     */
    private static void initUISettings() {
        // Set custom UI properties
        UIManager.put("Button.arc", 15);
        UIManager.put("Panel.background", new Color(248, 249, 250));
        UIManager.put("OptionPane.background", new Color(248, 249, 250));
        UIManager.put("Panel.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));

        // Set primary colors
        UIManager.put("Button.background", new Color(20, 184, 166)); // Teal
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", new Color(13, 148, 136)); // Darker teal

        // Set focus colors
        UIManager.put("Component.focusColor", new Color(59, 130, 246)); // Blue
        UIManager.put("TextField.selectionBackground", new Color(59, 130, 246, 128));
    }
}