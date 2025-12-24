package com.addressbook.model;

/**
 * 联系人实体类
 * 存储联系人的基本信息：姓名、电话、邮箱、地址
 */
public class Contact {
    private String name;      // 姓名（作为排序关键字）
    private String phone;     // 电话
    private String email;     // 邮箱
    private String address;   // 地址

    /**
     * 构造函数
     */
    public Contact(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "联系人{" +
                "姓名='" + name + '\'' +
                ", 电话='" + phone + '\'' +
                ", 邮箱='" + email + '\'' +
                ", 地址='" + address + '\'' +
                '}';
    }
}
