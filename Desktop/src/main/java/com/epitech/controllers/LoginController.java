package com.epitech.controllers;


import java.net.URL;
import java.util.ResourceBundle;

import com.epitech.Network.LoginRequest;
import com.epitech.Network.LoginResponse;
import com.epitech.Network.PostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;


/**
 * Created by loulo on 05/01/2017.
 */
public class LoginController implements Initializable {

    public JFXTextField username;
    public JFXPasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    public void login(ActionEvent mouseEvent) {

        final LoginRequest loginRequest = new LoginRequest(username.getText(), password.getText());


        Task<String> requestTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                PostRequest request = new PostRequest();

                ObjectMapper mapper = new ObjectMapper();

                if (request.send("http://louismondesir.me:8080/aggregator/auth/login", mapper.writeValueAsString(loginRequest), "")) {
                    LoginResponse response = request.getResponse(LoginResponse.class);
                    System.out.println(response.getToken());
                    return response.getToken();
                } else {
                    return "";
                }
            }
        };

        requestTask.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.isEmpty()) {

                }
            }
        });

        requestTask.run();
    }

}
