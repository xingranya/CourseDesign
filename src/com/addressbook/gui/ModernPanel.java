package com.addressbook.gui;

import javax.swing.*;
import java.awt.*;

/**
 * 现代化圆角面板
 * 用于展示卡片等需要圆角背景的容器
 */
public class ModernPanel extends JPanel {
    private int cornerRadius = 15;
    private Color borderColor = new Color(220, 220, 220);
    private boolean showBorder = true;

    public ModernPanel() {
        super();
        setOpaque(false);
    }

    public ModernPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setShowBorder(boolean show) {
        this.showBorder = show;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制背景
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // 绘制边框
        if (showBorder) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        super.paintComponent(g);
        g2.dispose();
    }
}
