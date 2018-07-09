package com.spring.example.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

public class FormData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "type",
            "name",
            "title",
            "defaultValue",
            "isRequired",
            "items"
    })
    @JsonProperty("question1")
    private String question1;
    @JsonProperty("question2")
    private Map<String,String> question2;
    @JsonProperty("question3")
    private Map<String,String> question3;
    @JsonProperty("question4")
    private Map<String,String> question4;
    @JsonProperty("question5")
    private Map<String,String> question5;

    public FormData() {
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public Map<String, String> getQuestion2() {
        return question2;
    }

    public void setQuestion2(Map<String, String> question2) {
        this.question2 = question2;
    }

    public Map<String, String> getQuestion3() {
        return question3;
    }

    public void setQuestion3(Map<String, String> question3) {
        this.question3 = question3;
    }

    public Map<String, String> getQuestion4() {
        return question4;
    }

    public void setQuestion4(Map<String, String> question4) {
        this.question4 = question4;
    }

    public Map<String, String> getQuestion5() {
        return question5;
    }

    public void setQuestion5(Map<String, String> question5) {
        this.question5 = question5;
    }
}
