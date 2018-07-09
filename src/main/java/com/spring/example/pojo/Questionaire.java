package com.spring.example.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pages",
        "completedHtml"
})
public class Questionaire {

    @JsonProperty("pages")
    private List<Page> pages = null;

    @JsonProperty("completedHtml")
    private String completedHtml = null;

    @JsonProperty("pages")
    public List<Page> getPages() {
        return pages;
    }

    @JsonProperty("pages")
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public String getCompletedHtml() {
        return completedHtml;
    }

    public void setCompletedHtml(String completedHtml) {
        this.completedHtml = completedHtml;
    }
}