package com.bkgroup.worm.controllers;

import com.bkgroup.worm.App;
import com.bkgroup.worm.utils.DisplayCreator;
import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.Tools;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bkgroup.worm.utils.DatabaseConnection.db;

public class HomeController {
    private final static HashMap<String,ArrayList<String[]>> cache = new HashMap<>();
    private static boolean INITIALIZED;
    @FXML VBox sections;
    public static int BOOK_HEIGHT = 300;

    /**
     * Initializes home page, adding books to a cache and displaying those books in genre-appropriate displays.
     */
    @FXML
    public void initialize() {
        // Load database objects into cache if none are loaded
        if (!INITIALIZED) {
            populateSections();
        }

        // Add CSS in the hackiest way possible
        sections.setStyle("-fx-background-color: #c0bfbc");

        // Initialize book viewer icons
        App.oc.initializeViewerPane();

        // Populate local author section
        DisplayCreator.createSection("PNW Local Author",sections);
        DisplayCreator.createBookList(cache.get("LOCAL_AUTHORS"),sections);

        // Populate fiction section
        DisplayCreator.createSection("Fiction",sections);
        DisplayCreator.createBookList(cache.get("FICTION"),sections);

        // Populate children's books section
        DisplayCreator.createSection("Children",sections);
        DisplayCreator.createBookList(cache.get("CHILDREN"),sections);

        // Populate young adult book section
        DisplayCreator.createSection("Young Adult",sections);
        DisplayCreator.createBookList(cache.get("YOUNG_ADULT"),sections);

        // Populate fantasy book section
        DisplayCreator.createSection("Fantasy",sections);
        DisplayCreator.createBookList(cache.get("FANTASY"),sections);

        // Populate science fiction book section
        DisplayCreator.createSection("Science Fiction",sections);
        DisplayCreator.createBookList(cache.get("SCIENCE_FICTION"),sections);


        // Populate Mystery book section
        DisplayCreator.createSection("Mystery",sections);
        DisplayCreator.createBookList(cache.get("MYSTERY"),sections);

        // Populate thriller book section
        DisplayCreator.createSection("Thriller",sections);
        DisplayCreator.createBookList(cache.get("THRILLER"),sections);

        INITIALIZED = true;
    }

    /**
     * Populates cache with specified genres
     */
    private void populateSections()
    {
        cache.put("LOCAL_AUTHORS", Query.resultSetToArrayList(Query.select("book","*","author='Kristin Hannah'")));
        cache.put("FICTION", Query.resultSetToArrayList(populateGenre("Fiction")));
        cache.put("CHILDREN", Query.resultSetToArrayList(populateGenre("Children")));
        cache.put("FANTASY", Query.resultSetToArrayList(populateGenre("Fantasy")));
        cache.put("YOUNG_ADULT", Query.resultSetToArrayList(populateGenre("Young Adult")));
        cache.put("SCIENCE_FICTION", Query.resultSetToArrayList(populateGenre("Science Fiction")));
        cache.put("MYSTERY", Query.resultSetToArrayList(populateGenre("Mystery")));
        cache.put("THRILLER", Query.resultSetToArrayList(populateGenre("Thriller")));
    }

    /**
     * Grabs all books from specified genre and returns ResultSet.
     * @param genre genre to search for
     * @return ResultSet of all books in specified genre
     */
    public static ResultSet populateGenre(String genre) {
        try {
            String query = String.format("SELECT * FROM Book b JOIN Genre g ON b.bookID = g.bookID WHERE g.genre = '%s'", genre);
            return db().createStatement().executeQuery(query);
        }
        catch (SQLException e) {
            System.err.println("SQL ERROR IN \"populateGenre()\":\"Tools.java\"");
            return null;
        }
    }
}
