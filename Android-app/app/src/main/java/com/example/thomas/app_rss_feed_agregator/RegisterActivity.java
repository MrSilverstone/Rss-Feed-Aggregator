package com.example.thomas.app_rss_feed_agregator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.FuelManager;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;
import com.loopj.android.http.*;

import com.example.thomas.app_rss_feed_agregator.models.DataSet;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import kotlin.Pair;

public class RegisterActivity extends AppCompatActivity {

    AppCompatButton b1, b2;
    EditText ed1,ed2;
    private TextView resultText;

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataSet.create();
        setContentView(R.layout.activity_register);
        b1=(AppCompatButton) findViewById(R.id.button);
        b2=(AppCompatButton) findViewById(R.id.button2);
        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText2);
//        ed1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        ed2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        ed1.setBackgroundColor(Color.parseColor("#D3D3D3"));
        ed2.setBackgroundColor(Color.parseColor("#D3D3D3"));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSet.credentials().add(ed1.getText().toString());
                DataSet.credentials().add(ed2.getText().toString());
                register();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });
    }

    class LoginRequestBody {

        private String username;
        private String password;

        LoginRequestBody(String name, String pass) {
            username = name;
            password = pass;
        }

        public String name() {
            return username;
        }

    }

    public void register() {
        String tag_json_obj = "json_obj_req";

        LoginRequestBody body = new LoginRequestBody(DataSet.credentials().get(0), DataSet.credentials().get(1));
        String url = "http://louismondesir.me:8080/aggregator/auth/register";
        Gson gson = new Gson();
        JSONObject jsonBody = new JSONObject();
        final String requestBody = gson.toJson(body);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,url, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        System.out.println(ed1.getText().toString());
                        System.out.println(ed2.getText().toString());
                        System.out.println(response.toString());
                        Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                System.out.println(ed1.getText().toString());
                System.out.println(ed2.getText().toString());
                System.out.println(error.toString());
                Toast.makeText(getApplicationContext(), "Account creation failed", Toast.LENGTH_SHORT).show();
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

        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
