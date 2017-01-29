package com.example.thomas.app_rss_feed_agregator.models;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataSet {
    private static SparseArray<Category> categories;
    private static SparseArray<Feed> feeds;
    private static HashMap<Category, HashSet<Feed>> data;
    private static List<String> credentials;

    public static void create() {
        categories = new SparseArray<>();
        feeds = new SparseArray<>();
        data = new HashMap<>();
        credentials = new ArrayList<>();
    }

    public static SparseArray<Category> categories() {
        return categories;
    }

    public static SparseArray<Feed> feeds() {
        return feeds;
    }

    public static HashMap<Category, HashSet<Feed>> data() {
        return data;
    }

    public static List<String> credentials() { return credentials; }
}