package com.bkgroup.worm.controllers;

import com.bkgroup.worm.App;
import com.bkgroup.worm.utils.DisplayCreator;
import com.bkgroup.worm.utils.Query;

import com.bkgroup.worm.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;

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

        // Initialize book viewer icons
        App.oc.initializeViewerPane();

        // Populate local author section
        DisplayCreator.createSection("PNW Local Author",sections,1);
        DisplayCreator.createBookList(cache.get("LOCAL_AUTHORS"),sections,1);

        // Populate fiction section
        DisplayCreator.createSection("Fiction",sections,1);
        DisplayCreator.createBookList(cache.get("FICTION"),sections,1);

        // Populate children's books section
        DisplayCreator.createSection("Children",sections,1);
        DisplayCreator.createBookList(cache.get("CHILDREN"),sections,1);

        // Populate young adult book section
        DisplayCreator.createSection("Young Adult",sections,1);
        DisplayCreator.createBookList(cache.get("YOUNG_ADULT"),sections,1);

        // Populate fantasy book section
        DisplayCreator.createSection("Fantasy",sections,1);
        DisplayCreator.createBookList(cache.get("FANTASY"),sections,1);

        // Populate science fiction book section
        DisplayCreator.createSection("Science Fiction",sections,1);
        DisplayCreator.createBookList(cache.get("SCIENCE_FICTION"),sections,1);


        // Populate Mystery book section
        DisplayCreator.createSection("Mystery",sections,1);
        DisplayCreator.createBookList(cache.get("MYSTERY"),sections,1);

        // Populate thriller book section
        DisplayCreator.createSection("Thriller",sections,1);
        DisplayCreator.createBookList(cache.get("THRILLER"),sections,1);

        INITIALIZED = true;
    }

    /**
     * Populates cache with specified section criteria.
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
}
