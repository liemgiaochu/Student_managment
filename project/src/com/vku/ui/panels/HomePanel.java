package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Student;
import com.vku.model.Subject;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Home panel that displays different content based on user role.
 */
public class HomePanel extends JPanel {
    
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_COLOR);
        
        if (AuthenticationManager.isTeacher()) {
            initTeacherView();
        } else if (AuthenticationManager.isStudent()) {
            initStudentView();
        }
    }
    
    private void initTeacherView() {
        // Top section - Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Get data for statistics
        int totalStudents = DataManager.getInstance().getStudents().size();
        int pendingProjects = (int) DataManager.getInstance().getProjects().stream()
                                .filter(p -> p.getProgress() < 100).count();
        
        double totalFees = DataManager.getInstance().getFees().stream()
                             .filter(f -> !f.isPaid())
                             .mapToDouble(f -> f.getAmount())
                             .sum();
        
        double averageGPA = DataManager.getInstance().getStudents().stream()
                             .mapToDouble(s -> s.calculateGPA())
                             .average()
                             .orElse(0.0);
        
        // Create statistic cards
        statsPanel.add(createStatCard("Total Students", String.valueOf(totalStudents), UIConstants.PRIMARY_COLOR));
        statsPanel.add(createStatCard("Pending Projects", String.valueOf(pendingProjects), UIConstants.SECONDARY_COLOR));
        statsPanel.add(createStatCard("Unpaid Fees", String.format("%.0f VND", totalFees), UIConstants.WARNING_COLOR));
        statsPanel.add(createStatCard("Average GPA", String.format("%.1f", averageGPA), UIConstants.ACCENT_COLOR));
        
        // Middle section - Charts
        JPanel chartPanel = new ModernPanel(true);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setLayout(new BorderLayout());
        
        JLabel chartTitle = new JLabel("Student Distribution by Major");
        chartTitle.setFont(UIConstants.SUBTITLE_FONT);
        chartPanel.add(chartTitle, BorderLayout.NORTH);
        
        // Count students by major
        Map<String, Integer> majorCounts = new HashMap<>();
        for (Student student : DataManager.getInstance().getStudents()) {
            String major = student.getMajor();
            majorCounts.put(major, majorCounts.getOrDefault(major, 0) + 1);
        }
        
        // Create simple pie chart visualization
        JPanel pieChart = createSimplePieChart(majorCounts);
        chartPanel.add(pieChart, BorderLayout.CENTER);
        
        // Bottom section - Quick access buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonsPanel.setOpaque(false);
        
        JButton viewStudentsBtn = new JButton("View Students");
        JButton checkGradesBtn = new JButton("Check Grades");
        
        stylizeButton(viewStudentsBtn);
        stylizeButton(checkGradesBtn);
        
        buttonsPanel.add(viewStudentsBtn);
        buttonsPanel.add(checkGradesBtn);
        
        // Add action listeners
        viewStudentsBtn.addActionListener(e -> {
            // Switch to Student Info panel
            Container mainPanel = getParent();
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, "StudentInfo");
        });
        
        checkGradesBtn.addActionListener(e -> {
            // Switch to Grades panel
            Container mainPanel = getParent();
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, "Grades");
        });
        
        // Add all sections to the main panel
        add(statsPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        centerPanel.add(chartPanel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top section - Welcome message and personal info
        JPanel topPanel = new ModernPanel(true);
        topPanel.setLayout(new BorderLayout(15, 15));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Chào " + student.getName() + ", hôm nay bạn có gì?");
        welcomeLabel.setFont(UIConstants.TITLE_FONT);
        topPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Personal info panel
        JPanel personalInfoPanel = new JPanel(new BorderLayout());
        personalInfoPanel.setOpaque(false);
        
        // Left side - profile picture placeholder
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setOpaque(false);
        profilePanel.setPreferredSize(new Dimension(100, 100));
        
        // Create a circular profile pic placeholder
        JPanel profilePic = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(UIConstants.SECONDARY_COLOR);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                FontMetrics fm = g2d.getFontMetrics();
                String text = student.getName().substring(0, 1).toUpperCase();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2 + textHeight / 4);
                g2d.dispose();
            }
        };
        profilePic.setPreferredSize(new Dimension(80, 80));
        profilePanel.add(profilePic, BorderLayout.CENTER);
        
        // Right side - student details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel("Student ID: " + student.getId());
        JLabel classLabel = new JLabel("Class: " + student.getClassName());
        JLabel majorLabel = new JLabel("Major: " + student.getMajor());
        JLabel courseLabel = new JLabel("Course: " + student.getCourse());
        
        detailsPanel.add(idLabel);
        detailsPanel.add(classLabel);
        detailsPanel.add(majorLabel);
        detailsPanel.add(courseLabel);
        
        personalInfoPanel.add(profilePanel, BorderLayout.WEST);
        personalInfoPanel.add(detailsPanel, BorderLayout.CENTER);
        
        topPanel.add(personalInfoPanel, BorderLayout.CENTER);
        
        // Middle section - Study overview widgets
        JPanel middlePanel = new JPanel(new GridLayout(1, 4, 15, 0));
        middlePanel.setOpaque(false);
        
        double gpa = student.calculateGPA();
        int completedCredits = student.getTotalCredits();
        int totalRequiredCredits = 120; // Assuming 120 credits required for graduation
        int currentSubjectsCount = student.getSubjects().size();
        
        // Get project progress if student has any
        int projectProgress = 0;
        if (!DataManager.getInstance().getProjects().isEmpty()) {
            projectProgress = DataManager.getInstance().getProjects().stream()
                .filter(p -> p.getStudents().contains(student))
                .findFirst()
                .map(p -> p.getProgress())
                .orElse(0);
        }
        
        middlePanel.add(createStatCard("Current GPA", String.format("%.1f", gpa), UIConstants.PRIMARY_COLOR));
        middlePanel.add(createStatCard("Credits", completedCredits + "/" + totalRequiredCredits, UIConstants.SECONDARY_COLOR));
        middlePanel.add(createStatCard("Current Subjects", String.valueOf(currentSubjectsCount), UIConstants.ACCENT_COLOR));
        middlePanel.add(createStatCard("Project Progress", projectProgress + "%", UIConstants.SUCCESS_COLOR));
        
        // Bottom section - Today's schedule
        JPanel bottomPanel = new ModernPanel(true);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel scheduleTitle = new JLabel("Today's Schedule");
        scheduleTitle.setFont(UIConstants.SUBTITLE_FONT);
        bottomPanel.add(scheduleTitle, BorderLayout.NORTH);
        
        // Create a table for schedule
        String[] columnNames = {"Subject", "Time", "Room"};
        
        // Example schedule data - in a real app, this would come from a schedule database
        Object[][] data = {
            {"Java Programming", "8:00-10:00", "A101"},
            {"Web Development", "13:00-15:00", "B202"}
        };
        
        JTable scheduleTable = new JTable(data, columnNames);
        scheduleTable.setRowHeight(30);
        scheduleTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        scheduleTable.setFont(UIConstants.BODY_FONT);
        scheduleTable.setShowGrid(false);
        scheduleTable.setIntercellSpacing(new Dimension(0, 0));
        
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add all sections to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new ModernPanel(true);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.SMALL_FONT);
        titleLabel.setForeground(Color.DARK_GRAY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIConstants.TITLE_FONT);
        valueLabel.setForeground(color);
        
        // Create a colored indicator at the top
        JPanel indicator = new JPanel();
        indicator.setBackground(color);
        indicator.setPreferredSize(new Dimension(card.getWidth(), 5));
        
        card.add(indicator, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(valueLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createSimplePieChart(Map<String, Integer> data) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int total = data.values().stream().mapToInt(Integer::intValue).sum();
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(centerX, centerY) - 40;
                
                // Define colors for majors
                Color[] colors = {
                    UIConstants.PRIMARY_COLOR,
                    UIConstants.SECONDARY_COLOR,
                    UIConstants.ACCENT_COLOR,
                    UIConstants.SUCCESS_COLOR
                };
                
                // Draw pie slices
                int startAngle = 0;
                int i = 0;
                
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    int arcAngle = (int) Math.round(360.0 * entry.getValue() / total);
                    Color color = colors[i % colors.length];
                    g2d.setColor(color);
                    g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, startAngle, arcAngle);
                    
                    // Draw legend
                    int legendX = 20;
                    int legendY = 20 + (i * 30);
                    
                    g2d.fillRect(legendX, legendY, 15, 15);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(entry.getKey() + ": " + entry.getValue() + " (" + 
                                  String.format("%.1f", 100.0 * entry.getValue() / total) + "%)", 
                                  legendX + 25, legendY + 12);
                    
                    startAngle += arcAngle;
                    i++;
                }
                
                g2d.dispose();
            }
        };
    }
    
    private void stylizeButton(JButton button) {
        button.setBackground(UIConstants.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(UIConstants.BODY_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(13, 148, 136)); // Darker teal
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.PRIMARY_COLOR);
            }
        });
    }
}