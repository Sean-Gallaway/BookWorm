package com.bkgroup.worm;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        // resize window listeners.
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
        content.getChildren().add(vbox);
        Card.getCard(vbox, "something");
        Card.getCard(vbox, "second card!");
        //Card.resize(3);
    }

    public static void main(String[] args) {
        launch();
    }
}