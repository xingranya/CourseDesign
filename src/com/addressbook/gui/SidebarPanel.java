package com.addressbook.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 侧边栏面板
 * 提供主要功能的导航，采用现代化扁平设计
 */
public class SidebarPanel extends JPanel {
    private List<MenuButton> menuButtons;
    private final Color SIDEBAR_BG = new Color(44, 62, 80); // 深蓝灰背景
    private final Color TEXT_COLOR = new Color(236, 240, 241); // 浅灰文本
    private final Color HOVER_COLOR = new Color(52, 73, 94); // 悬停背景
    private final Color ACTIVE_COLOR = new Color(41, 128, 185); // 激活色 (高亮)
    private final Color ACCENT_COLOR = new Color(46, 204, 113); // 装饰色 (左侧条)

    public SidebarPanel(ActionListener navigationListener) {
        setLayout(new BorderLayout());
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(240, 0)); // 加宽一点

        menuButtons = new ArrayList<>();

        // 1. 顶部 Logo 区域
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel iconLabel = new JLabel("📱"); // 使用Emoji作为简单Logo，或者用绘图
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setForeground(ACCENT_COLOR);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("通讯录系统");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Address Book Pro");
        subtitleLabel.setForeground(new Color(149, 165, 166));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(iconLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(subtitleLabel);

        add(topPanel, BorderLayout.NORTH);

        // 2. 中间菜单区域
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        addMenuButton(menuPanel, "联系人列表", "list", "👥", navigationListener);
        addMenuButton(menuPanel, "添加联系人", "add", "➕", navigationListener);
        addMenuButton(menuPanel, "遍历查看", "traversal", "🌳", navigationListener);

        add(menuPanel, BorderLayout.CENTER);

        // 3. 底部版本信息
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel versionLabel = new JLabel("v2.1 GUI Edition");
        versionLabel.setForeground(new Color(127, 140, 141));
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        bottomPanel.add(versionLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addMenuButton(JPanel panel, String text, String actionCommand, String icon, ActionListener listener) {
        MenuButton button = new MenuButton(text, icon);
        button.setActionCommand(actionCommand);
        button.addActionListener(listener);
        button.addActionListener(e -> setActiveButton(button));

        menuButtons.add(button);
        panel.add(button);
        // 不再需要垂直间距，按钮紧贴看起来更像菜单列表
    }

    private void setActiveButton(MenuButton activeBtn) {
        for (MenuButton btn : menuButtons) {
            btn.setActive(false);
        }
        activeBtn.setActive(true);
    }

    /**
     * 自定义菜单按钮
     */
    private class MenuButton extends JButton {
        private boolean isActive = false;
        private boolean isHover = false;
        private String iconSymbol;

        public MenuButton(String text, String iconSymbol) {
            super(text);
            this.iconSymbol = iconSymbol;

            setFont(new Font("微软雅黑", Font.PLAIN, 15));
            setForeground(TEXT_COLOR);
            setBackground(SIDEBAR_BG);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false); // 自己绘制背景
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // 强制左对齐，并设置固定高度
            setHorizontalAlignment(SwingConstants.LEFT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            setPreferredSize(new Dimension(240, 55));

            // 增加左侧内边距，给图标留空间
            setBorder(new EmptyBorder(0, 30, 0, 0));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
            });
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制背景
            if (isActive) {
                g2.setColor(ACTIVE_COLOR); // 激活状态背景更亮
                // 圆角矩形背景 (Pill shape)
                g2.fillRoundRect(10, 5, getWidth() - 20, getHeight() - 10, 15, 15);

                setFont(new Font("微软雅黑", Font.BOLD, 15));
                setForeground(Color.WHITE);
            } else if (isHover) {
                g2.setColor(HOVER_COLOR);
                g2.fillRoundRect(10, 5, getWidth() - 20, getHeight() - 10, 15, 15);
                setForeground(Color.WHITE);
            } else {
                // 默认背景透明 (使用父容器背景)
                // g2.setColor(SIDEBAR_BG);
                // g2.fillRect(0, 0, getWidth(), getHeight());
                setFont(new Font("微软雅黑", Font.PLAIN, 15));
                setForeground(TEXT_COLOR);
            }

            // 绘制图标 (手动绘制以控制位置)
            g2.setColor(getForeground());
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18)); // 使用支持Emoji的字体
            g2.drawString(iconSymbol, 25, 34); // 图标位置

            // 绘制文字 (手动绘制或者调用super，这里调用super比较简单，但需要处理偏移)
            // 为了简单，我们让super绘制文字，通过Border控制了文字的左边距
            // 但是super会绘制默认的文字位置，可能和我们自定义的背景配合不好
            // 所以这里我们完全自己绘制

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), 60, textY); // 文字位置
        }
    }
}
