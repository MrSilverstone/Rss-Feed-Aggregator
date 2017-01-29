package com.example.thomas.app_rss_feed_agregator.feed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.thomas.app_rss_feed_agregator.R;

public class FeedDetailsActivity extends AppCompatActivity {
    private WebView wv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);

        wv1=(WebView)findViewById(R.id.webView);




        Intent myIntent = getIntent(); // gets the previously created intent
        final String feed = myIntent.getExtras().getString("feed");
        wv1.loadData(feed, "text/html; charset=UTF-8", "utf-8");
    }

}
