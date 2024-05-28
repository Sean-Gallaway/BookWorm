package com.bkgroup.worm.utils;

import com.bkgroup.worm.App;
import com.bkgroup.worm.controllers.HomeController;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class DisplayCreator {

    /**
     * Creates a new Label for a section title and adds it to the specified VBox.
     * @param sectionTitle Title
     * @param box Vbox
     */
    public static void createSection(String sectionTitle, VBox box) {
        Label label = new Label(" " + sectionTitle);
        label.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 28));
        box.getChildren().add(label);
    }

    /**
     * Create a new HBox that displays the images of all books.
     * @param content The list of book content used to create the list
     * @param box VBox
     */
    public static void createBookList(ArrayList<String[]> content, VBox box) {
        // Create content area
        HBox hBox = createDisplayArea(box);

        // Populate area with data
        populateDisplayArea(content, hBox);
    }

    /**
     * Creates HBox with scroll bars for books to be displayed in.
     * @param box Vbox
     * @return HBox
     */
    private static HBox createDisplayArea(VBox box)
    {
        // Create display box
        HBox hBox = new HBox();
        hBox.setPrefHeight(316);
        hBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setSpacing(16);

        // Create scroll bar
        createScrollPane(hBox, box);

        return hBox;
    }

    /**
     * Creates scroll bar for specified HBox.
     * @param hBox HBox
     * @param box VBox
     */
    private static void createScrollPane(HBox hBox, VBox box) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);//Never display a vertical scroll bar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);//Only display a horizontal scroll bar if it is needed
        scrollPane.setVmax(0);//Make sure you can't scroll up and down
        scrollPane.setFitToWidth(true);
        scrollPane.prefWidthProperty().bind(box.widthProperty());
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-insets: 0;");
        scrollPane.setContent(hBox);
        box.getChildren().add(scrollPane);
    }

    /**
     * Populates HBox with books and displays them for the user.
     * @param content Book data
     * @param hBox HBox section
     */
    private static void populateDisplayArea(ArrayList<String[]> content, HBox hBox)
    {
        for(String[] i: content) {
            try {
                String title = i[1].replaceAll(" ","").replaceAll("'", "").replaceAll("-", "");

                // Set book cover image
                ImageView imageView = new ImageView();
                Image image = new Image("BookCovers/" + title + ".jpg"); // Replace "path/to/your/image.jpg" with the actual path to your image file
                imageView.setImage(image);

                // Optionally, you can set additional properties such as fit width and fit height
                double desiredHeight = HomeController.BOOK_HEIGHT;
                double scaleFactor = desiredHeight / image.getHeight();
                double scaledWidth = image.getWidth() * scaleFactor;

                // Set ImageView width and height
                imageView.setFitWidth(scaledWidth);
                imageView.setFitHeight(desiredHeight);

                //Set the click listener for clicking on the book cover
                imageView.setOnMouseClicked(event -> App.oc.clickBook(i, image, title));

                // Add book image to hBox
                hBox.getChildren().add(imageView);
            }
            catch (Exception e) {
                System.out.printf("Error loading cover for \"%s\" : %s\n",i[0], e.getMessage());
            }
        }
    }
}
