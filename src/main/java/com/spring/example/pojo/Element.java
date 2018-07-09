package com.spring.example.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "name",
        "title",
        "defaultValue",
        "isRequired",
        "items"
})
public class Element {

    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("title")
    private String title;
    @JsonProperty("defaultValue")
    private String defaultValue;
    @JsonProperty("isRequired")
    private Boolean isRequired;
    @JsonProperty("items")
    private List<Item> items = null;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("defaultValue")
    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonProperty("defaultValue")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @JsonProperty("isRequired")
    public Boolean getIsRequired() {
        return isRequired;
    }

    @JsonProperty("isRequired")
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

}
