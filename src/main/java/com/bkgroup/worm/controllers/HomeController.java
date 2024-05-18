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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;

public class HomeController {
    private int BOOK_HEIGHT = 175;
    @FXML VBox sections;

    @FXML
    public void initialize() {
        // Populate local author section
        createSection("PNW Local Author");
        ResultSet local_authors = Query.select("book","title","author='Kristin Hannah'");
        createBookList(Objects.requireNonNull(Query.resultSetToArrayList(local_authors)));

        // Populate fiction section
        createSection("Fiction");
        ResultSet fiction = Query.populateGenre("Fiction");
        createBookList(Objects.requireNonNull(Query.resultSetToArrayList(fiction)));

        // Populate children's books section
        createSection("Children");
        ResultSet children = Query.populateGenre("Children");
        createBookList(Objects.requireNonNull(Query.resultSetToArrayList(children)));

        // Populate fantasy book section
        createSection("Fantasy");
        ResultSet fantasy = Query.populateGenre("Fantasy");
        createBookList(Objects.requireNonNull(Query.resultSetToArrayList(fantasy)));

        // Populate young adult book section
        createSection("Young Adult");
        ResultSet young_adult = Query.populateGenre("Young Adult");
        createBookList(Objects.requireNonNull(Query.resultSetToArrayList(young_adult)));
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
     * Create a new HBox that displays the images of all books
     * @param content The list of book content used to create the list
     */
    private void createBookList(ArrayList<String[]> content) {
        HBox hBox = new HBox();
        hBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setSpacing(5);

        ScrollPane scroll = new ScrollPane();
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);//Never display a vertical scroll bar
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);//Only display a horizontal scroll bar if it is needed
        scroll.setVmax(0);//Make sure you can't scroll up and down
        scroll.setFitToWidth(true);
        scroll.prefWidthProperty().bind(sections.widthProperty());
        scroll.setMaxWidth(Double.MAX_VALUE);
        scroll.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0;");

        scroll.setContent(hBox);
        sections.getChildren().add(scroll);

        /* TODO REMOVE AFTER DEBUGGING
        System.out.println("Sections width: " + sections.getWidth());
        System.out.println("scroll width: " + scroll.getWidth());
        System.out.println("hbox width: " + hBox.getWidth());
        */

        String title;
        for(String[] i: content) {
            try {
                title = i[0];
                title = title.replaceAll(" ", "");

                ImageView imageView = new ImageView();

                String path = "BookCovers/" + title + ".jpg";
                Image image = new Image("BookCovers/" + title + ".jpg"); // Replace "path/to/your/image.jpg" with the actual path to your image file
                imageView.setImage(image);
                // Optionally, you can set additional properties such as fit width and fit height
                double desiredHeight = BOOK_HEIGHT; // Set your desired height
                double scaleFactor = desiredHeight / image.getHeight();
                double scaledWidth = image.getWidth() * scaleFactor;

                imageView.setFitWidth(scaledWidth); // Set the width of the ImageView
                imageView.setFitHeight(desiredHeight); // Set the height of the ImageView

                hBox.getChildren().add(imageView);
            }
            catch (Exception e) {
                System.out.printf("Error loading cover for \"%s\" : %s\n",i[0], e.getMessage());
            }
        }
    }
}
