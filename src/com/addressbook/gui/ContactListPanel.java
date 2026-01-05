package com.addressbook.gui;

import com.addressbook.model.Contact;
import com.addressbook.service.AddressBookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * 联系人列表面板
 * 展示联系人表格，提供搜索和操作功能
 */
public class ContactListPanel extends ModernPanel {
    private AddressBookService service;
    private ContactTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private Consumer<Contact> onEditRequest;

    public ContactListPanel(AddressBookService service) {
        super(new BorderLayout(0, 0));
        this.service = service;
        this.tableModel = new ContactTableModel();

        setBackground(Color.WHITE); // 设置为白色背景
        setShowBorder(false); // 不显示边框，只要圆角背景

        // 1. 顶部工具栏
        add(createTopBar(), BorderLayout.NORTH);

        // 2. 中间表格
        add(createTablePanel(), BorderLayout.CENTER);

        // 3. 底部状态栏
        add(createBottomBar(), BorderLayout.SOUTH);

        // 初始加载数据
        refreshData();
    }

    public void setOnEditRequest(Consumer<Contact> onEditRequest) {
        this.onEditRequest = onEditRequest;
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 5, 0, 5);

        // 1. 搜索图标
        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(searchLabel, gbc);

        // 2. 搜索框
        searchField = new ModernTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "输入姓名查找...");
        searchField.setPreferredSize(new Dimension(180, 36));
        gbc.gridx = 1;
        panel.add(searchField, gbc);

        // 3. 搜索按钮组
        JPanel searchBtnGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchBtnGroup.setOpaque(false);

        JButton searchBtn = new ModernButton("精确", new Color(52, 152, 219));
        searchBtn.setPreferredSize(new Dimension(70, 36));
        searchBtn.addActionListener(e -> performSearch(false));

        JButton prefixSearchBtn = new ModernButton("前缀", new Color(41, 128, 185));
        prefixSearchBtn.setPreferredSize(new Dimension(70, 36));
        prefixSearchBtn.addActionListener(e -> performSearch(true));

        searchBtnGroup.add(searchBtn);
        searchBtnGroup.add(prefixSearchBtn);

        gbc.gridx = 2;
        panel.add(searchBtnGroup, gbc);

        // 4. 占位符 (撑开中间空间)
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        panel.add(Box.createHorizontalGlue(), gbc);

        // 5. 操作按钮组
        JPanel actionBtnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionBtnGroup.setOpaque(false);

        JButton resetBtn = new ModernButton("重置", new Color(149, 165, 166));
        resetBtn.setPreferredSize(new Dimension(70, 36));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });

        JButton editBtn = new ModernButton("编辑", new Color(243, 156, 18));
        editBtn.setPreferredSize(new Dimension(70, 36));
        editBtn.addActionListener(e -> handleEdit());

        JButton deleteBtn = new ModernButton("删除", new Color(231, 76, 60));
        deleteBtn.setPreferredSize(new Dimension(70, 36));
        deleteBtn.addActionListener(e -> handleDelete());

        actionBtnGroup.add(resetBtn);
        actionBtnGroup.add(editBtn);
        actionBtnGroup.add(deleteBtn);

        gbc.gridx = 4;
        gbc.weightx = 0;
        panel.add(actionBtnGroup, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(20, 20, 0, 20));

        table = new JTable(tableModel);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        table.setRowHeight(55); // 增加行高，给多行内容留出空间
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(232, 246, 253)); // 选中背景色
        table.setSelectionForeground(Color.BLACK);

        // 表头样式
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(127, 140, 141));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(236, 240, 241)));

        // 单元格渲染器
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBorder(new EmptyBorder(0, 10, 0, 10));

        // 自定义渲染器实现斑马纹和内边距
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(Color.WHITE); // 全白背景更干净
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 移除边框

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(236, 240, 241));

        JLabel tipLabel = new JLabel("💡 提示: 双击列表行也可以快速编辑联系人");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tipLabel.setForeground(new Color(149, 165, 166));

        panel.add(tipLabel);
        return panel;
    }

    private void performSearch(boolean isPrefix) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入搜索关键词", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Contact> results;
        if (isPrefix) {
            results = service.findByPrefix(keyword);
        } else {
            results = service.findContact(keyword);
        }

        tableModel.setContacts(results);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "未找到匹配的联系人", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的联系人", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Contact contact = tableModel.getContactAt(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除联系人 " + contact.getName() + " 吗？",
                "确认删除", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = service.deleteContact(contact.getName(), contact.getPhone());
            if (success) {
                JOptionPane.showMessageDialog(this, "删除成功");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的联系人", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (onEditRequest != null) {
            onEditRequest.accept(tableModel.getContactAt(row));
        }
    }

    public void refreshData() {
        List<Contact> all = service.getAllContacts();
        tableModel.setContacts(all);
    }
}
