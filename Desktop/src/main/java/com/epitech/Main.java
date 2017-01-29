package com.epitech;


import com.epitech.controllers.LoginController;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/epitech/views/Login.fxml"));
        Parent root = (Parent)loader.load();

        LoginController controller = (LoginController)loader.getController();
        controller.setPrevStage(stage);

        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 500, 300);

        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-design.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/epitech/css/jfoenix-main-demo.css").toExternalForm());

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

}
