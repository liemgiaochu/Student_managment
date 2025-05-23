package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Grade;
import com.vku.model.Student;
import com.vku.model.Subject;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Panel for displaying and managing student grades.
 */
public class GradesPanel extends JPanel {
    
    public GradesPanel() {
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
        JLabel titleLabel = new JLabel("Student Grades");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Subject selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        JLabel subjectLabel = new JLabel("Select Subject:");
        
        // Get subjects for the teacher
        String[] subjectNames = DataManager.getInstance().getSubjects().stream()
                               .map(Subject::getName)
                               .toArray(String[]::new);
        
        JComboBox<String> subjectComboBox = new JComboBox<>(subjectNames);
        
        selectionPanel.add(subjectLabel);
        selectionPanel.add(subjectComboBox);
        
        // Grades table
        String[] columnNames = {"Student Name", "ID", "Assignment (20%)", "Midterm (20%)", 
                             "Attendance (10%)", "Final (50%)", "Average", "Rank"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make score columns editable
                return column >= 2 && column <= 5;
            }
        };
        
        JTable gradesTable = new JTable(tableModel);
        gradesTable.setRowHeight(30);
        gradesTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        gradesTable.setFont(UIConstants.BODY_FONT);
        
        // Set custom renderer for the rank column
        gradesTable.getColumnModel().getColumn(7).setCellRenderer(new GradeRankRenderer());
        
        // Populate table with initial subject
        if (subjectNames.length > 0) {
            updateGradesTable(tableModel, subjectNames[0]);
        }
        
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        
        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton saveButton = new JButton("Save Grades");
        saveButton.setBackground(UIConstants.PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(UIConstants.BODY_FONT);
        
        buttonPanel.add(saveButton);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(selectionPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Add action listeners
        subjectComboBox.addActionListener(e -> {
            String selectedSubject = (String) subjectComboBox.getSelectedItem();
            updateGradesTable(tableModel, selectedSubject);
        });
        
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Grades saved successfully!", 
                "Save Grades", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void updateGradesTable(DefaultTableModel model, String subjectName) {
        model.setRowCount(0);
        
        // Find the selected subject
        Subject selectedSubject = DataManager.getInstance().getSubjects().stream()
                                 .filter(s -> s.getName().equals(subjectName))
                                 .findFirst()
                                 .orElse(null);
        
        if (selectedSubject == null) {
            return;
        }
        
        // Find students for this subject and their grades
        for (Student student : DataManager.getInstance().getStudents()) {
            if (student.getSubjects().contains(selectedSubject)) {
                // Find grade for this subject
                Grade grade = student.getGrades().stream()
                             .filter(g -> g.getSubject().equals(selectedSubject))
                             .findFirst()
                             .orElse(null);
                
                if (grade != null) {
                    model.addRow(new Object[]{
                        student.getName(),
                        student.getId(),
                        grade.getAssignmentScore(),
                        grade.getMidtermScore(),
                        grade.getAttendanceScore(),
                        grade.getFinalScore(),
                        grade.calculateAverage(),
                        grade.getLetterGrade()
                    });
                } else {
                    // If no grade yet, add student with empty scores
                    model.addRow(new Object[]{
                        student.getName(),
                        student.getId(),
                        0.0,
                        0.0,
                        0.0,
                        0.0,
                        0.0,
                        "F"
                    });
                }
            }
        }
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Grades");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        
        // Top section - Overall grades table
        ModernPanel overallPanel = new ModernPanel(true);
        overallPanel.setLayout(new BorderLayout());
        overallPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel overallTitle = new JLabel("Overall Academic Performance");
        overallTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        String[] overallColumns = {"Semester", "Registered Credits", "New Credits", "GPA (4.0)", 
                                 "GPA (10.0)", "Rank", "Cumulative GPA (4.0)", 
                                 "Cumulative GPA (10.0)", "Cumulative Credits"};
        
        Object[][] overallData = {
            {"1", "15", "15", "3.5", "8.2", "B", "3.5", "8.2", "15"},
            {"2", "18", "18", "3.7", "8.8", "A", "3.6", "8.5", "33"},
            {"Current", "15", "0", "-", "-", "-", "3.6", "8.5", "33"}
        };
        
        JTable overallTable = new JTable(overallData, overallColumns);
        overallTable.setRowHeight(30);
        overallTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        overallTable.setFont(UIConstants.BODY_FONT);
        
        JScrollPane overallScrollPane = new JScrollPane(overallTable);
        overallScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        overallPanel.add(overallTitle, BorderLayout.NORTH);
        overallPanel.add(overallScrollPane, BorderLayout.CENTER);
        
        // Bottom section - Detailed grades by semester
        ModernPanel detailedPanel = new ModernPanel(true);
        detailedPanel.setLayout(new BorderLayout());
        detailedPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel detailedTitle = new JLabel("Detailed Grades by Subject");
        detailedTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        String[] detailedColumns = {"Subject Name", "Credits", "Attempt", "Attendance", 
                                  "Assignment", "Midterm", "Final", "Score (10.0)", "Rank"};
        
        DefaultTableModel detailedModel = new DefaultTableModel(detailedColumns, 0);
        
        // Populate with student's subjects and grades
        for (Grade grade : student.getGrades()) {
            Subject subject = grade.getSubject();
            
            detailedModel.addRow(new Object[]{
                subject.getName(),
                subject.getCredits(),
                "1",
                grade.getAttendanceScore(),
                grade.getAssignmentScore(),
                grade.getMidtermScore(),
                grade.getFinalScore(),
                grade.calculateAverage(),
                grade.getLetterGrade()
            });
        }
        
        JTable detailedTable = new JTable(detailedModel);
        detailedTable.setRowHeight(30);
        detailedTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        detailedTable.setFont(UIConstants.BODY_FONT);
        
        // Set custom renderer for the rank column
        detailedTable.getColumnModel().getColumn(8).setCellRenderer(new GradeRankRenderer());
        
        JScrollPane detailedScrollPane = new JScrollPane(detailedTable);
        detailedScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        detailedPanel.add(detailedTitle, BorderLayout.NORTH);
        detailedPanel.add(detailedScrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(overallPanel, BorderLayout.NORTH);
        centerPanel.add(detailedPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // Custom renderer for the grade rank column
    class GradeRankRenderer extends JLabel implements TableCellRenderer {
        public GradeRankRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(UIConstants.BODY_FONT.deriveFont(Font.BOLD));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String rank = value.toString();
            
            switch (rank) {
                case "A":
                    setBackground(new Color(34, 197, 94, 50)); // Light green
                    setForeground(new Color(21, 128, 61)); // Dark green
                    break;
                case "B":
                    setBackground(new Color(59, 130, 246, 50)); // Light blue
                    setForeground(new Color(29, 78, 216)); // Dark blue
                    break;
                case "C":
                    setBackground(new Color(249, 115, 22, 50)); // Light orange
                    setForeground(new Color(194, 65, 12)); // Dark orange
                    break;
                case "D":
                    setBackground(new Color(234, 179, 8, 50)); // Light yellow
                    setForeground(new Color(161, 98, 7)); // Dark yellow
                    break;
                default: // F
                    setBackground(new Color(239, 68, 68, 50)); // Light red
                    setForeground(new Color(185, 28, 28)); // Dark red
                    break;
            }
            
            setText(rank);
            return this;
        }
    }
}