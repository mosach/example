package com.spring.example.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class UserForms {

    @EmbeddedId
    private UserFormId userFormId;
    @ElementCollection
    @MapKeyColumn(name="question")
    @Column(name="answer")
    private Map<String, String> myMap = new HashMap<String, String>();

    @Basic(optional = false)
    @Column(name = "updated_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
//
//    @ManyToOne
//    @JoinColumn(name="form_id")
//    private Form form;

    public UserForms() {
    }

    public UserFormId getUserFormId() {
        return userFormId;
    }

    public void setUserFormId(UserFormId userFormId) {
        this.userFormId = userFormId;
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

//    public Form getForm() {
//        return form;
//    }
}
