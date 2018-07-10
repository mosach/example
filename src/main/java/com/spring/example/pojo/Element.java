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
        "items",
        "isRequired",
        "choices",
        "visibleIf",
        "enableIf",
        "inputType",
        "templateElements"
})
public class Element {

    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("title")
    private String title;
    @JsonProperty("items")
    private List<Item> items = null;
    @JsonProperty("isRequired")
    private Boolean isRequired;
    @JsonProperty("choices")
    private List<String> choices = null;
    @JsonProperty("visibleIf")
    private String visibleIf;
    @JsonProperty("enableIf")
    private String enableIf;
    @JsonProperty("inputType")
    private String inputType;
    @JsonProperty("templateElements")
    private List<TemplateElement> templateElements = null;

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

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @JsonProperty("isRequired")
    public Boolean getIsRequired() {
        return isRequired;
    }

    @JsonProperty("isRequired")
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @JsonProperty("choices")
    public List<String> getChoices() {
        return choices;
    }

    @JsonProperty("choices")
    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    @JsonProperty("visibleIf")
    public String getVisibleIf() {
        return visibleIf;
    }

    @JsonProperty("visibleIf")
    public void setVisibleIf(String visibleIf) {
        this.visibleIf = visibleIf;
    }

    @JsonProperty("enableIf")
    public String getEnableIf() {
        return enableIf;
    }

    @JsonProperty("enableIf")
    public void setEnableIf(String enableIf) {
        this.enableIf = enableIf;
    }

    @JsonProperty("inputType")
    public String getInputType() {
        return inputType;
    }

    @JsonProperty("inputType")
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    @JsonProperty("templateElements")
    public List<TemplateElement> getTemplateElements() {
        return templateElements;
    }

    @JsonProperty("templateElements")
    public void setTemplateElements(List<TemplateElement> templateElements) {
        this.templateElements = templateElements;
    }

}
