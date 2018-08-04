package com.spring.example.repository;

import com.spring.example.entity.Documents;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Documents,Integer> {
}
