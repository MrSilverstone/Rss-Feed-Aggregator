package com.epitech.model;

import java.util.List;

public class Feed {
    private String url;
    private List<String> readItems;

    public Feed() {
    }

    public Feed(String url, List<String> readItems) {
        this.url = url;
        this.readItems = readItems;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getReadItems() {
        return readItems;
    }

    public void setReadItems(List<String> readItems) {
        this.readItems = readItems;
    }
}