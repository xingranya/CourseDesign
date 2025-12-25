package com.addressbook.gui;

import com.addressbook.model.Contact;
import com.addressbook.service.AddressBookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * 遍历展示面板
 * 以卡片列表形式展示前序、中序、后序遍历结果
 */
public class TraversalPanel extends ModernPanel {
    private AddressBookService service;
    private JPanel cardsPanel;
    private JLabel statusLabel;
    private JScrollPane scrollPane;

    public TraversalPanel(AddressBookService service) {
        super(new BorderLayout(0, 0));
        this.service = service;
        setBackground(Color.WHITE);
        setShowBorder(false);

        // 1. 顶部控制栏
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 按钮组
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton preorderBtn = createTabButton("前序遍历", new Color(142, 68, 173));
        preorderBtn.addActionListener(e -> showTraversal("preorder", "前序遍历 (根 -> 左 -> 右)"));

        JButton inorderBtn = createTabButton("中序遍历", new Color(41, 128, 185));
        inorderBtn.addActionListener(e -> showTraversal("inorder", "中序遍历 (左 -> 根 -> 右，即升序)"));

        JButton postorderBtn = createTabButton("后序遍历", new Color(211, 84, 0));
        postorderBtn.addActionListener(e -> showTraversal("postorder", "后序遍历 (左 -> 右 -> 根)"));

        buttonPanel.add(preorderBtn);
        buttonPanel.add(inorderBtn);
        buttonPanel.add(postorderBtn);

        // 状态标签
        statusLabel = new JLabel("请选择遍历方式");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        statusLabel.setForeground(new Color(127, 140, 141));

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 2. 内容区域 (卡片列表)
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(new Color(236, 240, 241));
        cardsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(236, 240, 241));

        add(scrollPane, BorderLayout.CENTER);

        // 默认显示中序
        showTraversal("inorder", "中序遍历 (左 -> 根 -> 右，即升序)");
    }

    private JButton createTabButton(String text, Color color) {
        JButton btn = new ModernButton(text, color);
        btn.setPreferredSize(new Dimension(100, 36));
        return btn;
    }

    private void showTraversal(String type, String desc) {
        statusLabel.setText(desc);
        cardsPanel.removeAll();

        List<Contact> contacts = null;
        switch (type) {
            case "preorder":
                contacts = service.getPreorderContacts();
                break;
            case "inorder":
                contacts = service.getAllContacts();
                break;
            case "postorder":
                contacts = service.getPostorderContacts();
                break;
        }

        if (contacts != null && !contacts.isEmpty()) {
            int index = 1;
            for (Contact c : contacts) {
                cardsPanel.add(createContactCard(c, index++));
                cardsPanel.add(Box.createVerticalStrut(10)); // 卡片间距
            }
        } else {
            JLabel emptyLabel = new JLabel("暂无联系人数据", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsPanel.add(Box.createVerticalGlue());
            cardsPanel.add(emptyLabel);
            cardsPanel.add(Box.createVerticalGlue());
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();

        // 滚动到顶部
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    /**
     * 创建单个联系人卡片
     */
    private JPanel createContactCard(Contact c, int index) {
        ModernPanel card = new ModernPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(0, 90));

        // 左侧：序号和头像
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(60, 0));

        JLabel indexLabel = new JLabel(String.valueOf(index), SwingConstants.CENTER);
        indexLabel.setFont(new Font("Arial", Font.BOLD, 24));
        indexLabel.setForeground(new Color(189, 195, 199));
        leftPanel.add(indexLabel, BorderLayout.CENTER);

        card.add(leftPanel, BorderLayout.WEST);

        // 中间：信息
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);

        // 第一行：姓名
        JLabel nameLabel = new JLabel(c.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        nameLabel.setForeground(new Color(44, 62, 80));
        centerPanel.add(nameLabel);

        // 第二行：详细信息 (使用HTML实现简单的富文本)
        String details = String.format(
                "<html><font color='#7F8C8D'>📱 %s &nbsp;&nbsp;|&nbsp;&nbsp; 📧 %s &nbsp;&nbsp;|&nbsp;&nbsp; 🏠 %s</font></html>",
                c.getPhone(), c.getEmail(), c.getAddress());
        JLabel detailLabel = new JLabel(details);
        detailLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        centerPanel.add(detailLabel);

        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }
}
