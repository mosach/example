package com.spring.example.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Form7 {
    @Id
    private Long userId;
    @ElementCollection
    @MapKeyColumn(name="question")
    @Column(name="answer")
    Map<String, String> myMap = new HashMap<String, String>();
    @Basic(optional = false)
    @Column(name = "updated_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Form7() {
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

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
