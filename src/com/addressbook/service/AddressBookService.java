package com.addressbook.service;

import com.addressbook.model.Contact;
import com.addressbook.tree.AVLTree;
import java.util.List;

/**
 * 通讯录服务类
 * 提供通讯录管理的业务逻辑层
 */
public class AddressBookService {
    private AVLTree tree; // AVL树数据结构

    /**
     * 构造函数
     */
    public AddressBookService() {
        this.tree = new AVLTree();
    }

    /**
     * 添加联系人
     * 
     * @throws IllegalArgumentException 如果添加失败
     */
    public void addContact(String name, String phone, String email, String address) {
        try {
            Contact contact = new Contact(name, phone, email, address);
            tree.insert(contact);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 删除联系人（根据姓名和电话）
     * 
     * @return 是否删除成功
     */
    public boolean deleteContact(String name, String phone) {
        return tree.delete(name, phone);
    }

    /**
     * 删除所有同名联系人
     * 
     * @return 是否删除成功
     */
    public boolean deleteAllByName(String name) {
        return tree.delete(name, null);
    }

    /**
     * 查找联系人（精确查找）
     * 
     * @return 联系人列表
     */
    public List<Contact> findContact(String name) {
        return tree.search(name);
    }

    /**
     * 前缀模糊查找
     * 
     * @return 联系人列表
     */
    public List<Contact> findByPrefix(String prefix) {
        return tree.searchByPrefix(prefix);
    }

    /**
     * 更新联系人信息
     * 
     * @return 是否更新成功
     */
    public boolean updateContact(String name, String phone, String newPhone, String newEmail, String newAddress) {
        Contact newContact = new Contact(name, newPhone, newEmail, newAddress);
        return tree.update(name, phone, newContact);
    }

    /**
     * 获取所有联系人（中序遍历）
     */
    public List<Contact> getAllContacts() {
        return tree.getAllContacts();
    }

    /**
     * 获取前序遍历联系人
     */
    public List<Contact> getPreorderContacts() {
        return tree.getPreorderContacts();
    }

    /**
     * 获取后序遍历联系人
     */
    public List<Contact> getPostorderContacts() {
        return tree.getPostorderContacts();
    }

    /**
     * 获取所有联系人数量
     */
    public int getContactCount() {
        return tree.getAllContacts().size();
    }

    /**
     * 查找联系人（返回结果列表）
     * 
     * @deprecated Use findContact instead
     */
    public List<Contact> searchByName(String name) {
        return tree.search(name);
    }

    /**
     * 前缀模糊查找（返回结果列表）
     * 
     * @deprecated Use findByPrefix instead
     */
    public List<Contact> searchByPrefix(String prefix) {
        return tree.searchByPrefix(prefix);
    }
}
