package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Attendance;
import com.vku.model.Student;
import com.vku.model.Subject;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Panel for displaying and managing student attendance.
 */
public class AttendancePanel extends JPanel {
    
    public AttendancePanel() {
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
        JLabel titleLabel = new JLabel("Student Attendance");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        // Subject selection
        JLabel subjectLabel = new JLabel("Subject:");
        String[] subjectNames = DataManager.getInstance().getSubjects().stream()
                               .map(Subject::getName)
                               .toArray(String[]::new);
        
        JComboBox<String> subjectComboBox = new JComboBox<>(subjectNames);
        
        // Date selection
        JLabel dateLabel = new JLabel("Date:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JTextField dateField = new JTextField(dateFormat.format(new Date()), 10);
        
        selectionPanel.add(subjectLabel);
        selectionPanel.add(subjectComboBox);
        selectionPanel.add(dateLabel);
        selectionPanel.add(dateField);
        
        // Attendance table
        String[] columnNames = {"Student Name", "ID", "Present"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        JTable attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(30);
        attendanceTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        attendanceTable.setFont(UIConstants.BODY_FONT);
        
        // Set up check box column
        attendanceTable.getColumnModel().getColumn(2).setCellRenderer(new CheckBoxRenderer());
        attendanceTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        // Populate table with initial data
        if (subjectNames.length > 0) {
            updateAttendanceTable(tableModel, subjectNames[0]);
        }
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        
        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton saveButton = new JButton("Save Attendance");
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
            updateAttendanceTable(tableModel, selectedSubject);
        });
        
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Attendance saved successfully!", 
                "Save Attendance", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void updateAttendanceTable(DefaultTableModel model, String subjectName) {
        model.setRowCount(0);
        
        // Find the selected subject
        Subject selectedSubject = DataManager.getInstance().getSubjects().stream()
                                 .filter(s -> s.getName().equals(subjectName))
                                 .findFirst()
                                 .orElse(null);
        
        if (selectedSubject == null) {
            return;
        }
        
        // Find students for this subject
        for (Student student : DataManager.getInstance().getStudents()) {
            if (student.getSubjects().contains(selectedSubject)) {
                // Find attendance record for today
                boolean present = DataManager.getInstance().getAttendanceRecords().stream()
                                 .filter(a -> a.getStudent().equals(student) && a.getSubject().equals(selectedSubject))
                                 .findFirst()
                                 .map(Attendance::isPresent)
                                 .orElse(true); // Default to present
                
                model.addRow(new Object[]{
                    student.getName(),
                    student.getId(),
                    present
                });
            }
        }
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Attendance");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        
        // Subjects panel
        ModernPanel subjectsPanel = new ModernPanel(true);
        subjectsPanel.setLayout(new BorderLayout());
        subjectsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel subjectsTitle = new JLabel("My Subjects");
        subjectsTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // Create panel for subject cards
        JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        cardsPanel.setOpaque(false);
        
        // Add subject cards
        for (Subject subject : student.getSubjects()) {
            ModernPanel subjectCard = new ModernPanel(true);
            subjectCard.setLayout(new BorderLayout(10, 10));
            subjectCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel nameLabel = new JLabel(subject.getName());
            nameLabel.setFont(UIConstants.SUBTITLE_FONT);
            
            JLabel teacherLabel = new JLabel("Teacher: " + subject.getTeacher().getName());
            
            // Count sessions for today (dummy data)
            JLabel sessionsLabel = new JLabel("Today's Sessions: 1");
            
            JButton viewScheduleButton = new JButton("View Schedule");
            viewScheduleButton.setBackground(UIConstants.PRIMARY_COLOR);
            viewScheduleButton.setForeground(Color.WHITE);
            
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setOpaque(false);
            infoPanel.add(teacherLabel);
            infoPanel.add(sessionsLabel);
            
            subjectCard.add(nameLabel, BorderLayout.NORTH);
            subjectCard.add(infoPanel, BorderLayout.CENTER);
            subjectCard.add(viewScheduleButton, BorderLayout.SOUTH);
            
            cardsPanel.add(subjectCard);
            
            // Add action listener
            viewScheduleButton.addActionListener(e -> {
                showScheduleDialog(subject);
            });
        }
        
        JScrollPane cardsScrollPane = new JScrollPane(cardsPanel);
        cardsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        subjectsPanel.add(subjectsTitle, BorderLayout.NORTH);
        subjectsPanel.add(cardsScrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(subjectsPanel, BorderLayout.CENTER);
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void showScheduleDialog(Subject subject) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Schedule for " + subject.getName(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Schedule table
        String[] columnNames = {"No.", "Content", "Date", "Absence Status"};
        
        // Example schedule data
        Object[][] data = {
            {"1", "Introduction to " + subject.getName(), "2025-01-15", "Present"},
            {"2", "Basic Concepts", "2025-01-22", "Present"},
            {"3", "Advanced Topics", "2025-01-29", "Absent: Student 3 - S003"},
            {"4", "Practical Applications", "2025-02-05", "Present"},
            {"5", "Final Review", "2025-02-12", "Upcoming"}
        };
        
        JTable scheduleTable = new JTable(data, columnNames);
        scheduleTable.setRowHeight(30);
        scheduleTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        scheduleTable.setFont(UIConstants.BODY_FONT);
        
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
    
    // Custom renderer for checkbox column
    class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value);
            return this;
        }
    }
}