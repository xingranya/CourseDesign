package com.addressbook.gui;

import com.addressbook.model.Contact;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人表格模型
 * 用于在JTable中展示联系人信息
 */
public class ContactTableModel extends AbstractTableModel {
    private final String[] columnNames = {"姓名", "电话", "邮箱", "地址"};
    private List<Contact> contacts;

    /**
     * 构造函数
     */
    public ContactTableModel() {
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
     * 添加联系人
     */
    public void addContact(Contact contact) {
        contacts.add(contact);
        fireTableRowsInserted(contacts.size() - 1, contacts.size() - 1);
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        contacts.clear();
        fireTableDataChanged();
    }

    /**
     * 获取指定行的联系人
     */
    public Contact getContactAt(int row) {
        if (row >= 0 && row < contacts.size()) {
            return contacts.get(row);
        }
        return null;
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
            case 0: return contact.getName();
            case 1: return contact.getPhone();
            case 2: return contact.getEmail();
            case 3: return contact.getAddress();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
