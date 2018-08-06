
package com.spring.example.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
        "templateElements",
        "defaultValue",
        "correctAnswer"
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
    @JsonProperty("defaultValue")
    private String defaultValue = null;
    @JsonProperty("correctAnswer")
    private String correctAnswer;
    @JsonProperty("choices")
    private List<Choice> choices = null;
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

    @JsonProperty("defaultValue")
    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonProperty("defaultValue")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @JsonProperty("correctAnswer")
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @JsonProperty("correctAnswer")
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @JsonProperty("choices")
    public List<Choice> getChoices() {
        return choices;
    }

    @JsonProperty("choices")
    public void setChoices(List<Choice> choices) {
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
