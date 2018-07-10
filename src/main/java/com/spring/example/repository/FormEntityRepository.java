package com.spring.example.repository;

import com.spring.example.entity.Form1;
import org.springframework.data.repository.CrudRepository;

public interface FormEntityRepository extends CrudRepository<Form1,Long> {

    Form1 findByUserId(Long userId);
}
