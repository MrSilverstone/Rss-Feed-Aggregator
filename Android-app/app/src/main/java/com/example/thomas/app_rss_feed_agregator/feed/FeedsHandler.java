package com.example.thomas.app_rss_feed_agregator.feed;


import android.content.Context;
import android.content.Intent;

import com.example.thomas.app_rss_feed_agregator.LoginActivity;
import com.example.thomas.app_rss_feed_agregator.models.Category;
import com.example.thomas.app_rss_feed_agregator.models.DataSet;
import com.example.thomas.app_rss_feed_agregator.models.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class FeedsHandler {

    Context mcontext;

    public FeedsHandler(JSONArray itemsArray, Context context)
    {
        this.mcontext = context;
        try {
            if (itemsArray != null){
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.getJSONObject(i);
                    Category c = new Category(itemObj.getString("url"));
                    Feed f = new Feed(itemObj.getString("url"), itemObj.getJSONArray("feedMessages"));
                    DataSet.feeds().put(i, f);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if(mcontext instanceof LoginActivity){
            System.out.println("ACTIVITYSTARTOUIII");
            ((LoginActivity)mcontext).startCategoryActivity();

        }
    }
}
