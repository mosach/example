package com.spring.example.repository;

import com.spring.example.entity.User;
import com.spring.example.entity.UserFormId;
import com.spring.example.entity.UserForms;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserFormRepository extends CrudRepository<UserForms,UserFormId> {
    List<UserForms> findByUserFormIdUserId(Integer userId);
}
