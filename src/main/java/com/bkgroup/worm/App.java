package com.bkgroup.worm;

import com.bkgroup.worm.utils.DatabaseConnection;
import com.bkgroup.worm.utils.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    // size of the application window
    static double screenX = Screen.getPrimary().getBounds().getWidth()*.7;
    static double screenY = Screen.getPrimary().getBounds().getHeight()*.7;

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

        // necessary code to make window.
        Scene scene = new Scene(window, screenX, screenY);
        stage.setTitle("Bookworm");
        stage.setScene(scene);
        stage.show();

        // test of the switcher.
        VBox vbox = new VBox();
        vbox.setSpacing(screenX/30);
        Tools.switchContent(vbox, "/card.fxml", true);
        window.getChildren().add(vbox);

        DatabaseConnection.db();

    }

    public static void main(String[] args) {
        launch();
    }
}