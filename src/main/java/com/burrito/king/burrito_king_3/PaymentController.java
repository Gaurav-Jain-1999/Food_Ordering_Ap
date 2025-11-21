package com.burrito.king.burrito_king_3;

import com.burrito.king.burrito_king_3.DBUtil;
import com.burrito.king.burrito_king_3.models.Order;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

import static java.lang.Thread.sleep;

public class PaymentController {

    @FXML
    private TextField cardNumber;
    @FXML
    private DatePicker expiryDate;
    @FXML
    private TextField cvv;
    @FXML
    private TextField orderTime;
    @FXML
    private TextArea resultConsole;

    private Order currentOrder;
    private int cusId;

    public void setOrderDetails(Order order, int cusId) {
        this.currentOrder = order;
        this.cusId = cusId;
    }

    @FXML
    private void processPayment(ActionEvent event) {
        try {
            if (validatePaymentDetails()) {
                saveOrderToDatabase(currentOrder);
                resultConsole.setText("Payment successful! Order placed.");

                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(e -> {
                    try {
                        CustomerApplication customerApplication = new CustomerApplication(cusId);
                        customerApplication.start(new Stage());

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                delay.play();
            }

        } catch (Exception e) {
            resultConsole.setText("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validatePaymentDetails() {
        String cardNum = cardNumber.getText();
        LocalDate expDate = expiryDate.getValue();
        String cvvCode = cvv.getText();

        if (cardNum.length() != 16 || !cardNum.matches("\\d+")) {
            resultConsole.setText("Invalid card number. Must be 16 digits.");
            return false;
        }
        if (expDate == null || expDate.isBefore(LocalDate.now())) {
            resultConsole.setText("Invalid expiry date. Must be a future date.");
            return false;
        }
        if (cvvCode.length() != 3 || !cvvCode.matches("\\d+")) {
            resultConsole.setText("Invalid CVV. Must be 3 digits.");
            return false;
        }
        return true;
    }

    private void saveOrderToDatabase(Order order) {
        String sql = "INSERT INTO ActiveOrders (Cus_ID, OrderDetails, OrderID, OrderTime) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String orderNumber = generateOrderNumber();
            String orderTimeStr = orderTime.getText();

            pstmt.setInt(1, cusId);
            pstmt.setString(2, orderToString(order));
            pstmt.setString(3, orderNumber);
            pstmt.setString(4, orderTimeStr);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            resultConsole.setText("Error placing order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String orderToString(Order order) {
        StringBuilder orderDetails = new StringBuilder();
        order.getItems().forEach(item -> orderDetails.append(item.getQuantity())
                .append(" ")
                .append(item.getClass().getSimpleName())
                .append(", "));
        if (orderDetails.length() > 0) {
            orderDetails.setLength(orderDetails.length() - 2); // Remove trailing comma and space
        }
        return orderDetails.toString();
    }

    private String generateOrderNumber() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(100000000));
    }

    @FXML
    private void returnToCustomerView(ActionEvent event) throws Exception {
        CustomerApplication customerApplication = new CustomerApplication(cusId);
        customerApplication.start(new Stage());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
