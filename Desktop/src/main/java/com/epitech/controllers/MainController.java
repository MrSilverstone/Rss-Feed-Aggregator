package com.epitech.controllers;


import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // init the title hamburger icon
        drawer.setOnDrawerOpening((e) -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().play();
        });
        drawer.setOnDrawerClosing((e) -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().play();
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

        String token = "";


        FXMLLoader sidePanelLoader = new FXMLLoader(getClass().getResource("/com/epitech/views/SideMenu.fxml"));
        FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/com/epitech/views/Home.fxml"));

        Parent sidePanel;
        Parent content;
        try {
            sidePanel = sidePanelLoader.load();
            content = contentLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        drawer.setContent(content);
        drawer.setSidePane(sidePanel);

    }
}
