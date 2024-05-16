package com.bkgroup.worm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    // size of the application window
    public static double screenX = Screen.getPrimary().getBounds().getWidth()*.7;
    public static double screenY = Screen.getPrimary().getBounds().getHeight()*.7;

    // it is useful for some classes to be able to access the Stage globally.
    public static Stage stage;

    // containers for elements of the UI
    public static Pane window = new Pane();

    @Override
    public void start(Stage stage) {
        // resize window listeners.
        this.stage = stage;
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() > screenX) {
                Card.resize(stage.getWidth()/screenX);
            }
        });
        stage.heightProperty().addListener((observable, oldValue, newValue) -> stage.setWidth(stage.getWidth() * (newValue.doubleValue()/oldValue.doubleValue())));
        window.setPrefSize(screenX, screenY);

        /*// necessary code to make window.
        Scene scene = new Scene(window, screenX, screenY);
        stage.setTitle("Bookworm");
        stage.setScene(scene);
        stage.show();

        // test of the switcher.
        VBox vbox = new VBox();
        vbox.setSpacing(screenX/30);
        Tools.switchContent(vbox, "/card.fxml", true);
        window.getChildren().add(vbox);
        */

        //DatabaseConnection.db();

        try {
            //Load the home screen
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