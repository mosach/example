package com.spring.example.repository;

import com.spring.example.entity.Form7;
import org.springframework.data.repository.CrudRepository;

public interface Form7Repository extends CrudRepository<Form7,Long> {

    Form7 findByUserId(Long userId);
}
