package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.DatabaseConnection;
import com.bkgroup.worm.utils.Query;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.sql.Statement;
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
    ArrayList<String> selectedGenres = new ArrayList<>();


    @FXML
    void initialize () {
        base.heightProperty().addListener(event -> sp.setMinHeight(base.getHeight()-300));

        fp.setMinSize(output.getPrefWidth(), output.getPrefHeight());
        output.setContent(fp);

        output.widthProperty().addListener(event -> fp.setPrefWidth(output.getWidth()));
        output.heightProperty().addListener(event -> fp.setPrefHeight(output.getHeight()));

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
        StringBuilder queryOne = new StringBuilder("""
                \tSELECT a.bookID,
                \t    b.title,
                \t    COUNT(*) AS num
                \tFROM genre a
                \t    JOIN book b
                \t        ON a.bookID = b.bookID
                \tWHERE\s""");
        for (int a = 0; a < selectedGenres.size(); a++) {
            queryOne.append(" a.genre = '");
            queryOne.append(selectedGenres.get(a));
            queryOne.append("'");
            if (a != selectedGenres.size()-1) {
                queryOne.append(" OR");
            }
        }
        queryOne.append("\n\tGROUP BY a.bookID");
        //

        //
        StringBuilder queryTwo = new StringBuilder("""
                \tSELECT bookID,
                \t    title,
                \t    COUNT(*) as num
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
        queryTwo.append("\n\tGROUP BY bookID\n");
        //


        //
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

        //
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

        String subquery =
                queryOne + "\nUNION ALL\n" +
                queryTwo + "\nUNION ALL\n" +
                queryThree + "\nUNION ALL\n" +
                queryFour;

        StringBuilder finalQuery = new StringBuilder("""
                SELECT SUM(num) as tNum,
                r1.title
                FROM (
                    """);
        finalQuery.append(subquery);
        finalQuery.append("""
                \n) AS r1
                    JOIN book b2
                        ON r1.bookID = b2.bookID
                WHERE length >\s""");
        finalQuery.append((int) pageCount.getValue()).append("\n");
        finalQuery.append("""
                GROUP BY r1.title
                ORDER BY tnum DESC;
                """);
        System.out.println(finalQuery);
        ArrayList<String[]> result;
        try {
            Statement st4 = DatabaseConnection.db().createStatement();
            result = Query.resultSetToArrayList(st4.executeQuery(finalQuery.toString()));
            if (result != null) {
                for (String[] str : result) {
                    System.out.println(str[0] + " " + str[1]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* basic structure of search
    SELECT SUM(num) as tNum,
    r1.title
FROM (
    SELECT g.bookID,
        b.title,
        COUNT(g.bookId) AS num
    FROM genre g
        JOIN book b
            ON g.bookID = b.bookID
    WHERE g.genre = 'romance' OR g.genre = 'military'
    GROUP BY g.bookID
UNION ALL
	SELECT bookID,
		title,
		COUNT(bookID) as num
	FROM book
	WHERE title LIKE '%The%'
	GROUP BY bookID
UNION ALL
	SELECT bookID,
		title,
        COUNT(bookID) as num
	FROM book
    WHERE publicationDate = '2018-02-06'
    GROUP BY bookID
) AS r1
	JOIN book b2
		ON r1.bookID = b2.bookID
WHERE length < 800
GROUP BY r1.title
ORDER BY tNum DESC;
     */
}
