package com.bkgroup.worm.controllers;

import com.bkgroup.worm.App;
import com.bkgroup.worm.utils.Book;
import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.Tools;
import com.bkgroup.worm.utils.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.bkgroup.worm.controllers.HomeController.BOOK_HEIGHT;

public class OverlayController {

    private enum LikeStatus {Dislike, Neutral, Like}
    private final Image greyLike = new Image("images/GreyThumbUp.png");
    private final Image greenLike = new Image("images/GreenThumbUp.png");
    private final Image greyDislike = new Image("images/GreyThumbDown.png");
    private final Image redDislike = new Image("images/RedThumbDown.png");

    private Book selectedBook;
    @FXML AnchorPane bookViewer;
    //Book Viewer Window
    @FXML StackPane viewerPane;
    @FXML ImageView viewerCover;
    @FXML Label viewerTitle;
    @FXML Label viewerSeries;
    @FXML Label viewerAuthor;
    @FXML Label viewerGenre;
    @FXML Label viewerDescription;

    //Book Viewer Buttons
    @FXML Button likeButton;
    @FXML Button dislikeButton;
    @FXML Button closeButton;
    @FXML Button btn_wishlist;
    @FXML Button btn_cart;
    boolean addToWishlist = true;
    boolean addToCart = true;

    @FXML
    private ListView<GridPane> cartListView;
    @FXML
    private ListView<GridPane> wishlistListView;

    private static ListView<GridPane> staticWishlistListView;

    public void initialize() {
        staticWishlistListView = wishlistListView;
    }

    /**
     * Show the ClickView menu, loading the book information of the book that was clicked.
     * @param content The database content of the book that was clicked.
     * @param bookCover The Image cover art for the book
     * @param title The book title
     */
    public void clickBook(String[] content, Image bookCover, String title) {
        // Make visible and set size
        bookViewer.setVisible(true);
        bookViewer.setPrefWidth(App.stage.getWidth());
        bookViewer.setPrefHeight(App.stage.getHeight());

        // Set the book cover
        viewerCover.setImage(bookCover);
        double desiredHeight = BOOK_HEIGHT + 100;
        double scaleFactor = desiredHeight / bookCover.getHeight();
        double scaledWidth = bookCover.getWidth() * scaleFactor;
        viewerCover.setFitWidth(scaledWidth);
        viewerCover.setFitHeight(desiredHeight);

        // Set the title
        viewerTitle.setText(content[Query.BookAttributes.title.ordinal()]);

        // Set the series
        if (content[2] == null) { // If it's not part of a series, disable the label
            viewerSeries.setVisible(false);
        } else { // If it is part of a series
            viewerSeries.setVisible(true);
            viewerSeries.setText(content[Query.BookAttributes.series.ordinal()] + " : " + content[Query.BookAttributes.seriesNum.ordinal()]);
        }

        // Set the author
        viewerAuthor.setText(("by " + content[4]));

        // Set the genres
        ArrayList<String[]> genres = Query.resultSetToArrayList(Query.select("genre", "genre", Query.where("bookID", content[0])));
        StringBuilder genreText = new StringBuilder("Genre: ");
        for (String[] i : genres) {
            genreText.append(i[0]).append(", ");
        }
        genreText = new StringBuilder(genreText.substring(0, genreText.length() - 2)); // Remove the final comma and space
        viewerGenre.setText(genreText.toString());

        // Set description
        viewerDescription.setWrapText(true); // Make sure the text wraps if it gets too long.
        viewerDescription.setText(loadDescription(title));

        // Store the selected book reference
        selectedBook = new Book(Integer.parseInt(content[Query.BookAttributes.bookID.ordinal()]));

        setLikeStatus(selectedBook);//Set the current like status to the like status of the book.

        // Assign button correct text and action
        if (User.isLoggedIn() && User.ExistsInWishlist(selectedBook)) {
            btn_wishlist.setText("Remove Wishlist");
            addToWishlist = false;
        }
        else {
            btn_wishlist.setText("Add To Wishlist");
            addToWishlist = true;
        }

        // Assign button correct text and action
        if (User.isLoggedIn() && User.ExistsInCart(selectedBook)) {
            btn_cart.setText("Remove From Cart");
            addToCart = false;
        }
        else {
            btn_cart.setText("Add To Cart");
            addToCart = true;
        }
    }

    /**
     * Update the like status both logically and graphically for the book viewer.
     * @param book The book to check
     */
    @FXML
    public void setLikeStatus(Book book) {
        ImageView likeView = null;
        ImageView dislikeView = null;

        switch(getBookStatus(book)) {
            case Neutral:
                likeView = new ImageView(greyLike);
                dislikeView = new ImageView(greyDislike);
                break;
            case Dislike:
                likeView = new ImageView(greyLike);
                dislikeView = new ImageView(redDislike);
                break;
            case Like:
                likeView = new ImageView(greenLike);
                dislikeView = new ImageView(greyDislike);
                break;
        }

        //Set the graphics for likeButton
        likeView.setFitHeight(30);
        likeView.setPreserveRatio(true);
        likeButton.setGraphic(likeView);

        //Set the graphics for dislikeButton
        dislikeView.setFitHeight(30);
        dislikeView.setPreserveRatio(true);
        dislikeButton.setGraphic(dislikeView);
    }

    /**
     * Returns book status.
     * @param book Book
     * @return Like, Dislike, or Neutral LikeStatus
     */
    private LikeStatus getBookStatus(Book book) {
        if (User.ExistsInPreferences(book) == -1) {
            return LikeStatus.Neutral;
        }

        return User.ExistsInPreferences(book) == 1 ? LikeStatus.Like : LikeStatus.Dislike;
    }

    /**
     * Load the description text from the file.
     * @param title The full book title (NO spaces).
     * @return A string containing all text from the description txt file.
     */
    private String loadDescription(String title) {
        try {
            File file = new File("src/main/resources/Descriptions/" + title + ".txt");
            Scanner sc = new Scanner(file, "utf-8");
            StringBuilder fileContent = new StringBuilder();
            while (sc.hasNextLine()) {
                fileContent.append(sc.nextLine()).append(System.lineSeparator());
            }

            return fileContent.toString();
        } catch (Exception e) {
            System.out.println("Description file could not be read. " + e.getMessage());
        }
        return "";
    }

    /**
     * Adds the selected book to the cart
     * @param event This event runs when "Add to Cart" button is pressed.
     */
    @FXML
    public void handleCart(ActionEvent event) {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        }
        else if (User.ExistsInCart(selectedBook) && addToCart) {
            Tools.ShowPopup(1,"Cannot add duplicate","This book is already in your cart");
        }
        else if (selectedBook != null && addToCart) {
            User.AddToCart(selectedBook);
            updateCartView(selectedBook); // Update the cart view immediately
            btn_cart.setText("Remove From Cart");
            addToCart = false;
        }
        else if (!addToCart) {
            User.RemoveFromCart(selectedBook);
            btn_cart.setText("Add To Cart");
            addToCart = true;
        }
        else {
            Tools.ShowPopup(0, "Error", "No book selected to add to cart.");
        }
    }

    /**
     * Adds the selected book to the wishlist
     * @param event This event runs when "Add to Wishlist" button is pressed.
     */
    @FXML
    void handleWishlist(ActionEvent event) {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        }
        else if (User.ExistsInWishlist(selectedBook) && addToWishlist) {
            Tools.ShowPopup(1,"Cannot add duplicate","This book is already in your wishlist");
        }
        else if (selectedBook != null && addToWishlist) {
            User.AddToWishlist(selectedBook);
            updateWishlistView(selectedBook); // Update the wishlist view immediately
            btn_wishlist.setText("Remove From Wishlist");
            addToWishlist = false;
        }
        else if (!addToWishlist) {
            User.RemoveFromWishlist(selectedBook);
            btn_wishlist.setText("Add To Wishlist");
            addToWishlist = true;
        }
        else {
            Tools.ShowPopup(0, "Error", "No book selected to add to wishlist.");
        }
    }

    /**
     * Update the cart view
     * @param selectedBook The book to be added to the cart
     */
    private void updateCartView(Book selectedBook) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bkgroup/worm/controllers/Cart.fxml"));
        try {
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.addCartItem(selectedBook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the wishlist view
     * @param selectedBook The book to be added to the wishlist
     */
    private void updateWishlistView(Book selectedBook) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bkgroup/worm/controllers/Cart.fxml"));
        try {
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.addWishlistItem(selectedBook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put book viewer in the center of the current pane.
     */
    private void centerViewerPane() {
        double centerX = (bookViewer.getWidth() - viewerPane.getPrefWidth()) / 2;
        double centerY = (bookViewer.getHeight() - viewerPane.getPrefHeight()) / 2;
        viewerPane.setLayoutX(centerX);
        viewerPane.setLayoutY(centerY);
    }

    /**
     * Complete all initialization for the graphics of the book viewer
     */
    public void initializeViewerPane() {
        centerViewerPane();//Make sure cart viewer is centered

        //Set the close button to the appropriate icon
        Image closeImg = new Image("images/Close.png");
        ImageView closeView = new ImageView(closeImg);
        closeView.setFitHeight(25);
        closeView.setPreserveRatio(true);
        closeButton.setGraphic(closeView);
    }

    /**
     * Close the book viewer panel
     */
    @FXML
    public void clickCloseBookViewer() {
        bookViewer.setVisible(false);
    }

    /**
     * This method runs when the like button is clicked.
     * Based on the current like status of the book, it updates to the new like status
     * both graphically and logically.
     * @param event Button press information.
     */
    @FXML
    public void clickLikeButton(ActionEvent event) {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        }
        else if (getBookStatus(getSelectedBook()) == LikeStatus.Like) {
            // If the status is already set to like, and you click it again, it will go back to neutral
            User.RemoveFromPreferences(getSelectedBook());
            setLikeStatus(getSelectedBook());
        } else if (User.ExistsInPreferences(getSelectedBook()) != -1) {
            // Update status if book already has a preference
            User.RemoveFromPreferences(getSelectedBook());
            User.AddToPreferences(getSelectedBook(), true);
            setLikeStatus(getSelectedBook());
        } else {
            // If the status is neutral or dislike, and you click like, it will switch the status to "Like"
            User.AddToPreferences(getSelectedBook(), true);
            setLikeStatus(getSelectedBook());
        }
    }

    /**
     * This method runs when the dislike button is clicked.
     * Based on the current like status of the book, it updates to the new like status
     * both graphically and logically.
     * @param event Button press information.
     */
    @FXML
    public void clickDislikeButton(ActionEvent event) {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        } else if (getBookStatus(getSelectedBook()) == LikeStatus.Dislike) {
            // If the status is already set to dislike, and you click it again, it will go back to neutral
            User.RemoveFromPreferences(getSelectedBook());
            setLikeStatus(getSelectedBook());
        } else if (User.ExistsInPreferences(getSelectedBook()) != -1) {
            // Update status if book already has a preference
            User.RemoveFromPreferences(getSelectedBook());
            User.AddToPreferences(getSelectedBook(), false);
            setLikeStatus(getSelectedBook());
        } else {
            // If the status is neutral or like, and you click dislike, it will switch the status to "Dislike"
            User.AddToPreferences(getSelectedBook(), false);
            setLikeStatus(getSelectedBook());
        }
    }

    /**
     * Returns the Book currently selected
     * @return Currently selected Book
     */
    private Book getSelectedBook() {
        return selectedBook;
    }
}
