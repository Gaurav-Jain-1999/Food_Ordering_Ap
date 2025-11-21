package com.burrito.king.burrito_king_3;

import com.burrito.king.burrito_king_3.models.*;
import com.burrito.king.burrito_king_3.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.burrito.king.burrito_king_3.DBUtil.dbExecuteQuery;

public class NewOrderController {

    @FXML
    private TextField burritoQuantity;
    @FXML
    private TextField friesQuantity;
    @FXML
    private TextField sodaQuantity;
    @FXML
    private ListView<String> cartListView;
    @FXML
    private TextField totalPrice;
    @FXML
    private TextArea resultConsole;

    private final ObservableList<String> cartItems = FXCollections.observableArrayList();
    private final Order currentOrder = new Order();
    private int cusId;

    private final Restaurant restaurant = new Restaurant("Burrito King"); // Initialize Restaurant

    @FXML
    public void setCusId(int cusId) {
        this.cusId = cusId;
    }
    @FXML
    public void initialize() {
        cartListView.setItems(cartItems);
    }

    @FXML
    private void addToCart(ActionEvent event) {
        try {
            int burritos = !burritoQuantity.getText().isEmpty() ? Integer.parseInt(burritoQuantity.getText()) : 0;
            int fries = !friesQuantity.getText().isEmpty() ? Integer.parseInt(friesQuantity.getText()) : 0;
            int sodas = !sodaQuantity.getText().isEmpty() ? Integer.parseInt(sodaQuantity.getText()) : 0;

            if (burritos > 0) {
                Burrito burrito = new Burrito(Restaurant.getPrice("Burrito"), burritos);
                currentOrder.addFoodItem(burrito);
                cartItems.add(burritos + " Burritos");
            }

            if (fries > 0) {
                Fries friesItem = new Fries(Restaurant.getPrice("Fries"), fries);
                currentOrder.addFoodItem(friesItem);
                cartItems.add(fries + " Fries");
            }

            if (sodas > 0) {
                Soda soda = new Soda(Restaurant.getPrice("Soda"), sodas);
                currentOrder.addFoodItem(soda);
                cartItems.add(sodas + " Sodas");
            }

            burritoQuantity.clear();
            friesQuantity.clear();
            sodaQuantity.clear();
            updateTotalPrice();
        } catch (NumberFormatException e) {
            resultConsole.setText("Please enter valid quantities.");
        } catch (Exception e) {
            resultConsole.setText("Error adding to cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void updateCart(ActionEvent event) {
        ObservableList<String> updatedCartItems = FXCollections.observableArrayList();

        for (FoodItem item : currentOrder.getItems()) {
            if (item instanceof Burrito) {
                String newQuantityStr = burritoQuantity.getText();
                int newQuantity = !newQuantityStr.isEmpty() ? Integer.parseInt(newQuantityStr) : item.getQuantity();
                item.setQuantity(newQuantity);
                updatedCartItems.add(newQuantity + " Burritos");
            } else if (item instanceof Fries) {
                String newQuantityStr = friesQuantity.getText();
                int newQuantity = !newQuantityStr.isEmpty() ? Integer.parseInt(newQuantityStr) : item.getQuantity();
                item.setQuantity(newQuantity);
                updatedCartItems.add(newQuantity + " Fries");
            } else if (item instanceof Soda) {
                String newQuantityStr = sodaQuantity.getText();
                int newQuantity = !newQuantityStr.isEmpty() ? Integer.parseInt(newQuantityStr) : item.getQuantity();
                item.setQuantity(newQuantity);
                updatedCartItems.add(newQuantity + " Sodas");
            }
        }

        cartItems.setAll(updatedCartItems);
        updateTotalPrice();
    }

    @FXML
    private void placeOrder(ActionEvent event) throws IOException {
        if (currentOrder.getItems().isEmpty()) {
            resultConsole.setText("Cart is empty. Add items to the cart before placing an order.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/PaymentView.fxml"));
        Parent root = loader.load();

        PaymentController paymentController = loader.getController();
        paymentController.setOrderDetails(currentOrder, cusId);

        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    @FXML
    private void clearCart(ActionEvent event) {
        cartItems.clear();
        currentOrder.getItems().clear();
        updateTotalPrice();
        resultConsole.setText("Cart cleared.");
    }

    @FXML
    private void returnToCustomerView(ActionEvent event) throws Exception {

        CustomerApplication customerApplication = new CustomerApplication(cusId);
        customerApplication.start(new Stage());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void updateTotalPrice() {
        double total = currentOrder.getTotalPrice();
        totalPrice.setText(String.format("$%.2f", total));
    }
}