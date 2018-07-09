package com.spring.example.repository;

import com.spring.example.entity.FormEntity;
import com.spring.example.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface FormEntityRepository extends CrudRepository<FormEntity,Long> {

    FormEntity findByUserId(Long userId);
}
