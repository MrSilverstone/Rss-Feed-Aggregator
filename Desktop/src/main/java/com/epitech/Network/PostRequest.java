package com.epitech.Network;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by loulo on 13/01/2017.
 */
public class PostRequest {

    private final String USER_AGENT = "Mozilla/5.0";

    private String response = "";

    public boolean send(String url, String json, String token) {

        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (!token.isEmpty()) {
                con.setRequestProperty("Authorization", "Bearer " + token);
            }

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            if (!json.isEmpty())
                wr.write(json.getBytes());

            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();


            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();

            response = sb.toString();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> T getResponse(Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        T obj;

        try {
            obj = mapper.readValue(response, valueType);
            return obj;
        } catch (IOException e) {
            return null;
        }
    }
}