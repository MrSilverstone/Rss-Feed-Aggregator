package com.epitech.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javax.annotation.PostConstruct;


@FXMLController(value = "Home.fxml", title = "Home")
public class HomeController {

    @FXML
    private JFXListView<JFXListView> list1;

    private int counter = 0;

    void populateList() {

        String[] titles = new String[]{"Le monde", "9gag", "StackOverflow", "Xda developers"};

        for (String title : titles) {
            Node header = new Label(title);
            header.getStyleClass().add("sub-label");

            JFXListView<Label> subList = new JFXListView<Label>();

            subList.setGroupnode(header);
            subList.getStyleClass().add("sublist");

            for (int i = 0; i < 15; i++) {
                Label item = new Label("This is the news number : " + i);

                int itemIndex = i;
                item.setOnMouseClicked(event -> System.out.println("ici! : " + itemIndex));
                subList.getItems().add(item);
            }

            list1.getItems().add(subList);
        }

    }


    @PostConstruct
    public void init() throws FlowException, VetoException {

        populateList();
        list1.depthProperty().set(1);
    }

}
