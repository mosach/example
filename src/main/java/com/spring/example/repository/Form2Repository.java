package com.spring.example.repository;

import com.spring.example.entity.Form2;
import org.springframework.data.repository.CrudRepository;

public interface Form2Repository  extends CrudRepository<Form2,Long> {

    Form2 findByUserId(Long userId);
}
