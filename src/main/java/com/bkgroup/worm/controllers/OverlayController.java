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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.File;
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

    /**
     * Show the ClickView menu, loading the book information of the book that was clicked.
     * @param content
     */
    public void clickBook(String[] content, Image bookCover, String title) {
        // Make visible and set size
        bookViewer.setVisible(true);
        bookViewer.setPrefWidth(App.stage.getWidth()-35);
        bookViewer.setPrefHeight(App.stage.getHeight()-35);

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
        viewerAuthor.setText("by " + content[4]);

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

        setLikeStatus(selectedBook);
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
            Scanner sc = new Scanner(file);

            StringBuilder fileContent = new StringBuilder();
            while (sc.hasNextLine())
                fileContent.append(sc.nextLine()).append(System.lineSeparator());

            return fileContent.toString();
        } catch (Exception e) {
            System.out.println("Description file could not be read. " + e.getMessage());
        }
        return "";
    }

    @FXML
    public void handleAddToCart(ActionEvent event) {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        } else if (selectedBook != null) {
            User.AddToCart(selectedBook);
            updateCartView(selectedBook); // Update the cart view immediately
        } else {
            Tools.ShowPopup(4, "Error", "No book selected to add to cart.");
        }
    }

    @FXML
    void handleAddToWishlist(ActionEvent event) {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        } else if (selectedBook != null) {
            User.AddToWishlist(selectedBook);
        } else {
            Tools.ShowPopup(4, "Error", "No book selected to add to wishlist.");
        }
    }

    // Method to update the cart view
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
        centerViewerPane();
        Image closeImg = new Image("images/Close.png");
        ImageView closeView = new ImageView(closeImg);
        closeView.setFitHeight(25);
        closeView.setPreserveRatio(true);
        closeButton.setGraphic(closeView);
    }

    @FXML
    public void clickCloseBookViewer() {
        bookViewer.setVisible(false);
    }

    @FXML
    public void clickLikeButton() {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        }
        else if (getBookStatus(getSelectedBook()) == LikeStatus.Like) {
            //If the status is already set to like, and you click it again, it will go back to neutral
            User.RemoveFromPreferences(getSelectedBook());
            setLikeStatus(getSelectedBook());
        }
        else if (User.ExistsInPreferences(getSelectedBook()) != -1) {
            // Update status if book already has a preference
            User.RemoveFromPreferences(getSelectedBook());
            User.AddToPreferences(getSelectedBook(),true);
            setLikeStatus(getSelectedBook());
        }
        else {
            //If the status is neutral or dislike, and you click like, it will switch the status to "Like"
            User.AddToPreferences(getSelectedBook(),true);
            setLikeStatus(getSelectedBook());
        }
    }

    @FXML
    public void clickDislikeButton() {
        if (!User.isLoggedIn()) {
            User.LoginPrompt();
        }
        else if (getBookStatus(getSelectedBook()) == LikeStatus.Dislike) {
            //If the status is already set to dislike, and you click it again, it will go back to neutral
            User.RemoveFromPreferences(getSelectedBook());
            setLikeStatus(getSelectedBook());
        }
        else if (User.ExistsInPreferences(getSelectedBook()) != -1) {
            // Update status if book already has a preference
            User.RemoveFromPreferences(getSelectedBook());
            User.AddToPreferences(getSelectedBook(),false);
            setLikeStatus(getSelectedBook());
        }
        else {
            //If the status is neutral or like, and you click dislike, it will switch the status to "Dislike"
            User.AddToPreferences(getSelectedBook(),false);
            setLikeStatus(getSelectedBook());
        }
    }

    // Method to get the currently selected book
    private Book getSelectedBook() {
        return selectedBook;
    }
}
