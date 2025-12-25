package com.addressbook.test;

import com.addressbook.model.Contact;
import com.addressbook.service.AddressBookService;
import java.util.List;

/**
 * 简单的功能测试类
 * 模拟单元测试验证核心业务逻辑
 */
public class AddressBookTest {
    public static void main(String[] args) {
        System.out.println("开始执行业务逻辑测试...");

        AddressBookService service = new AddressBookService();

        // 测试1: 添加联系人
        testAdd(service);

        // 测试2: 查找联系人
        testSearch(service);

        // 测试3: 更新联系人
        testUpdate(service);

        // 测试4: 删除联系人
        testDelete(service);

        // 测试5: 遍历顺序
        testTraversal(service);

        System.out.println("所有测试执行完毕!");
    }

    private static void testAdd(AddressBookService service) {
        System.out.print("测试添加联系人... ");
        service.addContact("TestUser", "12345678901", "test@test.com", "Test Address");
        if (service.getContactCount() == 1) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }

    private static void testSearch(AddressBookService service) {
        System.out.print("测试精确查找... ");
        List<Contact> results = service.findContact("TestUser");
        if (!results.isEmpty() && results.get(0).getPhone().equals("12345678901")) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }

        System.out.print("测试前缀查找... ");
        results = service.findByPrefix("Test");
        if (!results.isEmpty()) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }

    private static void testUpdate(AddressBookService service) {
        System.out.print("测试更新联系人... ");
        service.updateContact("TestUser", "12345678901", "12345678901", "new@test.com", "New Address");
        List<Contact> results = service.findContact("TestUser");
        if (!results.isEmpty() && results.get(0).getEmail().equals("new@test.com")) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }

    private static void testDelete(AddressBookService service) {
        System.out.print("测试删除联系人... ");
        service.deleteContact("TestUser", "12345678901");
        if (service.getContactCount() == 0) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }

    private static void testTraversal(AddressBookService service) {
        System.out.print("测试遍历功能... ");
        service.addContact("B", "2", "b@b.com", "b");
        service.addContact("A", "1", "a@a.com", "a");
        service.addContact("C", "3", "c@c.com", "c");

        List<Contact> inorder = service.getAllContacts();
        if (inorder.get(0).getName().equals("A") &&
                inorder.get(1).getName().equals("B") &&
                inorder.get(2).getName().equals("C")) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }
}
