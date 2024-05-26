package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.DatabaseConnection;
import com.bkgroup.worm.utils.Query;
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
import java.util.Arrays;
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
        StringBuilder queryOne = new StringBuilder("""
                \tSELECT a.bookID,
                \t    b.title,
                \t    COUNT(*) AS num
                \tFROM genre a
                \t    JOIN book b
                \t        ON a.bookID = b.bookID""");
        for (int a = 0; a < selectedGenres.size(); a++) {
            if (a == 0) {
                queryOne.append("\tWHERE\s");
            }
            queryOne.append(" a.genre = '");
            queryOne.append(selectedGenres.get(a));
            queryOne.append("'");
            if (a != selectedGenres.size()-1) {
                queryOne.append(" OR");
            }
        }
        queryOne.append("\n\tGROUP BY a.bookID");
        //

        // exact name search
        StringBuilder queryTwo = new StringBuilder("""
                \tSELECT bookID,
                \t    title,
                \t    CEIL(COUNT(*)*3.5) as num
                \tFROM book
                \tWHERE\s""");
        String[] searchTerms = searchField.getText().split(" ");
        for (int a = 0; a < searchTerms.length; a++) {
            queryTwo.append(" title LIKE '%");
            queryTwo.append(searchTerms[a]);
            queryTwo.append("%'");
            if (a != searchTerms.length-1) {
                queryTwo.append(" AND");
            }
        }
        queryTwo.append("\n\tGROUP BY bookID");
        //


        // partial name search
        StringBuilder queryThree = new StringBuilder();
        String temp = """
                \tSELECT bookID,
                \t    title,
                \t    COUNT(*) as num
                \tFROM book
                \tWHERE\s""";
        for (int a = 0; a < searchTerms.length; a++) {
            queryThree.append(temp);
            queryThree.append(" title LIKE '%");
            queryThree.append(searchTerms[a]);
            queryThree.append("%'");
            queryThree.append("\n\tGROUP BY bookID");
            if (a != searchTerms.length-1) {
                queryThree.append("\nUNION ALL\n\t");
            }
        }
        //

        // publication date search
        String queryFour = """
                \tSELECT bookID,
                \t    title,
                \t    COUNT(*) as num
                \tFROM book
                \tWHERE\s""" + " publicationDate >= '" +
                (int) yearMin.getValue() + "-01" + "-01'" +
                " AND publicationDate < '" +
                (int) yearMax.getValue() + "-01" + "-01'" +
                "\n\tGROUP BY bookID";
        //

        // combine the queries
        String subquery =
                queryOne + "\nUNION ALL\n" +
                queryTwo + "\nUNION ALL\n" +
                queryThree + "\nUNION ALL\n" +
                queryFour;

        StringBuilder finalQuery = new StringBuilder("""
                SELECT SUM(num) as tNum,
                r1.title,
                r1.bookID
                FROM (
                    """);
        finalQuery.append(subquery);
        // post-amble
        finalQuery.append("""
                \n) AS r1
                    JOIN book b2
                        ON r1.bookID = b2.bookID
                WHERE length >\s""");
        finalQuery.append((int) pageCount.getValue()).append("\n");
        finalQuery.append("""
                GROUP BY r1.title, r1.bookID
                HAVING tNum > 1
                ORDER BY tnum DESC;
                """);
        ArrayList<String[]> result;
        System.out.println(finalQuery);

        try {
            Statement st = DatabaseConnection.db().createStatement();
            result = Query.resultSetToArrayList(st.executeQuery(finalQuery.toString()));
            if (result != null) {
                for (String[] str : result) {
                    FXMLLoader loader = new FXMLLoader(Tools.class.getResource("/com/bkgroup/worm/controllers/SearchCard.fxml"));
                    fp.getChildren().add(loader.load());
                    String tempStr = str[1].replaceAll(" ","").replaceAll("'", "").replaceAll("-", "");
                    System.out.println(Arrays.toString(str));
                    try {
                        Image img = new Image(SearchController.class.getResourceAsStream(Objects.requireNonNull("/BookCovers/"+tempStr+".jpg")));
                        SearchCard sc = loader.getController();
                        sc.setup(img, str[1]);
                    }
                    catch (Exception e) {
                        System.out.println(str[1] + " " + tempStr);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
