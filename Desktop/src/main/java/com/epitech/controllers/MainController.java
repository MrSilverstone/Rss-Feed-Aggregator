package com.epitech.controllers;


import com.epitech.model.AddFeedRequest;
import com.epitech.model.Feed;
import com.epitech.model.FeedMessage;
import com.epitech.model.Group;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainController implements Initializable {
    public JFXListView<JFXListView> feedsList;
    @FXML
    private StackPane root;

    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    private JFXHamburger titleBurger;

    @FXML
    private StackPane optionsBurger;
    @FXML
    private JFXRippler optionsRippler;

    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXPopup toolbarPopup;
    @FXML
    private Label exit;

    private String token;

    private List<Group> groupList;
    private List<FeedMessage> feedMessageList;

    private String currentGroup;

    AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        drawer.setMouseTransparent(true);

        // init the title hamburger icon
        drawer.setOnDrawerOpening((e) -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().play();
            drawer.setMouseTransparent(false);
        });
        drawer.setOnDrawerClosing((e) -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().play();
            drawer.setMouseTransparent(true);
        });
        titleBurgerContainer.setOnMouseClicked((e) -> {
            if (drawer.isHidden() || drawer.isHidding()) drawer.open();
            else drawer.close();
        });

        // init Popup
        toolbarPopup.setPopupContainer(root);
        toolbarPopup.setSource(optionsRippler);
        root.getChildren().remove(toolbarPopup);

        optionsBurger.setOnMouseClicked((e) -> {
            toolbarPopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, -12, 15);
        });

        // close application
        exit.setOnMouseClicked((e) -> {
            Platform.exit();
        });

        Preferences prefs = Preferences.userRoot().node("/com/epitech");
        token = prefs.get("token", "");

        getGroups();
    }

    private void getGroups() {

//        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
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

                        groupList = new ArrayList<>(Arrays.asList(groups));

                        Platform.runLater(() -> constructSideMenu(groups));

                        if (groups.length > 0)
                        {
                            currentGroup = groups[0].getName();
                            getFeedsForGroup(groups[0]);
                        } else {
                            if (feedMessageList != null)
                                Platform.runLater(() -> feedMessageList.clear());
                        }

                        return response;
                    }
                });
    }

    private void constructSideMenu(Group[] groups) {
        VBox sideMenu = new VBox();
        sideMenu.getStyleClass().add("side-menu");

        for (Group g : groups) {

            HBox hbox = new HBox();

            JFXButton deleteGroupButton = new JFXButton("X");
            deleteGroupButton.setOnAction(actionEvent -> deleteGroup(g.getName()));

            JFXButton groupButton = new JFXButton(g.getName());
            groupButton.setOnAction(groupActionHandler);

            hbox.getChildren().add(groupButton);
            hbox.getChildren().add(deleteGroupButton);

            sideMenu.getChildren().add(hbox);
        }

        JFXTextField groupTextField = new JFXTextField();
        groupTextField.setPromptText("New group");
        groupTextField.setOnAction(actionEvent -> {
            addGroup(groupTextField.getText());
            groupTextField.setText("");
        });

        sideMenu.getChildren().add(groupTextField);

        if (groups.length > 0) {
            JFXTextField feedTextField = new JFXTextField();
            feedTextField.setPromptText("New feed");
            feedTextField.setOnAction(actionEvent -> {
                addFeed(feedTextField.getText());
                feedTextField.setText("");
            });
            sideMenu.getChildren().add(feedTextField);
        }

        drawer.setSidePane(sideMenu);
    }

    private EventHandler<ActionEvent> groupActionHandler = actionEvent -> {
        JFXButton btn = (JFXButton) actionEvent.getSource();
        currentGroup = btn.getText();
        changeGroup(btn.getText());
    };

    private void addFeed(String feedUrl) {
        if (currentGroup.isEmpty())
            return;

        AddFeedRequest addFeedRequest = new AddFeedRequest(currentGroup, feedUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        String body;

        try {
            body = objectMapper.writeValueAsString(addFeedRequest);
        } catch (JsonProcessingException e) {
            System.out.println("Error while serializing addFeedRequest...");
            return;
        }

        asyncHttpClient.preparePut("http://louismondesir.me:8080/aggregator/api/feeds/")
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .setBody(body)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        if (response.getStatusCode() != 200)
                            return null;

                        getGroups();

                        return response;
                    }
                });
    }

    private void deleteGroup(String groupName) {

        asyncHttpClient.prepareDelete("http://louismondesir.me:8080/aggregator/api/groups/" + groupName)
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        if (response.getStatusCode() != 200)
                            return null;
                        getGroups();
                        return response;
                    }
                });
    }

    private void changeGroup(String groupName) {

        for (Group g : groupList) {
            if (g.getName().equals(groupName)) {
                getFeedsForGroup(g);
                break;
            }
        }
    }

    private void addGroup(String groupName) {
        //    AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        asyncHttpClient.preparePut("http://louismondesir.me:8080/aggregator/api/groups/" + groupName)
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        if (response.getStatusCode() != 200)
                            return null;

                        getGroups();

                        return response;
                    }
                });
    }

    private void showFeedDescription(FeedMessage feedMessage) {

        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(new URI(feedMessage.getLink()));
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Wrong url : " + feedMessage.getLink());
                }
            }).start();
        } else {
            System.err.println("Cannot open browser");
        }
    }

    private void getFeedsForGroup(Group group) {
        //    AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        asyncHttpClient.prepareGet("http://louismondesir.me:8080/aggregator/api/feeds/" + group.getName())
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "Bearer " + token)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        if (response.getStatusCode() != 200)
                            return null;

                        ObjectMapper mapper = new ObjectMapper();
                        Feed[] feeds = mapper.readValue(response.getResponseBody(), Feed[].class);

                        Platform.runLater(() -> {
                            feedsList.getItems().clear();
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
                                    item.setOnMouseClicked(event -> {
                                        System.out.println("ici! : " + itemIndex);
                                        showFeedDescription(feedMessage);
                                    });


                                    subList.getItems().add(item);
                                    i++;
                                }
                                feedsList.getItems().add(subList);
                            }
                            feedsList.depthProperty().set(1);
                        });
                        return response;
                    }
                });
    }
}
