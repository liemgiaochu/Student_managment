package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Project;
import com.vku.model.Student;
import com.vku.ui.components.ModernButton;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel for displaying and managing student projects.
 */
public class ProjectsPanel extends JPanel {
    
    public ProjectsPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_COLOR);
        
        if (AuthenticationManager.isTeacher()) {
            initTeacherView();
        } else if (AuthenticationManager.isStudent()) {
            initStudentView();
        }
    }
    
    private void initTeacherView() {
        // Title
        JLabel titleLabel = new JLabel("Student Projects");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Projects panel
        JPanel projectsPanel = new JPanel();
        projectsPanel.setLayout(new BoxLayout(projectsPanel, BoxLayout.Y_AXIS));
        projectsPanel.setOpaque(false);
        projectsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Add project groups
        List<Project> projects = DataManager.getInstance().getProjects();
        
        for (Project project : projects) {
            ModernPanel projectPanel = new ModernPanel(true);
            projectPanel.setLayout(new BorderLayout());
            projectPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            projectPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            
            // Header panel with project name and buttons
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            
            JLabel projectNameLabel = new JLabel(project.getName() + " (ID: " + project.getId() + ")");
            projectNameLabel.setFont(UIConstants.SUBTITLE_FONT);
            
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonsPanel.setOpaque(false);
            
            JButton approveButton = new JButton("Approve Topic");
            JButton gradeButton = new JButton("Enter Grade");
            
            approveButton.setBackground(UIConstants.SUCCESS_COLOR);
            approveButton.setForeground(Color.WHITE);
            gradeButton.setBackground(UIConstants.PRIMARY_COLOR);
            gradeButton.setForeground(Color.WHITE);
            
            buttonsPanel.add(approveButton);
            buttonsPanel.add(gradeButton);
            
            headerPanel.add(projectNameLabel, BorderLayout.WEST);
            headerPanel.add(buttonsPanel, BorderLayout.EAST);
            
            // Content panel with project details
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setOpaque(false);
            
            JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
            detailsPanel.setOpaque(false);
            
            // Supervisor
            JLabel supervisorLabel = new JLabel("Supervisor:");
            JLabel supervisorValue = new JLabel(project.getSupervisor().getName());
            
            // Deadline
            JLabel deadlineLabel = new JLabel("Deadline:");
            JLabel deadlineValue = new JLabel(project.getDeadline());
            
            // Progress
            JLabel progressLabel = new JLabel("Progress:");
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(project.getProgress());
            progressBar.setStringPainted(true);
            
            // Grade
            JLabel gradeLabel = new JLabel("Grade:");
            JTextField gradeField = new JTextField(String.valueOf(project.getGrade()));
            gradeField.setPreferredSize(new Dimension(50, 25));
            
            detailsPanel.add(supervisorLabel);
            detailsPanel.add(supervisorValue);
            detailsPanel.add(deadlineLabel);
            detailsPanel.add(deadlineValue);
            detailsPanel.add(progressLabel);
            detailsPanel.add(progressBar);
            detailsPanel.add(gradeLabel);
            detailsPanel.add(gradeField);
            
            // Students panel
            JPanel studentsPanel = new JPanel();
            studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
            studentsPanel.setOpaque(false);
            studentsPanel.setBorder(BorderFactory.createTitledBorder("Group Members"));
            
            for (Student student : project.getStudents()) {
                JLabel studentLabel = new JLabel(student.getName() + " (ID: " + student.getId() + ")");
                studentsPanel.add(studentLabel);
            }
            
            contentPanel.add(detailsPanel, BorderLayout.CENTER);
            contentPanel.add(studentsPanel, BorderLayout.EAST);
            
            // Add panels to project panel
            projectPanel.add(headerPanel, BorderLayout.NORTH);
            projectPanel.add(contentPanel, BorderLayout.CENTER);
            
            // Add project panel to projects panel
            projectsPanel.add(projectPanel);
            projectsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            
            // Add action listeners
            approveButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, 
                    "Topic approved for project: " + project.getName(), 
                    "Approve Topic", JOptionPane.INFORMATION_MESSAGE);
            });
            
            gradeButton.addActionListener(e -> {
                try {
                    double grade = Double.parseDouble(gradeField.getText());
                    project.setGrade(grade);
                    JOptionPane.showMessageDialog(this, 
                        "Grade updated for project: " + project.getName(), 
                        "Update Grade", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a valid grade", 
                        "Invalid Grade", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(projectsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Find student's project
        Project studentProject = DataManager.getInstance().getProjects().stream()
                                .filter(p -> p.getStudents().contains(student))
                                .findFirst()
                                .orElse(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Project");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        
        if (studentProject != null) {
            // Upload panel
            ModernPanel uploadPanel = new ModernPanel(true);
            uploadPanel.setLayout(new BorderLayout());
            uploadPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel uploadTitle = new JLabel("Upload Project Files");
            uploadTitle.setFont(UIConstants.SUBTITLE_FONT);
            
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
            buttonsPanel.setOpaque(false);
            
            ModernButton reportButton = new ModernButton("Upload Report (DOC)");
            ModernButton projectButton = new ModernButton("Upload Project (ZIP)");
            
            buttonsPanel.add(reportButton);
            buttonsPanel.add(projectButton);
            
            uploadPanel.add(uploadTitle, BorderLayout.NORTH);
            uploadPanel.add(buttonsPanel, BorderLayout.CENTER);
            
            // Info panel
            ModernPanel infoPanel = new ModernPanel(true);
            infoPanel.setLayout(new BorderLayout());
            infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel infoTitle = new JLabel("Project Information");
            infoTitle.setFont(UIConstants.SUBTITLE_FONT);
            
            JPanel infoGridPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            infoGridPanel.setOpaque(false);
            
            // Project name
            JLabel nameLabel = new JLabel("Project Name:");
            JLabel nameValue = new JLabel(studentProject.getName());
            
            // Supervisor
            JLabel supervisorLabel = new JLabel("Supervisor:");
            JLabel supervisorValue = new JLabel(studentProject.getSupervisor().getName());
            
            // Supervisor phone (dummy data)
            JLabel phoneLabel = new JLabel("Supervisor Phone:");
            JLabel phoneValue = new JLabel("0123456789");
            
            // Deadline
            JLabel deadlineLabel = new JLabel("Submission Deadline:");
            JLabel deadlineValue = new JLabel(studentProject.getDeadline());
            
            // Progress
            JLabel progressLabel = new JLabel("Progress:");
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(studentProject.getProgress());
            progressBar.setStringPainted(true);
            progressBar.setPreferredSize(new Dimension(200, 20));
            
            infoGridPanel.add(nameLabel);
            infoGridPanel.add(nameValue);
            infoGridPanel.add(supervisorLabel);
            infoGridPanel.add(supervisorValue);
            infoGridPanel.add(phoneLabel);
            infoGridPanel.add(phoneValue);
            infoGridPanel.add(deadlineLabel);
            infoGridPanel.add(deadlineValue);
            infoGridPanel.add(progressLabel);
            infoGridPanel.add(progressBar);
            
            // Group members panel
            JPanel membersPanel = new JPanel();
            membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
            membersPanel.setOpaque(false);
            membersPanel.setBorder(BorderFactory.createTitledBorder("Group Members"));
            
            for (Student groupMember : studentProject.getStudents()) {
                JLabel memberLabel = new JLabel(groupMember.getName() + " (ID: " + groupMember.getId() + ")");
                memberLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                membersPanel.add(memberLabel);
            }
            
            JPanel infoContentPanel = new JPanel(new BorderLayout(20, 0));
            infoContentPanel.setOpaque(false);
            infoContentPanel.add(infoGridPanel, BorderLayout.CENTER);
            infoContentPanel.add(membersPanel, BorderLayout.EAST);
            
            infoPanel.add(infoTitle, BorderLayout.NORTH);
            infoPanel.add(infoContentPanel, BorderLayout.CENTER);
            
            // Add panels to main panel
            mainPanel.add(titleLabel, BorderLayout.NORTH);
            
            JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
            centerPanel.setOpaque(false);
            centerPanel.add(uploadPanel, BorderLayout.NORTH);
            centerPanel.add(infoPanel, BorderLayout.CENTER);
            
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            
            // Add action listeners
            reportButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, 
                    "Report upload functionality would be implemented here.", 
                    "Upload Report", JOptionPane.INFORMATION_MESSAGE);
            });
            
            projectButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, 
                    "Project upload functionality would be implemented here.", 
                    "Upload Project", JOptionPane.INFORMATION_MESSAGE);
            });
        } else {
            // No project assigned
            JPanel noProjectPanel = new ModernPanel(true);
            noProjectPanel.setLayout(new BorderLayout());
            noProjectPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel noProjectLabel = new JLabel("You are not assigned to any project yet.");
            noProjectLabel.setFont(UIConstants.SUBTITLE_FONT);
            noProjectLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            noProjectPanel.add(noProjectLabel, BorderLayout.CENTER);
            
            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(noProjectPanel, BorderLayout.CENTER);
        }
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }
}