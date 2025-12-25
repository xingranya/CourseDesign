package com.addressbook.tree;

import com.addressbook.model.BSTNode;
import com.addressbook.model.Contact;
import java.util.ArrayList;
import java.util.List;

/**
 * AVL平衡二叉搜索树实现类
 * 支持联系人的增删改查、遍历、前缀模糊查询等功能
 */
public class AVLTree {
    private BSTNode root; // 树的根节点

    /**
     * 构造函数
     */
    public AVLTree() {
        this.root = null;
    }

    /**
     * 获取节点高度
     */
    private int getHeight(BSTNode node) {
        return node == null ? 0 : node.getHeight();
    }

    /**
     * 更新节点高度
     */
    private void updateHeight(BSTNode node) {
        if (node != null) {
            node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);
        }
    }

    /**
     * 获取平衡因子
     */
    private int getBalanceFactor(BSTNode node) {
        return node == null ? 0 : getHeight(node.getLeft()) - getHeight(node.getRight());
    }

    /**
     * 右旋转
     */
    private BSTNode rightRotate(BSTNode y) {
        BSTNode x = y.getLeft();
        BSTNode T2 = x.getRight();

        // 执行旋转
        x.setRight(y);
        y.setLeft(T2);

        // 更新高度
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * 左旋转
     */
    private BSTNode leftRotate(BSTNode x) {
        BSTNode y = x.getRight();
        BSTNode T2 = y.getLeft();

        // 执行旋转
        y.setLeft(x);
        x.setRight(T2);

        // 更新高度
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * 插入联系人（公共方法）
     */
    public void insert(Contact contact) {
        if (contact == null || contact.getName() == null || contact.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("联系人或姓名不能为空");
        }
        root = insertNode(root, contact);
    }

    /**
     * 插入节点（私有递归方法）
     */
    private BSTNode insertNode(BSTNode node, Contact contact) {
        // 标准BST插入
        if (node == null) {
            return new BSTNode(contact.getName(), contact);
        }

        int cmp = contact.getName().compareTo(node.getKey());
        if (cmp < 0) {
            node.setLeft(insertNode(node.getLeft(), contact));
        } else if (cmp > 0) {
            node.setRight(insertNode(node.getRight(), contact));
        } else {
            // 姓名相同，添加到同名链表中
            node.addContact(contact);
            return node;
        }

        // 更新高度
        updateHeight(node);

        // 获取平衡因子
        int balance = getBalanceFactor(node);

        // 左左情况
        if (balance > 1 && contact.getName().compareTo(node.getLeft().getKey()) < 0) {
            return rightRotate(node);
        }

        // 右右情况
        if (balance < -1 && contact.getName().compareTo(node.getRight().getKey()) > 0) {
            return leftRotate(node);
        }

        // 左右情况
        if (balance > 1 && contact.getName().compareTo(node.getLeft().getKey()) > 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // 右左情况
        if (balance < -1 && contact.getName().compareTo(node.getRight().getKey()) < 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    /**
     * 查找最小节点
     */
    private BSTNode findMin(BSTNode node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * 删除联系人（公共方法）
     */
    public boolean delete(String name, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        int[] result = { 0 };
        root = deleteNode(root, name, phone, result);
        return result[0] > 0;
    }

    /**
     * 删除节点（私有递归方法）
     */
    private BSTNode deleteNode(BSTNode node, String name, String phone, int[] result) {
        if (node == null) {
            return null;
        }

        int cmp = name.compareTo(node.getKey());
        if (cmp < 0) {
            node.setLeft(deleteNode(node.getLeft(), name, phone, result));
        } else if (cmp > 0) {
            node.setRight(deleteNode(node.getRight(), name, phone, result));
        } else {
            // 找到姓名匹配的节点
            if (phone != null) {
                // 删除特定电话号码的联系人
                Contact toRemove = null;
                for (Contact c : node.getContacts()) {
                    if (c.getPhone().equals(phone)) {
                        toRemove = c;
                        break;
                    }
                }
                if (toRemove != null) {
                    node.removeContact(toRemove);
                    result[0]++;
                }
            } else {
                // 删除所有同名联系人
                result[0] = node.getContacts().size();
                node.getContacts().clear();
            }

            // 如果节点没有联系人了，需要删除该节点
            if (!node.hasContacts()) {
                // 只有一个子节点或没有子节点的情况
                if (node.getLeft() == null) {
                    return node.getRight();
                } else if (node.getRight() == null) {
                    return node.getLeft();
                }

                // 有两个子节点的情况
                BSTNode minNode = findMin(node.getRight());
                node.setKey(minNode.getKey());
                node.getContacts().clear();
                node.getContacts().addAll(minNode.getContacts());
                node.setRight(deleteNode(node.getRight(), minNode.getKey(), null, new int[] { 0 }));
            } else {
                return node;
            }
        }

        // 更新高度
        updateHeight(node);

        // 获取平衡因子
        int balance = getBalanceFactor(node);

        // 左左情况
        if (balance > 1 && getBalanceFactor(node.getLeft()) >= 0) {
            return rightRotate(node);
        }

        // 左右情况
        if (balance > 1 && getBalanceFactor(node.getLeft()) < 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // 右右情况
        if (balance < -1 && getBalanceFactor(node.getRight()) <= 0) {
            return leftRotate(node);
        }

        // 右左情况
        if (balance < -1 && getBalanceFactor(node.getRight()) > 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    /**
     * 精确查找联系人（按姓名）
     */
    public List<Contact> search(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        BSTNode node = searchNode(root, name);
        return node == null ? new ArrayList<>() : node.getAllContacts();
    }

    /**
     * 查找节点（私有递归方法）
     */
    private BSTNode searchNode(BSTNode node, String name) {
        if (node == null) {
            return null;
        }

        int cmp = name.compareTo(node.getKey());
        if (cmp < 0) {
            return searchNode(node.getLeft(), name);
        } else if (cmp > 0) {
            return searchNode(node.getRight(), name);
        } else {
            return node;
        }
    }

    /**
     * 前缀模糊查询
     */
    public List<Contact> searchByPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("前缀不能为空");
        }
        List<Contact> results = new ArrayList<>();
        searchByPrefixHelper(root, prefix, results);
        return results;
    }

    /**
     * 前缀模糊查询辅助方法
     */
    private void searchByPrefixHelper(BSTNode node, String prefix, List<Contact> results) {
        if (node == null) {
            return;
        }

        // 中序遍历查找所有匹配前缀的节点
        searchByPrefixHelper(node.getLeft(), prefix, results);

        if (node.getKey().startsWith(prefix)) {
            results.addAll(node.getAllContacts());
        }

        searchByPrefixHelper(node.getRight(), prefix, results);
    }

    /**
     * 更新联系人信息
     */
    public boolean update(String name, String phone, Contact newContact) {
        List<Contact> contacts = search(name);
        for (Contact c : contacts) {
            if (c.getPhone().equals(phone)) {
                c.setPhone(newContact.getPhone());
                c.setEmail(newContact.getEmail());
                c.setAddress(newContact.getAddress());
                return true;
            }
        }
        return false;
    }

    /**
     * 中序遍历（升序输出）
     */
    public void inorderTraversal() {
        System.out.println("\n=== 中序遍历（升序） ===");
        inorderHelper(root);
    }

    private void inorderHelper(BSTNode node) {
        if (node != null) {
            inorderHelper(node.getLeft());
            for (Contact c : node.getAllContacts()) {
                System.out.println(c);
            }
            inorderHelper(node.getRight());
        }
    }

    /**
     * 前序遍历
     */
    public void preorderTraversal() {
        System.out.println("\n=== 前序遍历 ===");
        preorderHelper(root);
    }

    private void preorderHelper(BSTNode node) {
        if (node != null) {
            for (Contact c : node.getAllContacts()) {
                System.out.println(c);
            }
            preorderHelper(node.getLeft());
            preorderHelper(node.getRight());
        }
    }

    /**
     * 后序遍历
     */
    public void postorderTraversal() {
        System.out.println("\n=== 后序遍历 ===");
        postorderHelper(root);
    }

    private void postorderHelper(BSTNode node) {
        if (node != null) {
            postorderHelper(node.getLeft());
            postorderHelper(node.getRight());
            for (Contact c : node.getAllContacts()) {
                System.out.println(c);
            }
        }
    }

    /**
     * 获取前序遍历联系人列表
     */
    public List<Contact> getPreorderContacts() {
        List<Contact> contacts = new ArrayList<>();
        getPreorderContactsHelper(root, contacts);
        return contacts;
    }

    private void getPreorderContactsHelper(BSTNode node, List<Contact> contacts) {
        if (node != null) {
            contacts.addAll(node.getAllContacts());
            getPreorderContactsHelper(node.getLeft(), contacts);
            getPreorderContactsHelper(node.getRight(), contacts);
        }
    }

    /**
     * 获取后序遍历联系人列表
     */
    public List<Contact> getPostorderContacts() {
        List<Contact> contacts = new ArrayList<>();
        getPostorderContactsHelper(root, contacts);
        return contacts;
    }

    private void getPostorderContactsHelper(BSTNode node, List<Contact> contacts) {
        if (node != null) {
            getPostorderContactsHelper(node.getLeft(), contacts);
            getPostorderContactsHelper(node.getRight(), contacts);
            contacts.addAll(node.getAllContacts());
        }
    }

    /**
     * 获取树中所有联系人
     */
    public List<Contact> getAllContacts() {
        List<Contact> allContacts = new ArrayList<>();
        getAllContactsHelper(root, allContacts);
        return allContacts;
    }

    private void getAllContactsHelper(BSTNode node, List<Contact> allContacts) {
        if (node != null) {
            getAllContactsHelper(node.getLeft(), allContacts);
            allContacts.addAll(node.getAllContacts());
            getAllContactsHelper(node.getRight(), allContacts);
        }
    }

    /**
     * 判断树是否为空
     */
    public boolean isEmpty() {
        return root == null;
    }
}
