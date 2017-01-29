package com.example.thomas.app_rss_feed_agregator.models;

import android.util.SparseArray;

import com.example.thomas.app_rss_feed_agregator.feed.FeedMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Feed implements Serializable {

    String name;
    private List<FeedMessage> items;

    public Feed (String newName, JSONArray itemsArray) {

        name = newName;
        this.items = new ArrayList<>();
        try {
            if (itemsArray != null){
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.getJSONObject(i);
                    FeedMessage message = new FeedMessage(itemObj.getString("title"), itemObj.getString("description"), itemObj.getString("link"), itemObj.getString("author"), itemObj.getString("guid"));
                    items.add(message);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String name() {
        return name;
    }

    public List<FeedMessage> items() {
        return items;
    }

    public String content() {
        String s = "";
        for (int i = 0; i < items.size(); i++) {
            FeedMessage f = items.get(i);
                s += f.toString();
        }
        return s;
    }
}
