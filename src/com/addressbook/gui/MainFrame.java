package com.addressbook.gui;

import com.addressbook.service.AddressBookService;

import javax.swing.*;
import java.awt.*;

/**
 * 主窗口框架
 * 集成所有功能面板
 */
public class MainFrame extends JFrame {
    private AddressBookService service;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private ContactListPanel listPanel;
    private ContactFormPanel formPanel;
    private TraversalPanel traversalPanel;

    public MainFrame() {
        service = new AddressBookService();
        initTestData(); // 初始化一些数据方便演示

        initUI();
    }

    private void initUI() {
        setTitle("通讯录管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        // 设置外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 主布局
        setLayout(new BorderLayout());

        // 侧边栏
        SidebarPanel sidebar = new SidebarPanel(e -> handleNavigation(e.getActionCommand()));
        add(sidebar, BorderLayout.WEST);

        // 内容区域
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(44, 62, 80)); // 与侧边栏同色
        contentPanel.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10)); // 增加外边距

        // 初始化各个面板
        listPanel = new ContactListPanel(service);
        listPanel.setOnEditRequest(contact -> {
            formPanel.setContact(contact);
            cardLayout.show(contentPanel, "form");
        });

        formPanel = new ContactFormPanel(service);
        formPanel.setOnSaveSuccess(() -> {
            listPanel.refreshData();
            cardLayout.show(contentPanel, "list");
        });
        formPanel.setOnCancel(() -> {
            cardLayout.show(contentPanel, "list");
        });

        traversalPanel = new TraversalPanel(service);

        // 添加到卡片布局
        contentPanel.add(listPanel, "list");
        contentPanel.add(formPanel, "form");
        contentPanel.add(traversalPanel, "traversal");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void handleNavigation(String command) {
        switch (command) {
            case "list":
                listPanel.refreshData();
                cardLayout.show(contentPanel, "list");
                break;
            case "add":
                formPanel.clearForm();
                cardLayout.show(contentPanel, "form");
                break;
            case "traversal":
                cardLayout.show(contentPanel, "traversal");
                break;
        }
    }

    private void initTestData() {
        service.addContact("张三", "13800138001", "zhangsan@email.com", "北京市朝阳区");
        service.addContact("李四", "13800138002", "lisi@email.com", "上海市浦东新区");
        service.addContact("王五", "13800138003", "wangwu@email.com", "广州市天河区");
        service.addContact("赵六", "13800138004", "zhaoliu@email.com", "深圳市南山区");
        service.addContact("张伟", "13800138005", "zhangwei@email.com", "杭州市西湖区");
        service.addContact("张丽", "13800138006", "zhangli@email.com", "成都市武侯区");
        service.addContact("陈晨", "13800138007", "chenchen@email.com", "南京市鼓楼区");
        service.addContact("刘洋", "13800138008", "liuyang@email.com", "武汉市江汉区");
        service.addContact("周杰", "13800138009", "zhoujie@email.com", "重庆市渝北区");
        service.addContact("吴彦", "13800138010", "wuyan@email.com", "西安市雁塔区");
        service.addContact("郑强", "13800138011", "zhengqiang@email.com", "郑州市金水区");
        service.addContact("孙悦", "13800138012", "sunyue@email.com", "天津市和平区");
    }
}
