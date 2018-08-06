package com.spring.example.repository;

import com.spring.example.entity.UserRecords;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRecordsRepository extends CrudRepository<UserRecords,Long> {

    List<UserRecords> findAllByUserIdAndFormId(Long userId, Long formId);
    UserRecords findFirstByUserIdAndFormIdOrderByIdDesc(Long userId, Long formId);
    List<UserRecords> findDistinctFormIdByUserId(Long userId);
}
