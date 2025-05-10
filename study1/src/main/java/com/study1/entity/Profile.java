package com.study1.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_information")
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;  // 必须存在主键字段

    private String name;     // 登录用户的用户名
    private int age;
    private String sex;
    private String phone;
    private String address;
    private String email;
    private String headshot;
}
