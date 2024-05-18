package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.Query;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class SearchController {

    public TitledPane genres;
    public Slider pageCount, yearMin, yearMax;
    public Label searchButton;
    public TextField searchField;
    public ScrollPane output;
    public AnchorPane base;
    FlowPane fp = new FlowPane();
    ScrollPane sp = new ScrollPane();


    @FXML
    void initialize () {
        base.heightProperty().addListener(event -> {
            sp.setMinHeight(base.getHeight()-300);
        });

        fp.setMinSize(output.getPrefWidth(), output.getPrefHeight());
        output.setContent(fp);

        output.widthProperty().addListener(event -> fp.setPrefWidth(output.getWidth()));
        output.heightProperty().addListener(event -> fp.setPrefHeight(output.getHeight()));

        ArrayList<String[]> result = Query.resultSetToArrayList(Query.select("genre", "DISTINCT genre"));
        VBox vbox = new VBox();
        for (String[] str : result) {
            CheckBox cb = new CheckBox(str[0]);
            vbox.getChildren().add(cb);
        }
        sp.setMinHeight(base.getHeight()-300);
        sp.setContent(vbox);

        genres.setContent(sp);

        searchButton.setOnMouseClicked(event -> {

        });
    }
}
