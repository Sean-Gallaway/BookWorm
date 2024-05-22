package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.Query;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private int BOOK_HEIGHT = 175;
    @FXML VBox sections;

    @FXML AnchorPane bookViewer;
    @FXML StackPane viewerPane;
    @FXML ImageView viewerCover;
    @FXML Label viewerTitle;
    @FXML Label viewerSeries;
    @FXML Label viewerAuthor;
    @FXML Label viewerGenre;
    @FXML Label viewerDescription;


    @FXML
    public void initialize() {
        centerViewerPane();

        // Load database objects into cache if none are loaded
        if (!INITIALIZED)
        {
            populateSections();
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


        // Populate Myster book section
        createSection("Mystery");
        createBookList(cache.get("MYSTERY"));

        // Populate thriller book section
        createSection("Thriller");
        createBookList(cache.get("THRILLER"));



        INITIALIZED = true;


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
        cache.put("FICTION", Query.resultSetToArrayList(Query.populateGenre("Fiction")));
        cache.put("CHILDREN", Query.resultSetToArrayList(Query.populateGenre("Children")));
        cache.put("FANTASY", Query.resultSetToArrayList(Query.populateGenre("Fantasy")));
        cache.put("YOUNG_ADULT", Query.resultSetToArrayList(Query.populateGenre("Young Adult")));
        cache.put("SCIENCE_FICTION", Query.resultSetToArrayList(Query.populateGenre("Science Fiction")));
        cache.put("MYSTERY", Query.resultSetToArrayList(Query.populateGenre("Mystery")));
        cache.put("THRILLER", Query.resultSetToArrayList(Query.populateGenre("Thriller")));
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


        //Set the book cover
        viewerCover.setImage(bookCover);
        double desiredHeight = BOOK_HEIGHT + 100;
        double scaleFactor = desiredHeight / bookCover.getHeight();
        double scaledWidth = bookCover.getWidth() * scaleFactor;
        viewerCover.setFitWidth(scaledWidth);
        viewerCover.setFitHeight(desiredHeight);

        //Set the title
        viewerTitle.setText(content[1]);

        //Set the series
        if(content[2].equals("NULL")) {//If it's not part of a series, disable the label
            viewerSeries.setVisible(false);
        } else {//If it is part of a series
            viewerSeries.setVisible(true);
            viewerSeries.setText(content[2]);
        }

        //Set the author
        viewerAuthor.setText("by " + content[3]);

        //Set the genres
        ArrayList<String[]> genres = Query.resultSetToArrayList(Query.select("genre", "genre", Query.where("bookID", content[0])));
        String genreText = "Genre: ";
        for(String[] i: genres) {
            genreText += i[0] + ", ";
        }
        genreText = genreText.substring(0, genreText.length()-2);//Remove the final comma and space
        viewerGenre.setText(genreText);

        viewerDescription.setWrapText(true);//Make sure the text wraps if it gets too long.
        viewerDescription.setText(loadDescription(title));

    }

    @FXML
    public void clickCloseBookViewer(ActionEvent event) {
        bookViewer.setVisible(false);
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
}
