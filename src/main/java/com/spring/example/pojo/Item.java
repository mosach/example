package com.spring.example.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "isRequired",
        "inputType",
        "title",
        "validators"
})
public class Item {

    @JsonProperty("name")
    private String name;
    @JsonProperty("isRequired")
    private Boolean isRequired;
    @JsonProperty("inputType")
    private String inputType;
    @JsonProperty("title")
    private String title;
    @JsonProperty("validators")
    private List<Validator> validators = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("isRequired")
    public Boolean getIsRequired() {
        return isRequired;
    }

    @JsonProperty("isRequired")
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @JsonProperty("inputType")
    public String getInputType() {
        return inputType;
    }

    @JsonProperty("inputType")
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("validators")
    public List<Validator> getValidators() {
        return validators;
    }

    @JsonProperty("validators")
    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

}