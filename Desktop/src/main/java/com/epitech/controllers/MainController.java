package com.epitech.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by loulo on 16/01/2017.
 */
public class MainController implements Initializable {
    @FXML
    public JFXDrawer drawer;

    @FXML
    public JFXHamburger hamburger;

    @FXML
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/views/NavigationDrawer.fxml"));
            drawer.setSidePane(box);
        } catch (IOException ex) {

        }

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);

        transition.setRate(-1);

        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();

            if (drawer.isShown()){
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }
}
