package com.vku.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A modern panel with subtle background and optional shadow.
 */
public class ModernPanel extends JPanel {
    private boolean drawShadow = false;
    
    public ModernPanel() {
        this(false);
    }
    
    public ModernPanel(boolean drawShadow) {
        this.drawShadow = drawShadow;
        setBackground(Color.WHITE);
        setOpaque(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (drawShadow) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Draw shadow
            g2d.setColor(new Color(0, 0, 0, 30));
            for (int i = 0; i < 5; i++) {
                g2d.drawRoundRect(i, i, getWidth() - i * 2 - 1, getHeight() - i * 2 - 1, 15, 15);
            }
            
            g2d.dispose();
        }
    }
}