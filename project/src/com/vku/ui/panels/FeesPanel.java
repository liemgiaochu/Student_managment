package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Fee;
import com.vku.model.Student;
import com.vku.model.Subject;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel for displaying and managing student fees.
 */
public class FeesPanel extends JPanel {
    
    public FeesPanel() {
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
        JLabel titleLabel = new JLabel("Student Fees");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Fees table
        String[] columnNames = {"Student Name", "ID", "Class", "Fees Owed (VND)", "Paid"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        JTable feesTable = new JTable(tableModel);
        feesTable.setRowHeight(30);
        feesTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        feesTable.setFont(UIConstants.BODY_FONT);
        
        // Set up check box column
        feesTable.getColumnModel().getColumn(4).setCellRenderer(new CheckBoxRenderer());
        feesTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        // Populate table with fees
        for (Fee fee : DataManager.getInstance().getFees()) {
            Student student = fee.getStudent();
            
            tableModel.addRow(new Object[]{
                student.getName(),
                student.getId(),
                student.getClassName(),
                String.format("%.0f", fee.getAmount()),
                fee.isPaid()
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(feesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton saveButton = new JButton("Save Payment Status");
        saveButton.setBackground(UIConstants.PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(UIConstants.BODY_FONT);
        
        buttonPanel.add(saveButton);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Payment status saved successfully!", 
                "Save Payment Status", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Find student's fee
        Fee studentFee = DataManager.getInstance().getFees().stream()
                       .filter(f -> f.getStudent().equals(student))
                       .findFirst()
                       .orElse(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Fees");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        
        // Content panel with fees table and QR code
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        
        // Fees table panel
        ModernPanel feesPanel = new ModernPanel(true);
        feesPanel.setLayout(new BorderLayout());
        feesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel feesTitle = new JLabel("Tuition Fees");
        feesTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // Fees table
        String[] columnNames = {"Subject", "Credits", "Fee (VND)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        // Populate table with subjects and fees
        for (Subject subject : student.getSubjects()) {
            double subjectFee = subject.getCredits() * 1000000; // 1M VND per credit
            
            tableModel.addRow(new Object[]{
                subject.getName(),
                subject.getCredits(),
                String.format("%.0f", subjectFee)
            });
        }
        
        // Add total row
        double totalFee = studentFee != null ? studentFee.getAmount() : 0;
        tableModel.addRow(new Object[]{
            "Total",
            "",
            String.format("%.0f", totalFee)
        });
        
        JTable feesTable = new JTable(tableModel);
        feesTable.setRowHeight(30);
        feesTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        feesTable.setFont(UIConstants.BODY_FONT);
        
        JScrollPane scrollPane = new JScrollPane(feesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Payment status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        
        JLabel statusLabel = new JLabel("Payment Status:");
        JLabel statusValue = new JLabel(studentFee != null && studentFee.isPaid() ? 
                                      "Paid" : "Unpaid");
        statusValue.setForeground(studentFee != null && studentFee.isPaid() ? 
                                UIConstants.SUCCESS_COLOR : UIConstants.ERROR_COLOR);
        statusValue.setFont(UIConstants.BODY_FONT.deriveFont(Font.BOLD));
        
        statusPanel.add(statusLabel);
        statusPanel.add(statusValue);
        
        feesPanel.add(feesTitle, BorderLayout.NORTH);
        feesPanel.add(scrollPane, BorderLayout.CENTER);
        feesPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // QR code panel
        ModernPanel qrPanel = new ModernPanel(true);
        qrPanel.setLayout(new BorderLayout());
        qrPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel qrTitle = new JLabel("Payment QR Code");
        qrTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // QR code placeholder
        JPanel qrPlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw QR code placeholder
                g2d.setColor(Color.WHITE);
                g2d.fillRect(50, 50, 200, 200);
                
                g2d.setColor(Color.BLACK);
                g2d.drawRect(50, 50, 200, 200);
                
                // Draw QR pattern corners
                g2d.fillRect(70, 70, 40, 40);
                g2d.fillRect(190, 70, 40, 40);
                g2d.fillRect(70, 190, 40, 40);
                
                g2d.drawRect(90, 90, 120, 120);
                g2d.drawString("QR Code for Payment", 100, 150);
                
                g2d.dispose();
            }
        };
        qrPlaceholder.setPreferredSize(new Dimension(300, 300));
        
        // Payment instructions
        JLabel instructionLabel = new JLabel("<html>Scan the QR code with your banking app to pay the tuition fee.<br>" + 
                                          "Payment ID: " + student.getId() + "<br>" + 
                                          "Amount: " + (studentFee != null ? String.format("%.0f VND", studentFee.getAmount()) : "0 VND") + 
                                          "</html>");
        
        qrPanel.add(qrTitle, BorderLayout.NORTH);
        qrPanel.add(qrPlaceholder, BorderLayout.CENTER);
        qrPanel.add(instructionLabel, BorderLayout.SOUTH);
        
        // Add panels to content panel
        contentPanel.add(feesPanel);
        contentPanel.add(qrPanel);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
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