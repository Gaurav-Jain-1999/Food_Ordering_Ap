package com.burrito.king.burrito_king_3;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class RegistrationController {

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField Mobile;
    @FXML private TextArea resultConsole;

    public void register(ActionEvent event) throws Exception {
        try {
            RegistrationDAO.insert(username.getText(), password.getText(), name.getText(), surname.getText(), Mobile.getText());
            resultConsole.setText("Successfully registered");
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXML/LoginView.fxml")));
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            delay.play();

        } catch (Exception e) {
            resultConsole.setText("Error while registering: " + e.getMessage());
            System.out.println("Error while registering and inserting");
            e.printStackTrace();
        }
    }

    public void return_back(ActionEvent event) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/LoginView.fxml"));
            Scene log = new Scene(root);
            //gets stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(log);
            window.show();
        } catch (Exception e) {
            System.out.println("Error occurred while opening Log page");
            e.printStackTrace();
            throw e;
        }
    }
}
