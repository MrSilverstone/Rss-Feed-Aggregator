package com.epitech.controllers;

import com.epitech.model.LoginRequest;
import com.epitech.model.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class LoginController implements Initializable {

    @FXML
    public JFXTextField username;

    @FXML
    public JFXPasswordField password;

    @FXML
    public JFXButton loginBtn;

    @FXML
    public JFXButton signupBtn;

    private Stage prevStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void login(ActionEvent actionEvent) {

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

        String login = username.getText();
        String passwd = password.getText();

        LoginRequest request = new LoginRequest(login, passwd);

        ObjectMapper mapper = new ObjectMapper();

        String body = "";

        try {
            body = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            return;
        }


        asyncHttpClient.preparePost("http://louismondesir.me:8080/aggregator/auth/login")
                .setBody(body)
                .setHeader("Content-type", "application/json")
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {

                        if (response.getStatusCode() != 200)
                            return null;

                        final LoginResponse loginResponse = mapper.readValue(response.getResponseBody(), LoginResponse.class);
                        System.out.println(loginResponse.getToken());

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Preferences prefs = Preferences.userRoot().node("/com/epitech");
                                    prefs.put("token", loginResponse.getToken());
                                    setDrawer(loginResponse.getToken());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        return response;
                    }


                });
    }


    void setDrawer(String token) {
        prevStage.close();

        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/epitech/views/Main.fxml"));
        Parent root;

        try {
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-main-demo.css").toExternalForm());
        //		stage.initStyle(StageStyle.UNDECORATED);
        //		stage.setFullScreen(true);
        stage.setMinWidth(700);
        stage.setMinHeight(800);
        stage.setScene(scene);

        stage.show();
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }
}
