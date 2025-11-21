package com.burrito.king.burrito_king_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;

public class LoginController {

    @FXML
    public TextField txtUsername;
    public PasswordField txtPassword;

    public void login(ActionEvent event) throws Exception {
        try {
            if (LoginDAO.isLogin(txtUsername.getText(), txtPassword.getText())) {
                int cusId = LoginDAO.getCusId(txtUsername.getText(), txtPassword.getText());
                CustomerApplication customerApplication = new CustomerApplication(cusId);
                customerApplication.start(new Stage());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

                System.out.println("Connected successfully");
            } else {
                txtUsername.setStyle("-fx-border-color:red; -fx-border-width:2px;");
                txtPassword.setStyle("-fx-border-color:red; -fx-border-width:2px;");
                System.out.println("Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(ActionEvent event) throws Exception {
        try {
            URL url = getClass().getResource("FXML/RegistrationView.fxml");
            System.out.println("URL: " + url);  // This should print the actual URL or null

            Parent root = FXMLLoader.load(Objects.requireNonNull(url));
            Scene reg = new Scene(root);
            //gets stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(reg);
            window.show();
        } catch (Exception e) {
            System.out.println("Error occurred while opening registration page");
            e.printStackTrace();
        }
    }
}
