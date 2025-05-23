package com.vku.ui.components;

import com.vku.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom button for the sidebar navigation with icon and text.
 */
public class SidebarButton extends JButton {
    private boolean isHovered = false;
    private boolean isSelected = false;
    private String iconText;
    
    public SidebarButton(String text, String iconText) {
        super(text);
        this.iconText = iconText;
        initialize();
    }
    
    private void initialize() {
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        
        // Set preferred size and maximum size
        setPreferredSize(new Dimension(200, 40));
        setMaximumSize(new Dimension(200, 40));
        
        // Add mouse listeners for hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isSelected = true;
                repaint();
                
                // Deselect other sidebar buttons
                for (Component c : getParent().getComponents()) {
                    if (c instanceof SidebarButton && c != SidebarButton.this) {
                        ((SidebarButton) c).setSelected(false);
                    }
                }
            }
        });
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint background
        if (isSelected) {
            g2.setColor(UIConstants.SIDEBAR_SELECTED_COLOR);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw indicator on the left
            g2.setColor(UIConstants.ACCENT_COLOR);
            g2.fillRect(0, 0, 4, getHeight());
        } else if (isHovered) {
            g2.setColor(UIConstants.SIDEBAR_HOVER_COLOR);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw icon
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2.setColor(Color.WHITE);
        g2.drawString(iconText, 20, 25);
        
        // Draw text
        g2.setFont(getFont());
        g2.drawString(getText(), 50, 25);
        
        g2.dispose();
    }
}