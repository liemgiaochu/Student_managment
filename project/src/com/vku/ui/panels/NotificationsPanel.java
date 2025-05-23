package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Notification;
import com.vku.model.Student;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Panel for displaying and creating notifications.
 */
public class NotificationsPanel extends JPanel {
    
    public NotificationsPanel() {
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
        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Compose notification panel
        ModernPanel composePanel = new ModernPanel(true);
        composePanel.setLayout(new BorderLayout());
        composePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel composeTitle = new JLabel("Compose Announcement");
        composeTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Recipient selection
        JLabel recipientLabel = new JLabel("Recipient:");
        String[] recipientOptions = {"@All", "K40C", "K41A", "K42B", "student1@vku.vn", "student2@vku.vn"};
        JComboBox<String> recipientComboBox = new JComboBox<>(recipientOptions);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(recipientLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        formPanel.add(recipientComboBox, gbc);
        
        // Message
        JLabel messageLabel = new JLabel("Message:");
        JTextArea messageArea = new JTextArea();
        messageArea.setRows(5);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(messageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(messageScrollPane, gbc);
        
        // Send button
        JButton sendButton = new JButton("Send Announcement");
        sendButton.setBackground(UIConstants.PRIMARY_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(UIConstants.BODY_FONT);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(sendButton, gbc);
        
        composePanel.add(composeTitle, BorderLayout.NORTH);
        composePanel.add(formPanel, BorderLayout.CENTER);
        
        // Notifications list
        ModernPanel notificationsPanel = new ModernPanel(true);
        notificationsPanel.setLayout(new BorderLayout());
        notificationsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel notificationsTitle = new JLabel("Sent Announcements");
        notificationsTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // Notifications table
        String[] columnNames = {"Date", "Recipient", "Message"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        // Add notifications
        List<Notification> notifications = DataManager.getInstance().getNotifications();
        for (Notification notification : notifications) {
            tableModel.addRow(new Object[]{
                dateFormat.format(notification.getDate()),
                "@All", // Dummy recipient
                notification.getMessage()
            });
        }
        
        JTable notificationsTable = new JTable(tableModel);
        notificationsTable.setRowHeight(30);
        notificationsTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        notificationsTable.setFont(UIConstants.BODY_FONT);
        
        JScrollPane tableScrollPane = new JScrollPane(notificationsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        notificationsPanel.add(notificationsTitle, BorderLayout.NORTH);
        notificationsPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Add panels to main panel
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        centerPanel.add(composePanel, BorderLayout.NORTH);
        centerPanel.add(notificationsPanel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Add action listeners
        sendButton.addActionListener(e -> {
            String recipient = (String) recipientComboBox.getSelectedItem();
            String message = messageArea.getText();
            
            if (message.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a message.", 
                    "Empty Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Add new notification to table
            tableModel.insertRow(0, new Object[]{
                dateFormat.format(new Date()),
                recipient,
                message
            });
            
            JOptionPane.showMessageDialog(this, 
                "Announcement sent successfully!", 
                "Send Announcement", JOptionPane.INFORMATION_MESSAGE);
            
            messageArea.setText("");
        });
    }
    
    private void initStudentView() {
        Student student = (Student) AuthenticationManager.getCurrentUser();
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Notifications");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        
        // Notifications panel
        ModernPanel notificationsPanel = new ModernPanel(true);
        notificationsPanel.setLayout(new BorderLayout());
        notificationsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Notifications table
        String[] columnNames = {"Date", "Sender", "Message"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        // Add notifications
        List<Notification> notifications = DataManager.getInstance().getNotifications();
        for (Notification notification : notifications) {
            tableModel.addRow(new Object[]{
                dateFormat.format(notification.getDate()),
                notification.getSender(),
                notification.getMessage()
            });
        }
        
        JTable notificationsTable = new JTable(tableModel);
        notificationsTable.setRowHeight(30);
        notificationsTable.getTableHeader().setFont(UIConstants.BODY_FONT);
        notificationsTable.setFont(UIConstants.BODY_FONT);
        
        // Set column widths
        notificationsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        notificationsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        notificationsTable.getColumnModel().getColumn(2).setPreferredWidth(500);
        
        JScrollPane tableScrollPane = new JScrollPane(notificationsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        notificationsPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(notificationsPanel, BorderLayout.CENTER);
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }
}