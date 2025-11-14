package org.example.testdemo.entity;
import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String avatar;
    private String account;
    private String password;
    private String email;
    private String profile;
    private java.util.Date createTime;
    private java.util.Date updateTime;
}
