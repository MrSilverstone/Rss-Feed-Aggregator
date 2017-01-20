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

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);

        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
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

}
