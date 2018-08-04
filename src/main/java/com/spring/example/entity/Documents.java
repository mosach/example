package com.spring.example.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;
    @Basic(optional = false)
    @Column(name = "updated_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Documents() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
