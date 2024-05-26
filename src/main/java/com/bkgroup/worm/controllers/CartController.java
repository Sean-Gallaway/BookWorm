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
        clearCart(false);

        // Load items from the user's cart
        for (Book book : User.getCart()) {
            addCartItem(book);
        }

        // Set actions for the buttons
        checkoutButton.setOnAction(event -> checkout());
        clearButton.setOnAction(event -> clearCart(true));

        // Update the total items count
        updateTotal();
    }

    /**
     * Adds book item to cart displaying book data.
     * @param book Book
     */
    public void addCartItem(Book book) {
        GridPane item = new GridPane();
        item.setHgap(10);
        item.setVgap(5);
        item.setPadding(new Insets(5, 5, 5, 5));

        Label titleLabel = new Label(book.getTitle());
        Label authorLabel = new Label(book.getAuthor());

        item.add(titleLabel, 0, 0);
        item.add(authorLabel, 1, 0);

        cartListView.getItems().add(item);
        updateTotal(); // Update total whenever a new item is added
    }

    /**
     * Handles checkout process.
     */
    private void checkout() {
        Tools.ShowPopup(4, "Checkout", "Proceeding to checkout...");
    }

    /**
     * Clears cart display and user's cart if mode is destructive.
     * @param destructive True to delete user's cart; false to just clear display
     */
    private void clearCart(boolean destructive) {
        // Clear and reset display
        cartListView.getItems().clear();

        // Clear user cart
        if (destructive) {
            User.clearCart();
        }
    }

    /**
     * Updates text displaying total amount of books.
     */
    private void updateTotal() {
        int totalItems = cartListView.getItems().size();
        totalLabel.setText(String.valueOf(totalItems));
    }
}

