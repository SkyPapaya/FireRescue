package com.skypapaya.firerescuemanagesystem.DO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDO {
    Long id;
    String name;
    int age;
    String e_mail;
    String phone;
    String sex;
    String authority;
    String address;
    String password;
    LocalDateTime gmtCreated;
    LocalDateTime gmtModified;

    public UserDO() {}

    public UserDO(String name, String e_mail, String phone, String sex, String password, int age, String address) {
        this.name = name;
        this.e_mail = e_mail;
        this.phone = phone;
        this.sex = sex;
        this.password = password;
        this.age = age;
        this.address = address;
    }

    public String toString() {
        return "UserDO{" +"id=" + id + ", name='" + name + '\'' + ", age=" + age + ", eMail='" + e_mail + '\'' + ", phone='" + phone + '\'' + ", sex='" + sex + '\'' + ", authority='" + authority + '\'' + ", address='" + address + '\'' + ", password='" + password + '\'' + "}";
    }

}
