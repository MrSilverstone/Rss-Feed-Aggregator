package com.example.thomas.app_rss_feed_agregator.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;

    public Category(String n) {
        name = n;
    }

    public String name() {
        return name;
    }
}
