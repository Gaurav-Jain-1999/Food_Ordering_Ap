package com.burrito.king.burrito_king_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ProfileController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField mobile;
    @FXML
    private TextArea resultConsole;

    private int cusId;
    @FXML
    public void setCusId(int cusId) {
        this.cusId = cusId;
        System.out.println(cusId);
        loadProfile();
    }

    private void loadProfile() {
        // Load profile information from database based on cusId
        String sql = "SELECT log_username, log_password, Cus_FName, Cus_LName, Mobile FROM Customers WHERE Cus_ID = ?";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cusId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    username.setText(rs.getString("log_username"));
                    password.setText(rs.getString("log_password"));
                    name.setText(rs.getString("Cus_FName"));
                    surname.setText(rs.getString("Cus_LName"));
                    mobile.setText(rs.getString("Mobile"));
                    username.setDisable(true); // Ensure username cannot be edited
                }
            }
        } catch (SQLException e) {
            resultConsole.setText("Error loading profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void updateProfile(ActionEvent event) {
        String sql = "UPDATE Customers SET log_password = ?, Cus_FName = ?, Cus_LName = ?, Mobile = ? WHERE Cus_ID = ?";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, password.getText());
            pstmt.setString(2, name.getText());
            pstmt.setString(3, surname.getText());
            pstmt.setString(4, mobile.getText());
            pstmt.setInt(5, cusId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                resultConsole.setText("Changes saved successfully");
            } else {
                resultConsole.setText("Failed to save changes");
            }
        } catch (SQLException e) {
            resultConsole.setText("Error saving changes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void return_back(ActionEvent event) throws Exception {
        CustomerApplication customerApplication = new CustomerApplication(cusId);
        customerApplication.start(new Stage());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
