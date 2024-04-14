package com.bkgroup.worm;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class Switcher {
    /**
     * Loads an FXML file of the given path starting at the resources folder. FXML loader starts its relative path from where *this* class is, in relation to the resources folder.
     * So if we had this class in Dir1->Dir2->ThisClass, it would have to be mirrored in the file structure of the resources folder as Dir1->Dir2->ThisClass.
     * <br><br>This method will also clear the contents of the specified container.
     *
     * @param p The container to put what loads from the FXML.
     * @param fxmlPath  Path to the FXML file.
     */
    public static void switchContent (Pane p, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Switcher.class.getResource(fxmlPath));
            p.getChildren().clear();
            p.getChildren().add(loader.load());
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
