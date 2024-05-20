package com.bkgroup.worm.controllers;

// Import necessary JavaFX classes
import com.bkgroup.worm.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

public class CartController {
    // Declare FXML components
    @FXML
    private ListView<GridPane> cartListView; // ListView to display cart items
    @FXML
    private Label totalLabel; // Label to display total price
    @FXML
    private Button checkoutButton; // Button to proceed with checkout
    @FXML
    private Button clearButton; // Button to clear the cart

    @FXML
    public void initialize() {
        // This method runs when the controller is initialized
        // Print debug message
        System.out.println("CartController initialized");

        // Add example items to the cart (for testing purposes)
        addCartItem("The Magician's Nephew", "C.S. Lewis", 10.99, 1);
        addCartItem("Harry Potter and the Half-Blood Prince", "J.K. Rowling", 15.99, 2);

        // Set actions for the buttons
        checkoutButton.setOnAction(event -> checkout());
        // Set action for the clear cart button
        clearButton.setOnAction(event -> clearCart());

        // Update the total price
        updateTotal();
    }

    // Method to add an item to the cart
    private void addCartItem(String title, String author, double price, int quantity) {
        // Create a new GridPane for the item
        GridPane item = new GridPane();
        item.setHgap(10); // Set horizontal gap between elements
        item.setVgap(5); // Set vertical gap between elements
        item.setPadding(new Insets(5, 5, 5, 5)); // Set padding around the GridPane

        // Create labels for the item's details
        Label titleLabel = new Label(title); // Book title
        Label authorLabel = new Label(author); // Author name
        Label priceLabel = new Label(String.format("$%.2f", price)); // Price
        Label quantityLabel = new Label(String.valueOf(quantity)); // Quantity
        Label totalLabel = new Label(String.format("$%.2f", price * quantity)); // Total price for the item

        // Add labels to the GridPane in respective columns
        item.add(titleLabel, 0, 0);
        item.add(authorLabel, 1, 0);
        item.add(priceLabel, 2, 0);
        item.add(quantityLabel, 3, 0);
        item.add(totalLabel, 4, 0);

        // Add the GridPane to the ListView
        cartListView.getItems().add(item);
    }

    // Method to handle the checkout process
    private void checkout() {
        // Show an alert dialog to indicate checkout
        Tools.ShowPopup(4,"Checkout","Proceeding to checkout...");
    }

    // Method to clear the cart
    private void clearCart() {
        // Clear all items from the ListView
        cartListView.getItems().clear();
        // Update the total price to reflect the cleared cart
        updateTotal();
    }

    // Method to update the total price displayed
    private void updateTotal() {
        double total = 0.0; // Initialize total price
        // Iterate through each item in the ListView
        for (GridPane item : cartListView.getItems()) {
            // Get the total price label for the item (assumed to be in the 5th column)
            Label totalLabel = (Label) item.getChildren().get(4);
            // Add the item's total price to the overall total (remove the "$" sign and parse as double)
            total += Double.parseDouble(totalLabel.getText().substring(1));
        }
        // Update the totalLabel to show the overall total price
        totalLabel.setText(String.format("%.2f", total));
    }
}
