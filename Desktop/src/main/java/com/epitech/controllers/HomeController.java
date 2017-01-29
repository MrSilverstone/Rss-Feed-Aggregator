package com.epitech.controllers;

import com.epitech.model.Feed;
import com.epitech.model.FeedMessage;
import com.epitech.model.Group;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class HomeController implements Initializable {
    @FXML
    private JFXListView<JFXListView> list1;

    private String token;

    private void getGroups() {

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

        asyncHttpClient.prepareGet("http://louismondesir.me:8080/aggregator/api/groups")
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        if (response.getStatusCode() != 200)
                            return null;

                        ObjectMapper mapper = new ObjectMapper();


                        Group[] groups = mapper.readValue(response.getResponseBody(), Group[].class);

                        if (groups.length == 0)
                            return response;

                        getNews(groups);

                        return response;
                    }
                });


    }

    private void getNews(Group[] groups) {


        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        asyncHttpClient.prepareGet("http://louismondesir.me:8080/aggregator/api/feeds/" + groups[0].getName())
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        if (response.getStatusCode() != 200)
                            return null;

                        System.out.println(response.getResponseBody());

                        ObjectMapper mapper = new ObjectMapper();
                        Feed[] feeds = mapper.readValue(response.getResponseBody(), Feed[].class);

                        Platform.runLater(() -> {
                            for (Feed feed : feeds) {
                                Node header = new Label(feed.getUrl());
                                header.getStyleClass().add("sub-label");

                                JFXListView<Label> subList = new JFXListView<Label>();

                                subList.setGroupnode(header);
                                subList.getStyleClass().add("sublist");

                                int i = 0;
                                for (FeedMessage feedMessage : feed.getFeedMessages()) {
                                    Label item = new Label(feedMessage.getTitle());
                                    int itemIndex = i;
                                    item.setOnMouseClicked(event -> System.out.println("ici! : " + itemIndex));
                                    subList.getItems().add(item);
                                    i++;
                                }
                                list1.getItems().add(subList);
                            }

                            list1.depthProperty().set(1);
                        });
                        return response;
                    }
                });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Preferences prefs = Preferences.userRoot().node("/com/epitech");
        token = prefs.get("token", "");

        getGroups();
    }

}
