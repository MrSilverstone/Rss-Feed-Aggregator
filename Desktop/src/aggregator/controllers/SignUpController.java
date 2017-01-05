package aggregator.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by loulo on 05/01/2017.
 */
public class SignUpController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private JFXButton login;

    @FXML
    void makeLogin(ActionEvent event) {
        if(username.getText().equals("genuine") && pass.getText().equals("coder"))
        {
            System.out.println("Welcome");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
