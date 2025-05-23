package com.vku.ui;

import com.vku.auth.AuthenticationManager;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.components.SidebarButton;
import com.vku.ui.panels.*;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main application frame with sidebar navigation and content panels.
 */
public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    private JButton logoutButton;
    private JButton notificationsButton;
    
    // Content panels
    private HomePanel homePanel;
    private StudentInfoPanel studentInfoPanel;
    private GradesPanel gradesPanel;
    private ProjectsPanel projectsPanel;
    private AttendancePanel attendancePanel;
    private FeesPanel feesPanel;
    private MessagesPanel messagesPanel;
    private NotificationsPanel notificationsPanel;
    
    public MainFrame() {
        setTitle("VKU Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    private void initComponents() {
        // Create sidebar
        createSidebar();
        
        // Create top bar
        createTopBar();
        
        // Create content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Create and add content panels
        createContentPanels();
        
        // Add panels to the frame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        // Show home panel by default
        cardLayout.show(contentPanel, "Home");
    }
    
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(UIConstants.SIDEBAR_BG_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        
        // Add logo/title
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(UIConstants.SIDEBAR_BG_COLOR);
        logoPanel.setMaximumSize(new Dimension(220, 100));
        JLabel titleLabel = new JLabel("VKU Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        logoPanel.add(titleLabel);
        
        // Create sidebar buttons
        SidebarButton homeButton = new SidebarButton("Home", "\uD83C\uDFE0");
        SidebarButton studentInfoButton = new SidebarButton("Student Info", "\uD83D\uDC64");
        SidebarButton gradesButton = new SidebarButton("Grades", "\uD83D\uDCCA");
        SidebarButton projectsButton = new SidebarButton("Projects", "\uD83D\uDCBB");
        SidebarButton attendanceButton = new SidebarButton("Attendance", "\uD83D\uDCC5");
        SidebarButton feesButton = new SidebarButton("Fees", "\uD83D\uDCB0");
        SidebarButton messagesButton = new SidebarButton("Messages", "\uD83D\uDCE8");
        
        // Add action listeners
        homeButton.addActionListener(e -> cardLayout.show(contentPanel, "Home"));
        studentInfoButton.addActionListener(e -> cardLayout.show(contentPanel, "StudentInfo"));
        gradesButton.addActionListener(e -> cardLayout.show(contentPanel, "Grades"));
        projectsButton.addActionListener(e -> cardLayout.show(contentPanel, "Projects"));
        attendanceButton.addActionListener(e -> cardLayout.show(contentPanel, "Attendance"));
        feesButton.addActionListener(e -> cardLayout.show(contentPanel, "Fees"));
        messagesButton.addActionListener(e -> cardLayout.show(contentPanel, "Messages"));
        
        // Add components to sidebar
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(homeButton);
        sidebarPanel.add(studentInfoButton);
        sidebarPanel.add(gradesButton);
        sidebarPanel.add(projectsButton);
        sidebarPanel.add(attendanceButton);
        sidebarPanel.add(feesButton);
        sidebarPanel.add(messagesButton);
        
        // Add filler to push buttons to the top
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Add version at bottom
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setForeground(new Color(200, 200, 200));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(versionLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private void createTopBar() {
        JPanel topBarPanel = new JPanel();
        topBarPanel.setBackground(Color.WHITE);
        topBarPanel.setPreferredSize(new Dimension(getWidth(), 60));
        topBarPanel.setLayout(new BorderLayout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        
        // Notification button
        notificationsButton = new JButton("\uD83D\uDD14"); // Bell emoji
        notificationsButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        notificationsButton.setBorderPainted(false);
        notificationsButton.setFocusPainted(false);
        notificationsButton.setContentAreaFilled(false);
        
        // User profile/logout button
        String userName = AuthenticationManager.getCurrentUser().getName();
        logoutButton = new JButton(userName + " \uD83D\uDD3D"); // Down arrow emoji
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        
        notificationsButton.addActionListener(e -> cardLayout.show(contentPanel, "Notifications"));
        
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                AuthenticationManager.logout();
                dispose();
                
                SwingUtilities.invokeLater(() -> {
                    LoginPanel loginPanel = new LoginPanel();
                    JFrame loginFrame = new JFrame("VKU Student Management System");
                    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginFrame.setSize(400, 500);
                    loginFrame.setLocationRelativeTo(null);
                    loginFrame.add(loginPanel);
                    loginFrame.setVisible(true);
                });
            }
        });
        
        rightPanel.add(notificationsButton);
        rightPanel.add(logoutButton);
        
        topBarPanel.add(rightPanel, BorderLayout.EAST);
        
        add(topBarPanel, BorderLayout.NORTH);
    }
    
    private void createContentPanels() {
        // Create panels based on user role
        homePanel = new HomePanel();
        studentInfoPanel = new StudentInfoPanel();
        gradesPanel = new GradesPanel();
        projectsPanel = new ProjectsPanel();
        attendancePanel = new AttendancePanel();
        feesPanel = new FeesPanel();
        messagesPanel = new MessagesPanel();
        notificationsPanel = new NotificationsPanel();
        
        // Add panels to the content panel with card layout
        contentPanel.add(homePanel, "Home");
        contentPanel.add(studentInfoPanel, "StudentInfo");
        contentPanel.add(gradesPanel, "Grades");
        contentPanel.add(projectsPanel, "Projects");
        contentPanel.add(attendancePanel, "Attendance");
        contentPanel.add(feesPanel, "Fees");
        contentPanel.add(messagesPanel, "Messages");
        contentPanel.add(notificationsPanel, "Notifications");
    }
}