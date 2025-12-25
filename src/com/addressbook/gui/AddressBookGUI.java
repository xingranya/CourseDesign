package com.addressbook.gui;

import javax.swing.SwingUtilities;

/**
 * 通讯录管理系统GUI入口
 */
public class AddressBookGUI {
    /**
     * 主方法
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
