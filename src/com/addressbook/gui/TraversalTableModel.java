package com.addressbook.gui;

import com.addressbook.model.Contact;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历表格模型（带序号）
 * 用于在遍历页面展示联系人信息
 */
public class TraversalTableModel extends AbstractTableModel {
    private final String[] columnNames = {"序号", "姓名", "电话", "邮箱", "地址"};
    private List<Contact> contacts;

    /**
     * 构造函数
     */
    public TraversalTableModel() {
        this.contacts = new ArrayList<>();
    }

    /**
     * 设置联系人数据
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts != null ? new ArrayList<>(contacts) : new ArrayList<>();
        fireTableDataChanged();
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        contacts.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return contacts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contact contact = contacts.get(rowIndex);
        switch (columnIndex) {
            case 0:
                // 序号列
                return "<html><body style='padding:5px;'><span style='color:#95a5a6;font-size:16px;'><b>" + (rowIndex + 1) + "</b></span></body></html>";
            case 1: 
                return "<html><body style='padding:5px;'><b>" + contact.getName() + "</b></body></html>";
            case 2: 
                return "<html><body style='padding:5px;'><span style='color:#34495e;'>📞 " + contact.getPhone() + "</span></body></html>";
            case 3: 
                return "<html><body style='padding:5px;'><span style='color:#7f8c8d;'>✉ " + contact.getEmail() + "</span></body></html>";
            case 4: 
                return "<html><body style='padding:5px;line-height:1.5;'><span style='color:#95a5a6;'>🏠 " + contact.getAddress() + "</span></body></html>";
            default: 
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
