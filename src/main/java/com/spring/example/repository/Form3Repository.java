package com.spring.example.repository;

import com.spring.example.entity.Form3;
import org.springframework.data.repository.CrudRepository;

public interface Form3Repository  extends CrudRepository<Form3,Long> {

    Form3 findByUserId(Long userId);
}
