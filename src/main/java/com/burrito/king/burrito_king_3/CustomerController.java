package com.burrito.king.burrito_king_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CustomerController {

    private int cusId;

    public void setCusId(int cusId) {
        this.cusId = cusId;
        loadProfile();
    }

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSurname;

    private void loadProfile() {
        // Load profile information from database based on cusId
        String sql = "SELECT Cus_FName, Cus_LName FROM Customers WHERE Cus_ID = ?";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cusId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    txtName.setText(rs.getString("Cus_FName"));
                    txtSurname.setText(rs.getString("Cus_LName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }}
    @FXML
    private void makeOrder(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/NewOrderView.fxml"));
        Parent root = loader.load();
        NewOrderController NewOrderController = loader.getController();
        NewOrderController.setCusId(cusId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void viewOrders(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/CombinedOrders.fxml"));
        Parent root = loader.load();
        OrdersController ordersController = loader.getController();
        ordersController.setCusId(cusId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void viewProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ProfileView.fxml"));
        Parent root = loader.load();
        ProfileController profileController = loader.getController();
        profileController.setCusId(cusId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        navigateTo(event, "FXML/LoginView.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
