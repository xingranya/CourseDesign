package com.addressbook;

import com.addressbook.service.AddressBookService;
import java.util.Scanner;

/**
 * 通讯录管理系统主程序
 * 基于AVL平衡二叉搜索树实现
 */
public class Main {
    private static AddressBookService service = new AddressBookService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 添加一些测试数据
        initTestData();

        // 显示欢迎信息
        System.out.println("==========================================");
        System.out.println("   基于AVL树的通讯录管理系统");
        System.out.println("==========================================");

        // 主菜单循环
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addContactMenu();
                    break;
                case "2":
                    deleteContactMenu();
                    break;
                case "3":
                    updateContactMenu();
                    break;
                case "4":
                    searchContactMenu();
                    break;
                case "5":
                    searchByPrefixMenu();
                    break;
                case "6":
                    displayAllMenu();
                    break;
                case "7":
                    traversalMenu();
                    break;
                case "8":
                    testAllFunctions();
                    break;
                case "0":
                    running = false;
                    System.out.println("\n感谢使用，再见！");
                    break;
                default:
                    System.out.println("[错误] 无效选项，请重新选择");
            }

            if (running && !choice.equals("8")) {
                System.out.println("\n按回车键继续...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    /**
     * 显示主菜单
     */
    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("              主菜单");
        System.out.println("========================================");
        System.out.println("  1. 添加联系人");
        System.out.println("  2. 删除联系人");
        System.out.println("  3. 修改联系人");
        System.out.println("  4. 精确查找联系人");
        System.out.println("  5. 前缀模糊查找");
        System.out.println("  6. 显示所有联系人");
        System.out.println("  7. 遍历操作（前中后序）");
        System.out.println("  8. 运行完整功能测试");
        System.out.println("  0. 退出系统");
        System.out.println("========================================");
        System.out.print("请选择操作: ");
    }

    /**
     * 添加联系人菜单
     */
    private static void addContactMenu() {
        System.out.println("\n=== 添加联系人 ===");
        System.out.print("姓名: ");
        String name = scanner.nextLine().trim();
        System.out.print("电话: ");
        String phone = scanner.nextLine().trim();
        System.out.print("邮箱: ");
        String email = scanner.nextLine().trim();
        System.out.print("地址: ");
        String address = scanner.nextLine().trim();

        service.addContact(name, phone, email, address);
    }

    /**
     * 删除联系人菜单
     */
    private static void deleteContactMenu() {
        System.out.println("\n=== 删除联系人 ===");
        System.out.print("姓名: ");
        String name = scanner.nextLine().trim();
        System.out.print("电话（留空删除所有同名联系人）: ");
        String phone = scanner.nextLine().trim();

        if (phone.isEmpty()) {
            service.deleteAllByName(name);
        } else {
            service.deleteContact(name, phone);
        }
    }

    /**
     * 更新联系人菜单
     */
    private static void updateContactMenu() {
        System.out.println("\n=== 修改联系人 ===");
        System.out.print("姓名: ");
        String name = scanner.nextLine().trim();
        System.out.print("原电话号码: ");
        String phone = scanner.nextLine().trim();
        System.out.print("新电话号码: ");
        String newPhone = scanner.nextLine().trim();
        System.out.print("新邮箱: ");
        String newEmail = scanner.nextLine().trim();
        System.out.print("新地址: ");
        String newAddress = scanner.nextLine().trim();

        service.updateContact(name, phone, newPhone, newEmail, newAddress);
    }

    /**
     * 精确查找菜单
     */
    private static void searchContactMenu() {
        System.out.println("\n=== 精确查找联系人 ===");
        System.out.print("请输入姓名: ");
        String name = scanner.nextLine().trim();
        service.findContact(name);
    }

    /**
     * 前缀模糊查找菜单
     */
    private static void searchByPrefixMenu() {
        System.out.println("\n=== 前缀模糊查找 ===");
        System.out.print("请输入姓名前缀: ");
        String prefix = scanner.nextLine().trim();
        service.findByPrefix(prefix);
    }

    /**
     * 显示所有联系人菜单
     */
    private static void displayAllMenu() {
        System.out.println("\n=== 所有联系人（按姓名排序） ===");
        service.displayAllContacts();
        System.out.println("\n总计: " + service.getContactCount() + " 个联系人");
    }

    /**
     * 遍历操作菜单
     */
    private static void traversalMenu() {
        System.out.println("\n=== 遍历操作 ===");
        System.out.println("1. 中序遍历（升序）");
        System.out.println("2. 前序遍历");
        System.out.println("3. 后序遍历");
        System.out.print("请选择: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                service.inorderTraversal();
                break;
            case "2":
                service.preorderTraversal();
                break;
            case "3":
                service.postorderTraversal();
                break;
            default:
                System.out.println("[错误] 无效选项");
        }
    }

    /**
     * 初始化测试数据
     */
    private static void initTestData() {
        System.out.println("\n正在初始化测试数据...");
        service.addContact("张三", "13800138001", "zhangsan@email.com", "北京市朝阳区");
        service.addContact("李四", "13800138002", "lisi@email.com", "上海市浦东新区");
        service.addContact("王五", "13800138003", "wangwu@email.com", "广州市天河区");
        service.addContact("赵六", "13800138004", "zhaoliu@email.com", "深圳市南山区");
        service.addContact("张伟", "13800138005", "zhangwei@email.com", "杭州市西湖区");
        service.addContact("张丽", "13800138006", "zhangli@email.com", "成都市武侯区");
        service.addContact("张三", "13900139001", "zhangsan2@email.com", "北京市海淀区");  // 重名测试
        System.out.println("测试数据初始化完成！\n");
    }

    /**
     * 完整功能测试
     */
    private static void testAllFunctions() {
        System.out.println("\n==========================================");
        System.out.println("        开始完整功能测试");
        System.out.println("==========================================");

        // 测试1: 显示所有联系人
        System.out.println("\n【测试1】显示所有联系人:");
        service.displayAllContacts();

        // 测试2: 精确查找
        System.out.println("\n【测试2】精确查找 - 查找\"张三\":");
        service.findContact("张三");

        // 测试3: 前缀模糊查找
        System.out.println("\n【测试3】前缀模糊查找 - 查找所有姓\"张\"的联系人:");
        service.findByPrefix("张");

        // 测试4: 添加新联系人
        System.out.println("\n【测试4】添加新联系人 - 添加\"刘备\":");
        service.addContact("刘备", "13700137000", "liubei@email.com", "蜀国成都");

        // 测试5: 更新联系人
        System.out.println("\n【测试5】更新联系人 - 更新\"刘备\"的信息:");
        service.updateContact("刘备", "13700137000", "13700137001", "liubei_new@email.com", "蜀国新都");

        // 测试6: 验证更新
        System.out.println("\n【测试6】验证更新 - 查找\"刘备\":");
        service.findContact("刘备");

        // 测试7: 三种遍历方式
        System.out.println("\n【测试7】三种遍历方式:");
        service.inorderTraversal();
        service.preorderTraversal();
        service.postorderTraversal();

        // 测试8: 删除联系人（删除同名中的一个）
        System.out.println("\n【测试8】删除联系人 - 删除\"张三\"(13800138001):");
        service.deleteContact("张三", "13800138001");

        // 测试9: 验证删除后的查找
        System.out.println("\n【测试9】验证删除 - 查找\"张三\":");
        service.findContact("张三");

        // 测试10: 删除所有同名联系人
        System.out.println("\n【测试10】删除所有同名联系人 - 删除所有\"张三\":");
        service.deleteAllByName("张三");
        service.findContact("张三");

        // 测试11: 最终状态
        System.out.println("\n【测试11】最终通讯录状态:");
        service.displayAllContacts();

        System.out.println("\n==========================================");
        System.out.println("        功能测试完成");
        System.out.println("==========================================");

        System.out.println("\n按回车键返回主菜单...");
        scanner.nextLine();
    }
}
