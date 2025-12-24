package com.addressbook.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 二叉搜索树节点类
 * 每个节点存储一个姓名作为关键字，以及具有该姓名的所有联系人（处理重名情况）
 */
public class BSTNode {
    private String key;                        // 姓名关键字
    private LinkedList<Contact> contacts;      // 存储同名联系人的链表
    private BSTNode left;                      // 左子节点
    private BSTNode right;                     // 右子节点
    private int height;                        // 节点高度（用于AVL树平衡）

    /**
     * 构造函数
     */
    public BSTNode(String key, Contact contact) {
        this.key = key;
        this.contacts = new LinkedList<>();
        this.contacts.add(contact);
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    // Getter 和 Setter 方法
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LinkedList<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public BSTNode getLeft() {
        return left;
    }

    public void setLeft(BSTNode left) {
        this.left = left;
    }

    public BSTNode getRight() {
        return right;
    }

    public void setRight(BSTNode right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 获取所有同名联系人
     */
    public List<Contact> getAllContacts() {
        return new LinkedList<>(contacts);
    }

    /**
     * 移除指定的联系人
     */
    public boolean removeContact(Contact contact) {
        return contacts.remove(contact);
    }

    /**
     * 判断该节点是否还有联系人
     */
    public boolean hasContacts() {
        return !contacts.isEmpty();
    }
}
