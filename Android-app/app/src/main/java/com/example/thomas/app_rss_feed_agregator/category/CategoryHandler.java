package com.example.thomas.app_rss_feed_agregator.category;

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

public class CategoryHandler {

    Context mcontext;

    public CategoryHandler(JSONArray itemsArray, Context context)
    {
        this.mcontext = context;
        try {
            if (itemsArray != null){
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.getJSONObject(i);
                    Category c = new Category(itemObj.getString("name"));
                    DataSet.categories().append(i, c);
                    DataSet.data().put(c, new HashSet<Feed>());
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
