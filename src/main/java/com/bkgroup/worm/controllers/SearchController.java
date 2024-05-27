package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.DatabaseConnection;
import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.SearchBuilder;
import com.bkgroup.worm.utils.Tools;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class SearchController {
    public TitledPane genres;
    public Slider pageCount, yearMin, yearMax;
    public Label searchButton;
    public TextField searchField;
    public ScrollPane output;
    public AnchorPane base;
    FlowPane fp = new FlowPane();
    ScrollPane sp = new ScrollPane();
    ArrayList<String> selectedGenres = new ArrayList<>();


    /**
     * Builds the Search Controller window
     */
    @FXML
    void initialize () {
        // height of the scroll pane must scale when the window does.
        base.heightProperty().addListener(event -> sp.setMinHeight(base.getHeight()-300));

        // set up the content of the scroll pane.
        fp.setMinSize(output.getPrefWidth(), output.getPrefHeight());
        output.setContent(fp);

        // the flow pane scaling.
        output.widthProperty().addListener(event -> fp.setPrefWidth(output.getWidth()-20));
        output.heightProperty().addListener(event -> fp.setMinHeight(output.getHeight()));

        // build the genre combo box
        ArrayList<String[]> result = Query.resultSetToArrayList(Query.select("genre", "DISTINCT genre"));
        VBox vbox = new VBox();
        if (result != null) {
            for (String[] str : result) {
                CheckBox cb = new CheckBox(str[0]);
                cb.setOnAction(event -> {
                    if (cb.isSelected()) {
                        selectedGenres.add(cb.getText());
                    }
                    else {
                        selectedGenres.remove(cb.getText());
                    }
                });
                vbox.getChildren().add(cb);
            }
        }
        sp.setMinHeight(base.getHeight()-300);
        sp.setContent(vbox);
        genres.setContent(sp);

        // search button functionality
        searchButton.setOnMouseClicked(event -> searchQuery());
    }

    /**
     * Builds the search query and executes it.
     */
    public void searchQuery () {
        // genre search
        fp.getChildren().clear();

        // build the search
        String[] searchTerms = searchField.getText().split(" ");
        SearchBuilder search = new SearchBuilder()
                .fullName(searchTerms)
                .publication((int)yearMin.getValue(), (int)yearMax.getValue())
                .genre(selectedGenres.toArray(new String[0]))
                .pageCount((int)pageCount.getValue());
        // add the partial search terms
        for (String str : searchTerms) {
            search = search.partialName(str);
        }

        // query the database
        ArrayList<String[]> result;
        try {
            // create a statement
            Statement st = DatabaseConnection.db().createStatement();
            result = Query.resultSetToArrayList(st.executeQuery(search.construct()));

            // iterate through the result set
            if (result != null) {
                for (String[] str : result) {
                    // create the search card
                    FXMLLoader loader = new FXMLLoader(Tools.class.getResource("/com/bkgroup/worm/controllers/SearchCard.fxml"));
                    fp.getChildren().add(loader.load());
                    String tempStr = str[1].replaceAll(" ","").replaceAll("'", "").replaceAll("-", "");
                    try {
                        // finish up the card by fetching the books Image to use for it.
                        Image img = new Image(Objects.requireNonNull(SearchController.class.getResourceAsStream("/BookCovers/" + tempStr + ".jpg")));
                        SearchCard sc = loader.getController();
                        sc.setup(img, str[1], Integer.parseInt(str[2]));
                    }
                    catch (Exception e) {
                        System.out.println(str[0] + " " + str[1] + " " + str[2]);
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
