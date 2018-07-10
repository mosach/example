package com.spring.example.models;

import java.util.Date;

public class FormLink {

    private Integer id;

    private Date date;

    private String status;

    private String type;

    public FormLink(Integer id, Date date, String status, String type) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
