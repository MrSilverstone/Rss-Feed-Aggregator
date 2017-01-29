package com.epitech.controllers;

import com.epitech.model.LoginRequest;
import com.epitech.model.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.FlowView;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;
    @FXML
    public JFXTextField username;

    @FXML
    public JFXPasswordField password;

    @FXML
    public JFXButton loginBtn;

    @FXML
    public JFXButton signupBtn;

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


    void setDrawer(String token) throws FlowException {
        prevStage.close();

        prevStage = new Stage();

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", prevStage);
        flowContext.register("token", token);


        flow.createHandler(flowContext).start(container);


        JFXDecorator decorator = new JFXDecorator(prevStage, container.getView());
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-main-demo.css").toExternalForm());
        //		stage.initStyle(StageStyle.UNDECORATED);
        //		stage.setFullScreen(true);
        prevStage.setMinWidth(700);
        prevStage.setMinHeight(800);
        prevStage.setScene(scene);

        prevStage.show();

        System.out.println("ici!");
    }

    Stage prevStage;

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }


}
