package com.addressbook.gui;

import com.addressbook.model.Contact;
import com.addressbook.service.AddressBookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * 遍历展示面板
 * 使用表格形式展示前序、中序、后序遍历结果
 */
public class TraversalPanel extends ModernPanel {
    private AddressBookService service;
    private TraversalTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;

    public TraversalPanel(AddressBookService service) {
        super(new BorderLayout(0, 0));
        this.service = service;
        this.tableModel = new TraversalTableModel();
        setBackground(Color.WHITE);
        setShowBorder(false);

        // 1. 顶部控制栏
        add(createTopBar(), BorderLayout.NORTH);

        // 2. 中间表格区域
        add(createTablePanel(), BorderLayout.CENTER);

        // 3. 底部提示栏
        add(createBottomBar(), BorderLayout.SOUTH);

        // 默认显示中序
        showTraversal("inorder", "中序遍历 (左 -> 根 -> 右，即升序)");
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 左侧：按钮组
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

        // 右侧：状态标签
        statusLabel = new JLabel("请选择遍历方式");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        statusLabel.setForeground(new Color(127, 140, 141));

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    private JButton createTabButton(String text, Color color) {
        JButton btn = new ModernButton(text, color);
        btn.setPreferredSize(new Dimension(110, 36));
        return btn;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(20, 20, 0, 20));

        table = new JTable(tableModel);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.setRowHeight(55); // 给多行内容留出空间
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(232, 246, 253));
        table.setSelectionForeground(Color.BLACK);

        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // 序号列
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // 姓名列
        table.getColumnModel().getColumn(2).setPreferredWidth(180); // 电话列
        table.getColumnModel().getColumn(3).setPreferredWidth(250); // 邮箱列
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // 地址列

        // 表头样式
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(127, 140, 141));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(236, 240, 241)));

        // 单元格渲染器
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }

                // 底部边框分割线
                JComponent jc = (JComponent) c;
                jc.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                        new EmptyBorder(0, 15, 0, 15)));

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(236, 240, 241));

        JLabel tipLabel = new JLabel("💡 提示: 选择不同的遍历方式查看AVL树的遍历顺序");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tipLabel.setForeground(new Color(149, 165, 166));

        panel.add(tipLabel);
        return panel;
    }

    private void showTraversal(String type, String desc) {
        statusLabel.setText(desc);

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

        if (contacts != null) {
            tableModel.setContacts(contacts);
        } else {
            tableModel.clear();
        }
    }
}
