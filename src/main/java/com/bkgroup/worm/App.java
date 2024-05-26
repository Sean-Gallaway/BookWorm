package com.bkgroup.worm;

import com.bkgroup.worm.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    // size of the application window
    public static double screenX = Screen.getPrimary().getBounds().getWidth()*.7;
    public static double screenY = Screen.getPrimary().getBounds().getHeight()*.7;

    // it is useful for some classes to be able to access the Stage globally.
    public static Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        
        //Load the home screen
        try {
            DatabaseConnection.db();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/bkgroup/worm/controllers/menu.fxml"));
            Scene homeScene = new Scene(fxmlLoader.load());
            stage.setTitle("Bookworm");
            stage.setScene(homeScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load home page.\n " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}