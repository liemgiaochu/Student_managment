package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Student;
import com.vku.ui.components.ModernButton;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for displaying and managing student information.
 */
public class StudentInfoPanel extends JPanel {
    
    public StudentInfoPanel() {
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
        JLabel titleLabel = new JLabel("Student Information");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Filters panel
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filtersPanel.setOpaque(false);
        filtersPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        // Major filter
        JLabel majorLabel = new JLabel("Major:");
        JComboBox<String> majorComboBox = new JComboBox<>(new String[]{"All", "Information Technology", "Business Administration", "Design"});
        
        // Class filter
        JLabel classLabel = new JLabel("Class:");
        JComboBox<String> classComboBox = new JComboBox<>(new String[]{"All", "K40C", "K41A", "K42B"});
        
        // Search field
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(15);
        
        filtersPanel.add(majorLabel);
        filtersPanel.add(majorComboBox);
        filtersPanel.add(classLabel);
        filtersPanel.add(classComboBox);
        filtersPanel.add(searchLabel);
        filtersPanel.add(searchField);
        
        // Students table
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Class", "Major", "Course", "Contact", "Actions"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only the actions column editable
                return column == 6;
            }
        };
        
        JTable studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(35);
        studentsTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        studentsTable.setFont(UIConstants.BODY_FONT);
        
        // Set custom renderer for the actions column
        studentsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        studentsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
        
        // Populate table with students
        List<Student> students = DataManager.getInstance().getStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getClassName(),
                student.getMajor(),
                student.getCourse(),
                student.getEmail(),
                "Actions"
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Export button
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exportPanel.setOpaque(false);
        exportPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton exportButton = new JButton("Export to PDF");
        exportButton.setBackground(UIConstants.SECONDARY_COLOR);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(UIConstants.BODY_FONT);
        
        exportPanel.add(exportButton);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(filtersPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(exportPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Add action listeners
        majorComboBox.addActionListener(e -> {
            filterStudents(tableModel, (String) majorComboBox.getSelectedItem(), 
                         (String) classComboBox.getSelectedItem(), searchField.getText());
        });
        
        classComboBox.addActionListener(e -> {
            filterStudents(tableModel, (String) majorComboBox.getSelectedItem(), 
                         (String) classComboBox.getSelectedItem(), searchField.getText());
        });
        
        searchField.addActionListener(e -> {
            filterStudents(tableModel, (String) majorComboBox.getSelectedItem(), 
                         (String) classComboBox.getSelectedItem(), searchField.getText());
        });
        
        exportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "PDF export functionality would be implemented using iText library.", 
                "Export to PDF", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void filterStudents(DefaultTableModel model, String major, String className, String search) {
        model.setRowCount(0);
        
        for (Student student : DataManager.getInstance().getStudents()) {
            boolean majorMatch = major.equals("All") || student.getMajor().equals(major);
            boolean classMatch = className.equals("All") || student.getClassName().equals(className);
            boolean searchMatch = search.isEmpty() || 
                                student.getName().toLowerCase().contains(search.toLowerCase()) ||
                                student.getId().toLowerCase().contains(search.toLowerCase());
            
            if (majorMatch && classMatch && searchMatch) {
                model.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getClassName(),
                    student.getMajor(),
                    student.getCourse(),
                    student.getEmail(),
                    "Actions"
                });
            }
        }
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Profile and chart
        JPanel leftPanel = new ModernPanel(true);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));
        
        // Profile picture placeholder
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setOpaque(false);
        profilePanel.setPreferredSize(new Dimension(200, 200));
        profilePanel.setMaximumSize(new Dimension(200, 200));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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
                g2d.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g2d.getFontMetrics();
                String text = student.getName().substring(0, 1).toUpperCase();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2 + textHeight / 4);
                g2d.dispose();
            }
        };
        profilePic.setPreferredSize(new Dimension(150, 150));
        profilePanel.add(profilePic, BorderLayout.CENTER);
        
        // Upload photo button
        JButton uploadButton = new JButton("Upload Photo");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setMaximumSize(new Dimension(150, 30));
        
        // Student info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel idLabel = new JLabel("ID: " + student.getId());
        JLabel classLabel = new JLabel("Class: " + student.getClassName());
        JLabel majorLabel = new JLabel("Major: " + student.getMajor());
        JLabel courseLabel = new JLabel("Course: " + student.getCourse());
        
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        majorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(idLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(classLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(majorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(courseLabel);
        
        // Discipline score chart (placeholder)
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw chart title
                g2d.setColor(Color.BLACK);
                g2d.setFont(UIConstants.SUBTITLE_FONT);
                g2d.drawString("Discipline Scores", 10, 20);
                
                // Draw bar chart
                int barWidth = 30;
                int spacing = 20;
                int startX = 30;
                int maxHeight = 100;
                
                // Example discipline scores
                int[] scores = {85, 90, 88};
                String[] semesters = {"Sem 1", "Sem 2", "Sem 3"};
                
                for (int i = 0; i < scores.length; i++) {
                    int barHeight = (int)(scores[i] * maxHeight / 100);
                    int x = startX + i * (barWidth + spacing);
                    int y = getHeight() - 40 - barHeight;
                    
                    // Draw bar
                    g2d.setColor(UIConstants.PRIMARY_COLOR);
                    g2d.fillRect(x, y, barWidth, barHeight);
                    
                    // Draw border
                    g2d.setColor(new Color(13, 148, 136));
                    g2d.drawRect(x, y, barWidth, barHeight);
                    
                    // Draw score
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(String.valueOf(scores[i]), x + 5, y - 5);
                    
                    // Draw semester label
                    g2d.drawString(semesters[i], x, getHeight() - 20);
                }
                
                g2d.dispose();
            }
        };
        chartPanel.setPreferredSize(new Dimension(200, 200));
        chartPanel.setMaximumSize(new Dimension(200, 200));
        chartPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to left panel
        leftPanel.add(profilePanel);
        leftPanel.add(uploadButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(infoPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(chartPanel);
        
        // Right panel - Student details
        JPanel rightPanel = new ModernPanel(true);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel detailsTitle = new JLabel("Student Details");
        detailsTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // Create a panel for details in a grid
        JPanel detailsGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsGrid.setOpaque(false);
        
        // Personal info section
        JLabel personalTitle = new JLabel("Personal Information");
        personalTitle.setFont(UIConstants.BODY_FONT.deriveFont(Font.BOLD));
        personalTitle.setForeground(UIConstants.PRIMARY_COLOR);
        
        detailsGrid.add(personalTitle);
        detailsGrid.add(new JLabel()); // Empty cell for alignment
        
        addDetailRow(detailsGrid, "Full Name:", student.getName());
        addDetailRow(detailsGrid, "Date of Birth:", student.getDateOfBirth() != null ? student.getDateOfBirth() : "Not set");
        addDetailRow(detailsGrid, "Gender:", student.getGender() != null ? student.getGender() : "Not set");
        
        // Family info section
        JLabel familyTitle = new JLabel("Family Information");
        familyTitle.setFont(UIConstants.BODY_FONT.deriveFont(Font.BOLD));
        familyTitle.setForeground(UIConstants.PRIMARY_COLOR);
        
        detailsGrid.add(familyTitle);
        detailsGrid.add(new JLabel()); // Empty cell for alignment
        
        addDetailRow(detailsGrid, "Parent's Name:", student.getParentName() != null ? student.getParentName() : "Not set");
        addDetailRow(detailsGrid, "Parent's Contact:", student.getParentContact() != null ? student.getParentContact() : "Not set");
        
        // Address & contact section
        JLabel addressTitle = new JLabel("Address & Contact");
        addressTitle.setFont(UIConstants.BODY_FONT.deriveFont(Font.BOLD));
        addressTitle.setForeground(UIConstants.PRIMARY_COLOR);
        
        detailsGrid.add(addressTitle);
        detailsGrid.add(new JLabel()); // Empty cell for alignment
        
        addDetailRow(detailsGrid, "Permanent Address:", student.getAddress() != null ? student.getAddress() : "Not set");
        addDetailRow(detailsGrid, "Phone:", student.getPhone() != null ? student.getPhone() : "Not set");
        addDetailRow(detailsGrid, "Email:", student.getEmail());
        
        // Add edit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton editButton = new ModernButton("Edit Information");
        buttonPanel.add(editButton);
        
        // Add components to right panel
        rightPanel.add(detailsTitle, BorderLayout.NORTH);
        rightPanel.add(detailsGrid, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
        
        // Add action listeners
        uploadButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Upload photo functionality would be implemented here.", 
                                         "Upload Photo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        editButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Edit information functionality would be implemented here.", 
                                         "Edit Information", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UIConstants.BODY_FONT);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(UIConstants.BODY_FONT);
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    // Custom renderer for the actions column
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton viewButton;
        private JButton editButton;
        private JButton deleteButton;
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            viewButton = new JButton("View");
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            
            viewButton.setFont(UIConstants.SMALL_FONT);
            editButton.setFont(UIConstants.SMALL_FONT);
            deleteButton.setFont(UIConstants.SMALL_FONT);
            
            add(viewButton);
            add(editButton);
            add(deleteButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Custom editor for the actions column
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton viewButton;
        private JButton editButton;
        private JButton deleteButton;
        
        public ButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            viewButton = new JButton("View");
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            
            viewButton.setFont(UIConstants.SMALL_FONT);
            editButton.setFont(UIConstants.SMALL_FONT);
            deleteButton.setFont(UIConstants.SMALL_FONT);
            
            panel.add(viewButton);
            panel.add(editButton);
            panel.add(deleteButton);
            
            viewButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();
                String studentId = (String) table.getValueAt(row, 0);
                
                JOptionPane.showMessageDialog(panel, 
                    "View details for student: " + studentId, 
                    "View Student", JOptionPane.INFORMATION_MESSAGE);
                
                fireEditingStopped();
            });
            
            editButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();
                String studentId = (String) table.getValueAt(row, 0);
                
                JOptionPane.showMessageDialog(panel, 
                    "Edit student: " + studentId, 
                    "Edit Student", JOptionPane.INFORMATION_MESSAGE);
                
                fireEditingStopped();
            });
            
            deleteButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();
                String studentId = (String) table.getValueAt(row, 0);
                
                int choice = JOptionPane.showConfirmDialog(panel, 
                    "Are you sure you want to delete student: " + studentId + "?", 
                    "Delete Student", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.removeRow(row);
                }
                
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
}