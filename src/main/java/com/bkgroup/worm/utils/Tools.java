package com.bkgroup.worm.utils;

import static com.bkgroup.worm.utils.DatabaseConnection.db;
import com.bkgroup.worm.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tools {
    /**
     * Loads an FXML file of the given path starting at the resources folder. FXML loader starts its relative path from
     * where *this* class is, in relation to the resources folder. So if we had this class in Dir1->Dir2->ThisClass,
     * it would have to be mirrored in the file structure of the resources folder as Dir1->Dir2->ThisClass.
     * <br><br>This method will also clear the contents of the specified container.
     *
     * @param p The container to put what loads from the FXML.
     * @param fxmlPath  Path to the FXML file.
     * @param clear should this method remove the contents of the container?
     */
    public static void switchContent (Pane p, String fxmlPath, boolean clear) {
        try {
            FXMLLoader loader = new FXMLLoader(Tools.class.getResource(fxmlPath));
            loader.getNamespace().put("sizeX", App.screenX);
            loader.getNamespace().put("sizeY", App.screenY);

            if (clear) {
                p.getChildren().clear();
            }
            p.getChildren().add(loader.load());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
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

    /**
     * Creates error display with assigned message and title with the following types:
     * <ul><li>0: Error</li><li>1: Warning</li><li>2: Confirmation</li><li>3: Information</li></ul>
     * @param type Popup alert type
     * @param title Title
     * @param message Display Message
     */
    public static void ShowPopup(int type, String title, String message) {
        // Create popup with alert type
        Alert popup;
        if (type == 0) {
            popup = new Alert(Alert.AlertType.ERROR);
        }
        else if (type == 1) {
            popup = new Alert(Alert.AlertType.WARNING);
        }
        else if (type == 2) {
            popup = new Alert(Alert.AlertType.CONFIRMATION);
        }
        else {
            popup = new Alert(Alert.AlertType.INFORMATION);
        }

        // Assign text
        popup.setTitle(title);
        popup.setHeaderText(null);
        popup.setContentText(message);
        popup.showAndWait();
    }

    /**
     * Unbind the width and height properties of given regions so that they can be resized.
     * Regions should only need to be unbound when during its construction in an FXML file a variable is used to define its size.
     * Any number of Regions can be passed to this method. notably, VBox, HBox, and any derivative of Pane derives from Region, so they can be passed to this method.
     * @param p any number of given regions.
     */
    public static void unbind (Region... p) {
        for (Region r : p) {
            r.minWidthProperty().unbind();
            r.prefWidthProperty().unbind();
            r.maxWidthProperty().unbind();
            r.minHeightProperty().unbind();
            r.prefHeightProperty().unbind();
            r.maxHeightProperty().unbind();
        }
    }
}
