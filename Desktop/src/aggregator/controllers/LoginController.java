package aggregator.controllers;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;


/**
 * Created by loulo on 05/01/2017.
 */
public class LoginController implements Initializable {


    public TextField email;
    public PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void onEmailChanged(InputMethodEvent inputMethodEvent) {
        System.out.println(inputMethodEvent.getCommitted());
    }

    public void onPasswordChanged(InputMethodEvent inputMethodEvent) {
        System.out.println(inputMethodEvent.getCommitted());
    }

    public void login(MouseEvent mouseEvent) {


        final String emailStr = email.getText();
        final String passwordStr = password.getText();

        final Service<Void> loginService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return null;
            }
        };

        loginService.start();
    }
}
