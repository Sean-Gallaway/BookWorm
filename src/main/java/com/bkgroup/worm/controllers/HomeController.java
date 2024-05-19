package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.Query;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeController {
    private final static HashMap<String,ArrayList<String[]>> cache = new HashMap<>();
    private static boolean INITIALIZED;
    private int BOOK_HEIGHT = 175;
    @FXML VBox sections;

    @FXML
    public void initialize() {
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

        // Populate fantasy book section
        createSection("Fantasy");
        createBookList(cache.get("FANTASY"));

        // Populate young adult book section
        createSection("Young Adult");
        createBookList(cache.get("YOUNG_ADULT"));

        INITIALIZED = true;
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
        cache.put("LOCAL_AUTHORS", Query.resultSetToArrayList(Query.select("book","title","author='Kristin Hannah'")));
        cache.put("FICTION", Query.resultSetToArrayList(Query.populateGenre("Fiction")));
        cache.put("CHILDREN", Query.resultSetToArrayList(Query.populateGenre("Children")));
        cache.put("FANTASY", Query.resultSetToArrayList(Query.populateGenre("Fantasy")));
        cache.put("YOUNG_ADULT", Query.resultSetToArrayList(Query.populateGenre("Young Adult")));
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
                String title = i[0].replaceAll(" ","");

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

                // Add book image to hBox
                hBox.getChildren().add(imageView);
            }
            catch (Exception e) {
                System.out.printf("Error loading cover for \"%s\" : %s\n",i[0], e.getMessage());
            }
        }
    }
}
