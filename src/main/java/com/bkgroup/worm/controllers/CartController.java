package com.bkgroup.worm.controllers;

import com.bkgroup.worm.Book;
import com.bkgroup.worm.User;
import com.bkgroup.worm.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

public class CartController {
    @FXML
    private ListView<GridPane> cartListView;
    @FXML
    private Label totalLabel;
    @FXML
    private Button checkoutButton;
    @FXML
    private Button clearButton;
    @FXML
    private ImageView angelWormImage;
    @FXML
    private ImageView devilWormImage;
    @FXML
    public void initialize() {
        // Load items from the user's cart
        for (Book book : User.getCart()) {
            addCartItem(book, 1);
        }

        // Set actions for the buttons
        checkoutButton.setOnAction(event -> checkout());
        clearButton.setOnAction(event -> clearCart());

        // Update the total items count
        updateTotal();
    }

    // Method to add an item to the cart
    public void addCartItem(Book book, int quantity) {
        GridPane item = new GridPane();
        item.setHgap(10);
        item.setVgap(5);
        item.setPadding(new Insets(5, 5, 5, 5));

        Label titleLabel = new Label(book.getTitle());
        Label authorLabel = new Label(book.getAuthor());
        Label quantityLabel = new Label(String.valueOf(quantity));

        item.add(titleLabel, 0, 0);
        item.add(authorLabel, 1, 0);
        item.add(quantityLabel, 2, 0);

        cartListView.getItems().add(item);
        updateTotal(); // Update total whenever a new item is added
    }

    // Method to handle the checkout process
    private void checkout() {
        Tools.ShowPopup(4, "Checkout", "Proceeding to checkout...");
    }

    // Method to clear the cart
    private void clearCart() {
        cartListView.getItems().clear();
        User.clearCart(); // Clear the cart in User class
        updateTotal(); // Update the total count to reflect the cleared cart
    }

    // Method to update the total items displayed
    private void updateTotal() {
        int totalItems = cartListView.getItems().size();
        totalLabel.setText(String.valueOf(totalItems));
    }
}

