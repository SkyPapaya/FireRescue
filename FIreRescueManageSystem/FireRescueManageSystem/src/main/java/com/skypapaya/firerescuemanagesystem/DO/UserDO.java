package com.skypapaya.firerescuemanagesystem.DO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDO {
    Long id;
    String name;
    int age;
    String eMail;
    String phone;
    String sex;
    String authority;
    String address;
    String password;
    LocalDateTime gmtCreated;
    LocalDateTime gmtModified;

}
