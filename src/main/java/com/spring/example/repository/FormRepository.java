package com.spring.example.repository;

import com.spring.example.entity.Form;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormRepository extends CrudRepository<Form,Integer> {
}
