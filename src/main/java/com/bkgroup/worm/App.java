package com.bkgroup.worm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    // size of the application window
    static double screenX = Screen.getPrimary().getBounds().getWidth()*.7;
    static double screenY = Screen.getPrimary().getBounds().getHeight()*.7;

    // containers for elements of the UI
    public static StackPane content = new StackPane();
    public static Pane window = new Pane(content);

    @Override
    public void start(Stage stage) {
        window.setPrefSize(screenX, screenY);

        // necessary code to make window.
        Scene scene = new Scene(window, screenX, screenY);
        stage.setTitle("Bookworm");
        stage.setScene(scene);
        stage.show();

        // test of the switcher.
        Switcher.switchContent(content, "/test1.fxml");
        Switcher.switchContent(content, "/test2.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}