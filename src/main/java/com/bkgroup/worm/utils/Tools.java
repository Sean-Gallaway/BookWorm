package com.bkgroup.worm.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class Tools {
    /**
     * Loads an FXML file of the given path starting at the resources folder. FXML loader starts its relative path from where *this* class is, in relation to the resources folder.
     * So if we had this class in Dir1->Dir2->ThisClass, it would have to be mirrored in the file structure of the resources folder as Dir1->Dir2->ThisClass.
     * <br><br>This method will also clear the contents of the specified container.
     *
     * @param p The container to put what loads from the FXML.
     * @param fxmlPath  Path to the FXML file.
     * @param clear should this method remove the contents of the container?
     */
    public static void switchContent (Pane p, String fxmlPath, boolean clear) {
        try {
            FXMLLoader loader = new FXMLLoader(Tools.class.getResource(fxmlPath));
            loader.getNamespace().put("size", 100);
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
