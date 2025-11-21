package com.burrito.king.burrito_king_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerApplication extends Application {

    private int cusId;

    public CustomerApplication() {
    }

    public CustomerApplication(int cusId) {
        this.cusId = cusId;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerController controller = loader.getController();
        controller.setCusId(cusId);

        primaryStage.setTitle("Customer View");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
