package com.spring.example.repository;

import com.spring.example.entity.AdminUser;
import org.springframework.data.repository.CrudRepository;

public interface AdminUserRepository extends CrudRepository<AdminUser,Long> {
    AdminUser findByEmail(String email);
}
