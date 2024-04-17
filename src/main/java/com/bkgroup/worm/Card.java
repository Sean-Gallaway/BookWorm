package com.bkgroup.worm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Objects;

public class Card {
    // size of the card.
    static double size = 300;
    // potential future use.
    public static ArrayList<Card> cards = new ArrayList<>();


    @FXML
    private FlowPane title;
    @FXML
    private FlowPane description;
    @FXML
    private HBox starbox;
    @FXML
    private ImageView book;
    @FXML
    private AnchorPane base;

    /**
     * Separates the given text by spaces and places text objects into the description field. in the future we can add text effects if we wish.
     * @param text the given text
     */
    private void populateDescription (String text) {
        String[] passage = text.split(" ");
        for (String str : passage) {
            Text t = new Text(str + " ");
            description.getChildren().add(t);
        }
    }

    /**
     * Creates a card and places it into a container.
     * @param p the given container
     * @param str description
     */
    public static void getCard (Pane p, String str) {
        try {
            FXMLLoader loader = new FXMLLoader(Switcher.class.getResource("/card.fxml"));
            loader.getNamespace().put("size", size);
            p.getChildren().add(loader.load());
            Card cardController = loader.getController();
            cardController.book.setImage(new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/img.png"))));
            cardController.populateDescription(str);
            cards.add(cardController);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void resize () {

    }
}
