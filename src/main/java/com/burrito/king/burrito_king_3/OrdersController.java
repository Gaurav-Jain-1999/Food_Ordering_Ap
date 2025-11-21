package com.burrito.king.burrito_king_3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class OrdersController {

    @FXML
    private TableView<Order> activeOrdersTable;
    @FXML
    private TableColumn<Order, Integer> activeOrderIDColumn;
    @FXML
    private TableColumn<Order, String> activeOrderTimeColumn;
    @FXML
    private TableColumn<Order, String> activeOrderDetailsColumn;

    @FXML
    private TableView<Order> pastOrdersTable;
    @FXML
    private TableColumn<Order, Integer> pastOrderIDColumn;
    @FXML
    private TableColumn<Order, String> pastOrderStatusColumn;
    @FXML
    private TableColumn<Order, String> pastOrderDetailsColumn;

    @FXML
    private Button collectOrderButton;
    @FXML
    private Button cancelOrderButton;

    private int cusId;

    public void setCusId(int cusId) {
        this.cusId = cusId;
        loadOrders();
    }

    @FXML
    public void initialize() {
        activeOrderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        activeOrderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        activeOrderDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("orderDetails"));

        pastOrderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        pastOrderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        pastOrderDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("orderDetails"));

    }

    private void loadOrders() {
        ObservableList<Order> activeOrders = FXCollections.observableArrayList();
        ObservableList<Order> pastOrders = FXCollections.observableArrayList();

        String activeOrdersQuery = "SELECT OrderID, OrderTime, OrderDetails FROM ActiveOrders WHERE Cus_ID = ?";
        String pastOrdersQuery = "SELECT OrderID, OrderStatus, OrderDetails FROM CompletedOrders WHERE Cus_ID = ?";

        try (Connection conn = DBUtil.dbConnect();
             PreparedStatement activeStmt = conn.prepareStatement(activeOrdersQuery);
             PreparedStatement pastStmt = conn.prepareStatement(pastOrdersQuery)) {

            activeStmt.setInt(1, cusId);
            pastStmt.setInt(1, cusId);

            try (ResultSet rs = activeStmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("OrderID");
                    String orderTime = rs.getString("OrderTime");
                    String orderDetails = rs.getString("OrderDetails");
                    activeOrders.add(new Order(orderId, orderTime, orderDetails));
                }
            }

            try (ResultSet rs = pastStmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("OrderID");
                    String orderStatus = rs.getString("OrderStatus");
                    String orderDetails = rs.getString("OrderDetails");
                    pastOrders.add(new Order(orderId, orderStatus, orderDetails));
                }
            }

            activeOrdersTable.setItems(activeOrders);
            pastOrdersTable.setItems(pastOrders);

        } catch (SQLException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }

    @FXML
    private void collectOrder() {
        Order selectedOrder = activeOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            int orderId = selectedOrder.getOrderId();
            String orderDetails = selectedOrder.getOrderDetails();

            // Move order from ActiveOrders to CompletedOrders
            String insertCompletedOrder = "INSERT INTO CompletedOrders (Cus_ID, OrderID, OrderDetails, OrderStatus) VALUES (?, ?, ?, 'Completed')";
            String deleteActiveOrder = "DELETE FROM ActiveOrders WHERE OrderID = ?";

            try (Connection conn = DBUtil.dbConnect();
                 PreparedStatement insertStmt = conn.prepareStatement(insertCompletedOrder);
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteActiveOrder)) {

                insertStmt.setInt(1, cusId);
                insertStmt.setInt(2, orderId);
                insertStmt.setString(3, orderDetails);
                insertStmt.executeUpdate();

                deleteStmt.setInt(1, orderId);
                deleteStmt.executeUpdate();

                loadOrders();  // Refresh orders after update

            } catch (SQLException e) {
                System.out.println("Error processing order collection: " + e.getMessage());
            }
        }
    }

    @FXML
    private void cancelOrder() {
        Order selectedOrder = activeOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            int orderId = selectedOrder.getOrderId();
            String orderDetails = selectedOrder.getOrderDetails();

            // Move order from ActiveOrders to CompletedOrders with 'Cancelled' status
            String insertCompletedOrder = "INSERT INTO CompletedOrders (Cus_ID, OrderID, OrderDetails, OrderStatus) VALUES (?, ?, ?, 'Cancelled')";
            String deleteActiveOrder = "DELETE FROM ActiveOrders WHERE OrderID = ?";

            try (Connection conn = DBUtil.dbConnect();
                 PreparedStatement insertStmt = conn.prepareStatement(insertCompletedOrder);
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteActiveOrder)) {

                insertStmt.setInt(1, cusId);
                insertStmt.setInt(2, orderId);
                insertStmt.setString(3, orderDetails);
                insertStmt.executeUpdate();

                deleteStmt.setInt(1, orderId);
                deleteStmt.executeUpdate();

                loadOrders();  // Refresh orders after update

            } catch (SQLException e) {
                System.out.println("Error canceling order: " + e.getMessage());
            }
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