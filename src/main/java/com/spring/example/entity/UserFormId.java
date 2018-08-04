package com.spring.example.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFormId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "form_id")
    private Integer formId;

    public UserFormId(Integer userId, Integer formId) {
        this.userId = userId;
        this.formId = formId;
    }

    public UserFormId() {
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFormId that = (UserFormId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(formId, that.formId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, formId);
    }
}
