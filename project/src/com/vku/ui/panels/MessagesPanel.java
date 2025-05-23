package com.vku.ui.panels;

import com.vku.auth.AuthenticationManager;
import com.vku.data.DataManager;
import com.vku.model.Teacher;
import com.vku.ui.components.ModernPanel;
import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel for displaying and sending messages.
 */
public class MessagesPanel extends JPanel {
    
    public MessagesPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_COLOR);
        
        initView();
    }
    
    private void initView() {
        // Title
        JLabel titleLabel = new JLabel("Messages");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Teachers panel
        JPanel teachersPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        teachersPanel.setOpaque(false);
        teachersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Get all teachers
        List<Teacher> teachers = DataManager.getInstance().getTeachers();
        
        // Create teacher cards
        for (Teacher teacher : teachers) {
            ModernPanel teacherCard = new ModernPanel(true);
            teacherCard.setLayout(new BorderLayout());
            teacherCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // Teacher name
            JLabel nameLabel = new JLabel(teacher.getName());
            nameLabel.setFont(UIConstants.SUBTITLE_FONT);
            
            // Contact info
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            infoPanel.setOpaque(false);
            
            JLabel emailLabel = new JLabel("Email: " + teacher.getEmail());
            JLabel phoneLabel = new JLabel("Phone: 0123456789"); // Dummy data
            
            infoPanel.add(emailLabel);
            infoPanel.add(phoneLabel);
            
            // Action buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setOpaque(false);
            
            JButton emailButton = new JButton("Email");
            JButton messageButton = new JButton("Message");
            
            emailButton.setBackground(UIConstants.SECONDARY_COLOR);
            emailButton.setForeground(Color.WHITE);
            messageButton.setBackground(UIConstants.PRIMARY_COLOR);
            messageButton.setForeground(Color.WHITE);
            
            buttonPanel.add(emailButton);
            buttonPanel.add(messageButton);
            
            // Add components to card
            teacherCard.add(nameLabel, BorderLayout.NORTH);
            teacherCard.add(infoPanel, BorderLayout.CENTER);
            teacherCard.add(buttonPanel, BorderLayout.SOUTH);
            
            // Add card to panel
            teachersPanel.add(teacherCard);
            
            // Add action listeners
            emailButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, 
                    "Sending email to: " + teacher.getEmail(), 
                    "Send Email", JOptionPane.INFORMATION_MESSAGE);
            });
            
            messageButton.addActionListener(e -> {
                showMessageDialog(teacher);
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(teachersPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showMessageDialog(Teacher teacher) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Message to " + teacher.getName(), true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Message input
        JLabel messageLabel = new JLabel("Message:");
        JTextArea messageArea = new JTextArea();
        messageArea.setRows(10);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton sendButton = new JButton("Send");
        
        sendButton.setBackground(UIConstants.PRIMARY_COLOR);
        sendButton.setForeground(Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(sendButton);
        
        // Add components to panel
        contentPanel.add(messageLabel, BorderLayout.NORTH);
        contentPanel.add(messageScrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        
        // Add action listeners
        cancelButton.addActionListener(e -> dialog.dispose());
        
        sendButton.addActionListener(e -> {
            String message = messageArea.getText();
            if (message.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter a message.", 
                    "Empty Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(dialog, 
                "Message sent to " + teacher.getName(), 
                "Message Sent", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        dialog.setVisible(true);
    }
}