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


    @FXML
    void initialize () {
        base.heightProperty().addListener(event -> sp.setMinHeight(base.getHeight()-300));

        fp.setMinSize(output.getPrefWidth(), output.getPrefHeight());
        output.setContent(fp);

        output.widthProperty().addListener(event -> fp.setPrefWidth(output.getWidth()-20));
        output.heightProperty().addListener(event -> fp.setMinHeight(output.getHeight()));

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

        searchButton.setOnMouseClicked(event -> searchQuery());
    }

    public void searchQuery () {
        // genre search
        fp.getChildren().clear();

        String[] searchTerms = searchField.getText().split(" ");
        SearchBuilder search = new SearchBuilder()
                .fullName(searchTerms)
                .publication((int)yearMin.getValue(), (int)yearMax.getValue())
                .genre(selectedGenres.toArray(new String[0]))
                .pageCount((int)pageCount.getValue());
        for (String str : searchTerms) {
            search.partialName(str);
        }
        ArrayList<String[]> result;

        try {
            Statement st = DatabaseConnection.db().createStatement();
            result = Query.resultSetToArrayList(st.executeQuery(search.construct()));
            if (result != null) {
                for (String[] str : result) {
                    FXMLLoader loader = new FXMLLoader(Tools.class.getResource("/com/bkgroup/worm/controllers/SearchCard.fxml"));
                    fp.getChildren().add(loader.load());
                    String tempStr = str[1].replaceAll(" ","").replaceAll("'", "").replaceAll("-", "");
                    try {
                        Image img = new Image(SearchController.class.getResourceAsStream(Objects.requireNonNull("/BookCovers/"+tempStr+".jpg")));
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
