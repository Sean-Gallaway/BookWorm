package com.bkgroup.worm;

import com.bkgroup.worm.controllers.OverlayController;
import com.bkgroup.worm.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    // size of the application window
    public static double screenX = Screen.getPrimary().getBounds().getWidth()*.7;
    public static double screenY = Screen.getPrimary().getBounds().getHeight()*.7;
    public static OverlayController oc;

    // it is useful for some classes to be able to access the Stage globally.
    public static Stage stage;

    /**
     * Start the JavaFX application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        // Initialize application variables
        Group g = new Group();
        stage.getIcons().add(new Image("images/logo.png"));
        this.stage = stage;
        AnchorPane ap = new AnchorPane();

        //Load the home screen
        try {
            FXMLLoader fxmlLoader2 = new FXMLLoader(App.class.getResource("/com/bkgroup/worm/controllers/Overlay.fxml"));
            g.getChildren().add(fxmlLoader2.load());
            oc = fxmlLoader2.getController();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/bkgroup/worm/controllers/menu.fxml"));
            ap.getChildren().addAll(fxmlLoader.load(), g);
            Scene homeScene = new Scene(ap);
            stage.setTitle("Bookworm");
            stage.setScene(homeScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load home page.\n " + e.getMessage());
        }
    }

    /**
     * Entry point of the program. Launches JavaFX.
     * @param args The main method parameters
     */
    public static void main(String[] args) {
        launch();
    }
}