package com.spring.example.entity;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String name;
    @ManyToOne
    @JoinColumn(name="admin_user_id")
    private AdminUser adminUser;

    public User() {
    }

    public User(String email, String password, String phone, String name, AdminUser adminUser) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.adminUser = adminUser;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public Long getAdminUserId() {
        return adminUser.getId();
    }
}
