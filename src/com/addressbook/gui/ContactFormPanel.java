package com.addressbook.gui;

import com.addressbook.model.Contact;
import com.addressbook.service.AddressBookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/**
 * 联系人表单面板
 * 用于添加和修改联系人
 */
public class ContactFormPanel extends ModernPanel {
    private AddressBookService service;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JButton saveButton;
    private JButton cancelButton;

    private Contact editingContact; // 当前正在编辑的联系人（如果是新增则为null）
    private Runnable onSaveSuccess;
    private Runnable onCancel;

    public ContactFormPanel(AddressBookService service) {
        super(new GridBagLayout());
        this.service = service;
        setBackground(Color.WHITE);
        setShowBorder(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initUI();
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }

    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        JLabel titleLabel = new JLabel("联系人信息");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);

        // 重置约束
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 字段
        addFormField(formPanel, gbc, 1, "姓名 *:", nameField = new ModernTextField(20));
        addFormField(formPanel, gbc, 2, "电话 *:", phoneField = new ModernTextField(20));
        addFormField(formPanel, gbc, 3, "邮箱:", emailField = new ModernTextField(20));
        addFormField(formPanel, gbc, 4, "地址:", addressField = new ModernTextField(20));

        // 按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        saveButton = new ModernButton("保存", new Color(46, 204, 113));
        saveButton.setPreferredSize(new Dimension(100, 40));
        saveButton.addActionListener(e -> save());

        cancelButton = new ModernButton("取消", new Color(149, 165, 166));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> {
            if (onCancel != null)
                onCancel.run();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);

        add(formPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(250, 35));
        panel.add(field, gbc);
    }

    public void clearForm() {
        editingContact = null;
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        saveButton.setText("添加");
    }

    public void setContact(Contact contact) {
        this.editingContact = contact;
        nameField.setText(contact.getName());
        phoneField.setText(contact.getPhone());
        emailField.setText(contact.getEmail());
        addressField.setText(contact.getAddress());
        saveButton.setText("更新");
    }

    private void save() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "姓名和电话不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (editingContact == null) {
                // 添加模式
                service.addContact(name, phone, email, address);
                JOptionPane.showMessageDialog(this, "添加成功");
            } else {
                // 更新模式
                boolean success = service.updateContact(
                        editingContact.getName(),
                        editingContact.getPhone(),
                        phone, email, address);
                if (success) {
                    JOptionPane.showMessageDialog(this, "更新成功");
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败，原联系人可能已被删除", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
