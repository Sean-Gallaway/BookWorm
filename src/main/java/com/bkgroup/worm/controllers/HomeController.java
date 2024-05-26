package com.bkgroup.worm.controllers;

import com.bkgroup.worm.Book;
import com.bkgroup.worm.User;
import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.Tools;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class HomeController {
    private final static HashMap<String,ArrayList<String[]>> cache = new HashMap<>();
    private static boolean INITIALIZED;
    private Book selectedBook;
    private int BOOK_HEIGHT = 175;
    @FXML VBox sections;

    //Book Viewer Window
    @FXML AnchorPane bookViewer;
    @FXML StackPane viewerPane;
    @FXML ImageView viewerCover;
    @FXML Label viewerTitle;
    @FXML Label viewerSeries;
    @FXML Label viewerAuthor;
    @FXML Label viewerGenre;
    @FXML Label viewerDescription;

    private enum LikeStatus {Dislike, Neutral, Like};
    private LikeStatus like;//Keeps track of the like status for the current book being displayed.
    private Image greyLike = new Image("images/GreyThumbUp.png");
    private Image greenLike = new Image("images/GreenThumbUp.png");
    private Image greyDislike = new Image("images/GreyThumbDown.png");
    private Image redDislike = new Image("images/RedThumbDown.png");

    //Book Viewer Buttons
    @FXML Button likeButton;
    @FXML Button dislikeButton;
    @FXML Button closeButton;



    @FXML
    public void initialize() {
        // Load database objects into cache if none are loaded
        if (!INITIALIZED) {
            populateSections();
            initializeViewerPane();
        }

        // Populate local author section
        createSection("PNW Local Author");
        createBookList(cache.get("LOCAL_AUTHORS"));

        // Populate fiction section
        createSection("Fiction");
        createBookList(cache.get("FICTION"));

        // Populate children's books section
        createSection("Children");
        createBookList(cache.get("CHILDREN"));

        // Populate young adult book section
        createSection("Young Adult");
        createBookList(cache.get("YOUNG_ADULT"));

        // Populate fantasy book section
        createSection("Fantasy");
        createBookList(cache.get("FANTASY"));

        // Populate science fiction book section
        createSection("Science Fiction");
        createBookList(cache.get("SCIENCE_FICTION"));


        // Populate Mystery book section
        createSection("Mystery");
        createBookList(cache.get("MYSTERY"));

        // Populate thriller book section
        createSection("Thriller");
        createBookList(cache.get("THRILLER"));

        INITIALIZED = true;
    }

    /**
     * Complete all initialization for the graphics of the book viewer
     */
    private void initializeViewerPane() {
        centerViewerPane();

        setLikeStatus(LikeStatus.Neutral);

        Image closeImg = new Image("images/Close.png");
        ImageView closeView = new ImageView(closeImg);
        closeView.setFitHeight(25);
        closeView.setPreserveRatio(true);
        closeButton.setGraphic(closeView);

    }

    private void centerViewerPane() {
        double centerX = (bookViewer.getWidth() - viewerPane.getPrefWidth()) / 2;
        double centerY = (bookViewer.getHeight() - viewerPane.getPrefHeight()) / 2;
        viewerPane.setLayoutX(centerX);
        viewerPane.setLayoutY(centerY);
    }

    /**
     * Creates a new Label for a section title and adds it to the sections VBox
     * @param sectionTitle title
     */
    private void createSection(String sectionTitle) {
        Label l = new Label(sectionTitle);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sections.getChildren().add(l);
    }

    /**
     * Populates cache with specified genres
     */
    private void populateSections()
    {
        cache.put("LOCAL_AUTHORS", Query.resultSetToArrayList(Query.select("book","*","author='Kristin Hannah'")));
        cache.put("FICTION", Query.resultSetToArrayList(Tools.populateGenre("Fiction")));
        cache.put("CHILDREN", Query.resultSetToArrayList(Tools.populateGenre("Children")));
        cache.put("FANTASY", Query.resultSetToArrayList(Tools.populateGenre("Fantasy")));
        cache.put("YOUNG_ADULT", Query.resultSetToArrayList(Tools.populateGenre("Young Adult")));
        cache.put("SCIENCE_FICTION", Query.resultSetToArrayList(Tools.populateGenre("Science Fiction")));
        cache.put("MYSTERY", Query.resultSetToArrayList(Tools.populateGenre("Mystery")));
        cache.put("THRILLER", Query.resultSetToArrayList(Tools.populateGenre("Thriller")));
    }

    /**
     * Create a new HBox that displays the images of all books
     * @param content The list of book content used to create the list
     */
    private void createBookList(ArrayList<String[]> content) {
        // Create content area
        HBox hBox = createDisplayArea();

        // Populate area with data
        populateDisplayArea(content, hBox);
    }

    /**
     * Creates HBox with scroll bars for books to be displayed in
     * @return Hbox
     */
    private HBox createDisplayArea()
    {
        // Create display box
        HBox hBox = new HBox();
        hBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setSpacing(5);

        // Create scroll bar
        createScrollPane(hBox);

        return hBox;
    }

    /**
     * Creates scroll bar for specified HBox
     * @param hBox HBox
     */
    private void createScrollPane(HBox hBox) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);//Never display a vertical scroll bar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);//Only display a horizontal scroll bar if it is needed
        scrollPane.setVmax(0);//Make sure you can't scroll up and down
        scrollPane.setFitToWidth(true);
        scrollPane.prefWidthProperty().bind(sections.widthProperty());
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0;");
        scrollPane.setContent(hBox);
        sections.getChildren().add(scrollPane);
    }

    /**
     * Populates HBox with books and displays them for the user.
     * @param content Book data
     * @param hBox HBox section
     */
    private void populateDisplayArea(ArrayList<String[]> content, HBox hBox)
    {
        for(String[] i: content) {
            try {
                String title = i[1].replaceAll(" ","").replaceAll("'", "").replaceAll("-", "");

                // Set book cover image
                ImageView imageView = new ImageView();
                Image image = new Image("BookCovers/" + title + ".jpg"); // Replace "path/to/your/image.jpg" with the actual path to your image file
                imageView.setImage(image);

                // Optionally, you can set additional properties such as fit width and fit height
                double desiredHeight = BOOK_HEIGHT;
                double scaleFactor = desiredHeight / image.getHeight();
                double scaledWidth = image.getWidth() * scaleFactor;

                // Set ImageView width and height
                imageView.setFitWidth(scaledWidth);
                imageView.setFitHeight(desiredHeight);

                //Set the click listener for clicking on the book cover
                imageView.setOnMouseClicked(event -> {
                    clickBook(i, image, title);
                });

                // Add book image to hBox
                hBox.getChildren().add(imageView);
            }
            catch (Exception e) {
                System.out.printf("Error loading cover for \"%s\" : %s\n",i[0], e.getMessage());
            }
        }
    }

    /**
     * Show the ClickView menu, loading the book information of the book that was clicked.
     * @param content
     */
    private void clickBook(String[] content, Image bookCover, String title) {
        bookViewer.setVisible(true);

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
        String genreText = "Genre: ";
        for (String[] i : genres) {
            genreText += i[0] + ", ";
        }
        genreText = genreText.substring(0, genreText.length() - 2); // Remove the final comma and space
        viewerGenre.setText(genreText);

        viewerDescription.setWrapText(true); // Make sure the text wraps if it gets too long.
        viewerDescription.setText(loadDescription(title));

        // Store the selected book reference
        selectedBook = new Book(Integer.parseInt(content[Query.BookAttributes.bookID.ordinal()]));
    }


    @FXML
    public void clickCloseBookViewer(ActionEvent event) {
        bookViewer.setVisible(false);
        setLikeStatus(LikeStatus.Neutral);
    }

    @FXML
    public void clickLikeButton(ActionEvent event) {
        if(like == LikeStatus.Like) {
            //If the status is already set to like, and you click it again, it will go back to neutral
            setLikeStatus(LikeStatus.Neutral);
        } else {
            //If the status is neutral or dislike, and you click like, it will switch the status to "Like"
            setLikeStatus(LikeStatus.Like);
        }
    }

    @FXML
    public void clickDislikeButton(ActionEvent event) {
        if(like == LikeStatus.Dislike) {
            //If the status is already set to dislike, and you click it again, it will go back to neutral
            setLikeStatus(LikeStatus.Neutral);
        } else {
            //If the status is neutral or like, and you click dislike, it will switch the status to "Dislike"
            setLikeStatus(LikeStatus.Dislike);
        }
    }

    /**
     * Update the like status both logically and graphically for the book viewer
     * @param status The new like status
     */
    @FXML
    public void setLikeStatus(LikeStatus status) {
        like = status;//Update the status variable

        ImageView likeView = null;
        ImageView dislikeView = null;

        switch(like) {
            case Neutral:
                likeView = new ImageView(greyLike);
                dislikeView = new ImageView(greyDislike);
            break;
            case Like:
                likeView = new ImageView(greenLike);
                dislikeView = new ImageView(greyDislike);
                break;
            case Dislike:
                likeView = new ImageView(greyLike);
                dislikeView = new ImageView(redDislike);
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

    // Method to update the cart view
    private void updateCartView(Book selectedBook) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bkgroup/worm/controllers/Cart.fxml"));
        try {
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.addCartItem(selectedBook, 1); // Use appropriate quantity
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Method to get the currently selected book
    private Book getSelectedBook() {
        return selectedBook;
    }



}
