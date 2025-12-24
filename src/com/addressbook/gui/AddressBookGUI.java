package com.addressbook.gui;

import com.addressbook.model.Contact;
import com.addressbook.service.AddressBookService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * 通讯录管理系统GUI主界面
 * 基于Java Swing实现的图形用户界面
 */
public class AddressBookGUI extends JFrame {
    private AddressBookService service;
    private ContactTableModel tableModel;
    
    // UI组件
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField searchField;
    private JTable contactTable;
    private JTextArea resultArea;
    
    /**
     * 构造函数
     */
    public AddressBookGUI() {
        service = new AddressBookService();
        tableModel = new ContactTableModel();
        
        // 初始化测试数据
        initTestData();
        
        // 初始化界面
        initUI();
        
        // 加载所有联系人
        refreshContactList();
    }

    /**
     * 初始化用户界面
     */
    private void initUI() {
        setTitle("基于AVL树的通讯录管理系统 - GUI版本");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // 设置现代化的外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 顶部标题
        JLabel titleLabel = new JLabel("通讯录管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // 左侧输入面板
        JPanel leftPanel = createInputPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        
        // 中间表格面板
        JPanel centerPanel = createTablePanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // 右侧操作面板
        JPanel rightPanel = createOperationPanel();
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        add(mainPanel);
    }

    /**
     * 创建输入面板
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "联系人信息", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14)
        ));
        panel.setPreferredSize(new Dimension(280, 0));
        
        // 姓名输入
        panel.add(createFieldPanel("姓名:", nameField = new JTextField(20)));
        panel.add(Box.createVerticalStrut(10));
        
        // 电话输入
        panel.add(createFieldPanel("电话:", phoneField = new JTextField(20)));
        panel.add(Box.createVerticalStrut(10));
        
        // 邮箱输入
        panel.add(createFieldPanel("邮箱:", emailField = new JTextField(20)));
        panel.add(Box.createVerticalStrut(10));
        
        // 地址输入
        panel.add(createFieldPanel("地址:", addressField = new JTextField(20)));
        panel.add(Box.createVerticalStrut(20));
        
        // 操作按钮
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JButton addButton = createStyledButton("添加联系人", new Color(46, 204, 113));
        addButton.addActionListener(e -> addContact());
        buttonPanel.add(addButton);
        
        JButton updateButton = createStyledButton("修改联系人", new Color(52, 152, 219));
        updateButton.addActionListener(e -> updateContact());
        buttonPanel.add(updateButton);
        
        JButton deleteButton = createStyledButton("删除联系人", new Color(231, 76, 60));
        deleteButton.addActionListener(e -> deleteContact());
        buttonPanel.add(deleteButton);
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // 清空按钮
        JButton clearButton = createStyledButton("清空输入", new Color(149, 165, 166));
        clearButton.addActionListener(e -> clearInputFields());
        panel.add(clearButton);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    /**
     * 创建表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "联系人列表", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14)
        ));
        
        // 创建表格
        contactTable = new JTable(tableModel);
        contactTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contactTable.setRowHeight(30);
        contactTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 16));
        contactTable.getTableHeader().setBackground(new Color(52, 73, 94));
        contactTable.getTableHeader().setForeground(Color.BLACK);
        contactTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactTable.setGridColor(new Color(189, 195, 199));
        
        // 设置列宽
        contactTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        contactTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        contactTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        contactTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        // 表格选择监听
        contactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = contactTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Contact contact = tableModel.getContactAt(selectedRow);
                    if (contact != null) {
                        fillInputFields(contact);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(contactTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 统计信息
        JLabel countLabel = new JLabel("总联系人数: 0");
        countLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        countLabel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(countLabel, BorderLayout.SOUTH);
        
        // 更新统计的方法
        tableModel.addTableModelListener(e -> {
            countLabel.setText("总联系人数: " + tableModel.getRowCount());
        });
        
        return panel;
    }

    /**
     * 创建操作面板
     */
    private JPanel createOperationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "查询与操作", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14)
        ));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // 查询区域
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel searchLabel = new JLabel("查询关键字:");
        searchLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        searchPanel.add(searchLabel, BorderLayout.NORTH);
        searchField = new JTextField();
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(0, 28));
        searchPanel.add(searchField, BorderLayout.CENTER);
        panel.add(searchPanel);
        
        panel.add(Box.createVerticalStrut(10));
        
        // 查询按钮
        JPanel queryButtonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        queryButtonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        JButton exactSearchButton = createStyledButton("精确查询", new Color(155, 89, 182));
        exactSearchButton.addActionListener(e -> exactSearch());
        queryButtonPanel.add(exactSearchButton);
        
        JButton prefixSearchButton = createStyledButton("前缀模糊查询", new Color(230, 126, 34));
        prefixSearchButton.addActionListener(e -> prefixSearch());
        queryButtonPanel.add(prefixSearchButton);
        
        panel.add(queryButtonPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // 遍历操作
        JPanel traversalPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        traversalPanel.setBorder(BorderFactory.createTitledBorder("遍历操作"));
        traversalPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        JButton showAllButton = createStyledButton("显示所有", new Color(26, 188, 156));
        showAllButton.addActionListener(e -> refreshContactList());
        traversalPanel.add(showAllButton);
        
        JButton inorderButton = createStyledButton("中序遍历", new Color(41, 128, 185));
        inorderButton.addActionListener(e -> showTraversal("inorder"));
        traversalPanel.add(inorderButton);
        
        JButton preorderButton = createStyledButton("前序遍历", new Color(142, 68, 173));
        preorderButton.addActionListener(e -> showTraversal("preorder"));
        traversalPanel.add(preorderButton);
        
        JButton postorderButton = createStyledButton("后序遍历", new Color(211, 84, 0));
        postorderButton.addActionListener(e -> showTraversal("postorder"));
        traversalPanel.add(postorderButton);
        
        panel.add(traversalPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // 结果显示区域
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("操作结果"));
        resultArea = new JTextArea(10, 20);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(250, 250, 250));
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultPanel.add(resultScroll, BorderLayout.CENTER);
        panel.add(resultPanel);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    /**
     * 创建字段输入面板
     */
    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        jLabel.setPreferredSize(new Dimension(50, 30));
        panel.add(jLabel, BorderLayout.WEST);
        field.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 28));
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建样式化按钮
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(0, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * 添加联系人
     */
    private void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            showMessage("错误", "姓名和电话不能为空！", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        service.addContact(name, phone, email, address);
        refreshContactList();
        clearInputFields();
        resultArea.append("[成功] 添加联系人: " + name + "\n");
    }

    /**
     * 修改联系人
     */
    private void updateContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("提示", "请先选择要修改的联系人！", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Contact oldContact = tableModel.getContactAt(selectedRow);
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            showMessage("错误", "姓名和电话不能为空！", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        service.updateContact(oldContact.getName(), oldContact.getPhone(), phone, email, address);
        refreshContactList();
        clearInputFields();
        resultArea.append("[成功] 更新联系人: " + name + "\n");
    }

    /**
     * 删除联系人
     */
    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("提示", "请先选择要删除的联系人！", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Contact contact = tableModel.getContactAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "确定要删除联系人 \"" + contact.getName() + "\" 吗？", 
            "确认删除", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteContact(contact.getName(), contact.getPhone());
            refreshContactList();
            clearInputFields();
            resultArea.append("[成功] 删除联系人: " + contact.getName() + "\n");
        }
    }

    /**
     * 精确查询
     */
    private void exactSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            showMessage("提示", "请输入查询关键字！", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Contact> results = service.searchByName(keyword);
        if (results.isEmpty()) {
            resultArea.append("[查询] 未找到姓名为 \"" + keyword + "\" 的联系人\n");
            showMessage("查询结果", "未找到匹配的联系人！", JOptionPane.INFORMATION_MESSAGE);
        } else {
            tableModel.setContacts(results);
            resultArea.append("[查询] 找到 " + results.size() + " 个匹配的联系人\n");
        }
    }

    /**
     * 前缀模糊查询
     */
    private void prefixSearch() {
        String prefix = searchField.getText().trim();
        if (prefix.isEmpty()) {
            showMessage("提示", "请输入查询前缀！", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Contact> results = service.searchByPrefix(prefix);
        if (results.isEmpty()) {
            resultArea.append("[查询] 未找到以 \"" + prefix + "\" 开头的联系人\n");
            showMessage("查询结果", "未找到匹配的联系人！", JOptionPane.INFORMATION_MESSAGE);
        } else {
            tableModel.setContacts(results);
            resultArea.append("[查询] 找到 " + results.size() + " 个匹配的联系人\n");
        }
    }

    /**
     * 显示遍历结果
     */
    private void showTraversal(String type) {
        String title = "";
        switch (type) {
            case "inorder":
                title = "中序遍历（升序）";
                break;
            case "preorder":
                title = "前序遍历";
                break;
            case "postorder":
                title = "后序遍历";
                break;
        }
        
        resultArea.append("\n=== " + title + " ===\n");
        List<Contact> contacts = service.getAllContacts();
        for (Contact c : contacts) {
            resultArea.append(c.toString() + "\n");
        }
        resultArea.append("==================\n\n");
    }

    /**
     * 刷新联系人列表
     */
    private void refreshContactList() {
        List<Contact> allContacts = service.getAllContacts();
        tableModel.setContacts(allContacts);
        resultArea.append("[刷新] 已加载 " + allContacts.size() + " 个联系人\n");
    }

    /**
     * 清空输入框
     */
    private void clearInputFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        contactTable.clearSelection();
    }

    /**
     * 填充输入框
     */
    private void fillInputFields(Contact contact) {
        nameField.setText(contact.getName());
        phoneField.setText(contact.getPhone());
        emailField.setText(contact.getEmail());
        addressField.setText(contact.getAddress());
    }

    /**
     * 显示消息对话框
     */
    private void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * 初始化测试数据
     */
    private void initTestData() {
        service.addContact("张三", "13800138001", "zhangsan@email.com", "北京市朝阳区");
        service.addContact("李四", "13800138002", "lisi@email.com", "上海市浦东新区");
        service.addContact("王五", "13800138003", "wangwu@email.com", "广州市天河区");
        service.addContact("赵六", "13800138004", "zhaoliu@email.com", "深圳市南山区");
        service.addContact("张伟", "13800138005", "zhangwei@email.com", "杭州市西湖区");
        service.addContact("张丽", "13800138006", "zhangli@email.com", "成都市武侯区");
    }

    /**
     * 主方法
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddressBookGUI gui = new AddressBookGUI();
            gui.setVisible(true);
        });
    }
}
