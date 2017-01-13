package com.epitech.model;

import java.util.List;

public class Group {

    private String name;
    private List<Feed> feeds;

    public Group() {}

    public Group(String name, List<Feed> feeds) {
        this.name = name;
        this.feeds = feeds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }
}
