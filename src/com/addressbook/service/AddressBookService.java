package com.addressbook.service;

import com.addressbook.model.Contact;
import com.addressbook.tree.AVLTree;
import java.util.List;

/**
 * 通讯录服务类
 * 提供通讯录管理的业务逻辑层
 */
public class AddressBookService {
    private AVLTree tree;  // AVL树数据结构

    /**
     * 构造函数
     */
    public AddressBookService() {
        this.tree = new AVLTree();
    }

    /**
     * 添加联系人
     */
    public void addContact(String name, String phone, String email, String address) {
        try {
            Contact contact = new Contact(name, phone, email, address);
            tree.insert(contact);
            System.out.println("[成功] 成功添加联系人: " + name);
        } catch (Exception e) {
            System.out.println("[错误] 添加失败: " + e.getMessage());
        }
    }

    /**
     * 删除联系人（根据姓名和电话）
     */
    public void deleteContact(String name, String phone) {
        try {
            boolean success = tree.delete(name, phone);
            if (success) {
                System.out.println("[成功] 成功删除联系人: " + name);
            } else {
                System.out.println("[错误] 未找到该联系人");
            }
        } catch (Exception e) {
            System.out.println("[错误] 删除失败: " + e.getMessage());
        }
    }

    /**
     * 删除所有同名联系人
     */
    public void deleteAllByName(String name) {
        try {
            boolean success = tree.delete(name, null);
            if (success) {
                System.out.println("[成功] 成功删除所有名为 " + name + " 的联系人");
            } else {
                System.out.println("[错误] 未找到该联系人");
            }
        } catch (Exception e) {
            System.out.println("[错误] 删除失败: " + e.getMessage());
        }
    }

    /**
     * 查找联系人（精确查找）
     */
    public void findContact(String name) {
        try {
            List<Contact> contacts = tree.search(name);
            if (contacts.isEmpty()) {
                System.out.println("[错误] 未找到姓名为 \"" + name + "\" 的联系人");
            } else {
                System.out.println("\n找到 " + contacts.size() + " 个联系人:");
                for (Contact c : contacts) {
                    System.out.println("  " + c);
                }
            }
        } catch (Exception e) {
            System.out.println("[错误] 查找失败: " + e.getMessage());
        }
    }

    /**
     * 前缀模糊查找
     */
    public void findByPrefix(String prefix) {
        try {
            List<Contact> contacts = tree.searchByPrefix(prefix);
            if (contacts.isEmpty()) {
                System.out.println("[错误] 未找到姓名以 \"" + prefix + "\" 开头的联系人");
            } else {
                System.out.println("\n找到 " + contacts.size() + " 个联系人:");
                for (Contact c : contacts) {
                    System.out.println("  " + c);
                }
            }
        } catch (Exception e) {
            System.out.println("[错误] 查找失败: " + e.getMessage());
        }
    }

    /**
     * 更新联系人信息
     */
    public void updateContact(String name, String phone, String newPhone, String newEmail, String newAddress) {
        try {
            Contact newContact = new Contact(name, newPhone, newEmail, newAddress);
            boolean success = tree.update(name, phone, newContact);
            if (success) {
                System.out.println("[成功] 成功更新联系人信息");
            } else {
                System.out.println("[错误] 未找到该联系人");
            }
        } catch (Exception e) {
            System.out.println("[错误] 更新失败: " + e.getMessage());
        }
    }

    /**
     * 显示所有联系人
     */
    public void displayAllContacts() {
        if (tree.isEmpty()) {
            System.out.println("通讯录为空");
        } else {
            tree.inorderTraversal();
        }
    }

    /**
     * 中序遍历
     */
    public void inorderTraversal() {
        if (tree.isEmpty()) {
            System.out.println("通讯录为空");
        } else {
            tree.inorderTraversal();
        }
    }

    /**
     * 前序遍历
     */
    public void preorderTraversal() {
        if (tree.isEmpty()) {
            System.out.println("通讯录为空");
        } else {
            tree.preorderTraversal();
        }
    }

    /**
     * 后序遍历
     */
    public void postorderTraversal() {
        if (tree.isEmpty()) {
            System.out.println("通讯录为空");
        } else {
            tree.postorderTraversal();
        }
    }

    /**
     * 获取所有联系人数量
     */
    public int getContactCount() {
        return tree.getAllContacts().size();
    }

    /**
     * 查找联系人（返回结果列表）
     */
    public List<Contact> searchByName(String name) {
        return tree.search(name);
    }

    /**
     * 前缀模糊查找（返回结果列表）
     */
    public List<Contact> searchByPrefix(String prefix) {
        return tree.searchByPrefix(prefix);
    }

    /**
     * 获取所有联系人
     */
    public List<Contact> getAllContacts() {
        return tree.getAllContacts();
    }
}
