package com.epitech;

import com.epitech.controllers.HomeController;
import com.epitech.controllers.LoginController;
import com.epitech.controllers.MainController;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXDecorator;

public class Main extends Application {

    @FXMLViewFlowContext private ViewFlowContext flowContext;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        new Thread(()->{
            try {
                SVGGlyphLoader.loadGlyphsFont(getClass().getResourceAsStream("/com/epitech/font/icomoon.svg"),"icomoon.svg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/epitech/controllers/Login.fxml"));
        Parent root = (Parent)loader.load();

        LoginController controller = (LoginController)loader.getController();
        controller.setPrevStage(stage);

        Scene scene = new Scene(root);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

}
