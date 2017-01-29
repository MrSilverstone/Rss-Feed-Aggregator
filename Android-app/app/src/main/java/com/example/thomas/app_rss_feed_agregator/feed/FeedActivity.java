package com.example.thomas.app_rss_feed_agregator.feed;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.thomas.app_rss_feed_agregator.AppController;
import com.example.thomas.app_rss_feed_agregator.LoginActivity;
import com.example.thomas.app_rss_feed_agregator.R;
import com.example.thomas.app_rss_feed_agregator.category.CategoryActivity;
import com.example.thomas.app_rss_feed_agregator.category.CategoryAdapter;
import com.example.thomas.app_rss_feed_agregator.category.CategoryHandler;
import com.example.thomas.app_rss_feed_agregator.menu.MenuAdapter;
import com.example.thomas.app_rss_feed_agregator.models.Category;
import com.example.thomas.app_rss_feed_agregator.models.DataSet;
import com.example.thomas.app_rss_feed_agregator.models.Feed;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView feedList;
    private FeedAdapter feedAdapter;

    private EditText et;
    private AppCompatButton eb;
    private AppCompatButton ab;
    private AppCompatButton cab;
    private AppCompatButton ceb;
    private String newname;
    private Category tmp;
    private TextView Retxt;
    private TextView Addtxt;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager menuLayoutManager;

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("Categories");


        et = (EditText)findViewById(R.id.editText);
        eb = (AppCompatButton) findViewById(R.id.button);
        ab = (AppCompatButton) findViewById(R.id.addbutton);
        ceb = (AppCompatButton) findViewById(R.id.cbutton);
        cab = (AppCompatButton) findViewById(R.id.cabutton);
        Retxt = (TextView) findViewById(R.id.Retext);
        Addtxt = (TextView) findViewById(R.id.Addtext);


        //add the categories to the dataset

        feedList = (RecyclerView) findViewById(R.id.feedList);
        feedAdapter = new FeedAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        feedList.setLayoutManager(mLayoutManager);

        feedList.setAdapter(feedAdapter);


        eb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newname = et.getText().toString();
                newname = null;
                tmp = null;
            }
        });
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newname = et.getText().toString();
                Add();
                newname = null;
                tmp = null;
            }
        });
        ceb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eb.setVisibility(View.GONE);
                ceb.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                Retxt.setVisibility(View.GONE);
                feedList.setVisibility(View.VISIBLE);
                newname = null;
                tmp = null;
            }
        });
        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ab.setVisibility(View.GONE);
                cab.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                Addtxt.setVisibility(View.GONE);feedList.setVisibility(View.VISIBLE);
                newname = null;
                tmp = null;
            }
        });
    }




    public void remove(Feed feed) {
    }

    public void Add() {
        if (newname == null)
            return;
        if (newname.isEmpty()) {
            Toast.makeText(FeedActivity.this, "Name must not be empty", Toast.LENGTH_LONG).show();
            return;
        }
        for(int i = 0; i < DataSet.feeds().size(); i++) {
            int key = DataSet.feeds().keyAt(i);
            // get the object by the key.
            Feed obj = DataSet.feeds().get(key);
            if (newname.equals(obj.name())){
                Toast.makeText(FeedActivity.this, "Name already taken", Toast.LENGTH_LONG).show();
                return;
            }
        }

        ab.setVisibility(View.GONE);
        cab.setVisibility(View.GONE);
        et.setVisibility(View.GONE);
        Addtxt.setVisibility(View.GONE);
        feedList.setVisibility(View.VISIBLE);
        newname = null;
        tmp = null;

    }


    public void displayAdd () {
        et.setVisibility(View.VISIBLE);
        ab.setVisibility(View.VISIBLE);
        cab.setVisibility(View.VISIBLE);
        Addtxt.setVisibility(View.VISIBLE);
        feedList.setVisibility(View.GONE);

    }

    class GroupsRequestBody {

        private String username;
        private String password;

        GroupsRequestBody(String name, String pass) {
            username = name;
            password = pass;
        }

        public String name() {
            return username;
        }

    }

    public void retrieveCategories()
    {
        String tag_json_obj = "json_obj_req";

        GroupsRequestBody body = new GroupsRequestBody(DataSet.credentials().get(0), DataSet.credentials().get(1));
        String url = "http://louismondesir.me:8080/aggregator/api/groups";
        Gson gson = new Gson();
        JSONObject jsonBody = new JSONObject();
        final String requestBody = gson.toJson(body);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(com.android.volley.Request.Method.GET,url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            CategoryHandler fillCat = new CategoryHandler(response, getApplicationContext());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        launchFeedHandler(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                System.out.println(error.toString());
            }
        }) {



            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + DataSet.credentials().get(2));
                return headers;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public void launchFeedHandler(JSONArray response) {
        FeedsHandler fillCat = new FeedsHandler(response, this);
        Intent in = new Intent(this, CategoryActivity.class);
        startActivity(in);
    }

    public void launchFeedDetails(Feed f) {
        Intent in = new Intent(this, FeedDetailsActivity.class);
        in.putExtra("feed", f.content());
        startActivity(in);
    }


    @Override
    protected void onResume() {
        super.onResume();
        feedAdapter.notifyDataSetChanged();
    }

    public void notifyCategoryChange() {
        feedAdapter.notifyDataSetChanged();
    }
}
