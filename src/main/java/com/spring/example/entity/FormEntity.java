package com.spring.example.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class FormEntity {
    @Id
    private Long userId;
    @ElementCollection
    @MapKeyColumn(name="question")
    @Column(name="answer")
    Map<String, String> myMap = new HashMap<String, String>();

    public FormEntity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<String, String> getMyMap() {
        return new HashMap<>(myMap);
    }

    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }
}
