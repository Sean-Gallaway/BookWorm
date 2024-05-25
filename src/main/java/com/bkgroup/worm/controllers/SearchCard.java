package com.bkgroup.worm.controllers;

import com.bkgroup.worm.User;
import com.bkgroup.worm.utils.Tools;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class SearchCard {
    public static Image cartImage = new Image(Tools.class.getResourceAsStream("/images/addToCart.png"));
    public static Image removeImage = new Image(Tools.class.getResourceAsStream("/images/removeFromCart.png"));
    public Rectangle button;
    public ImageView image;
    public Label label;
    boolean putInCart = false;

    void setup (Image img, String string) {
        image.setImage(img);
        label.setText(string);
        button.setFill(new ImagePattern(cartImage));
        button.setOnMouseClicked(event-> {
            if (!User.isLoggedIn()) {
                User.LoginPrompt();
            }
            else if (putInCart) {
                System.out.println("remove from cart");
                /*
                Cole put you're method of removing from cart here
                */
                button.setFill(new ImagePattern(cartImage));
                putInCart = false;
            }
            else {
                System.out.println("add to cart");
                /*
                Cole put you're method of adding to cart here.
                */
                button.setFill(new ImagePattern(removeImage));
                putInCart = true;
            }
        });
    }
}
