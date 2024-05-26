package com.bkgroup.worm.controllers;

import com.bkgroup.worm.App;
import com.bkgroup.worm.utils.Book;
import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.User;
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
    boolean inCart = false;

    void setup (Image img, String title, int bookID) {
        // Set book image and title
        image.setImage(img);
        image.setOnMouseClicked(event -> {
            String[] arr = Query.resultSetToArrayList(Query.select("book", "*", Query.where("title", title))).get(0);
            App.oc.clickBook(arr, img, title.replaceAll(" ","").replaceAll("'", "").replaceAll("-", ""));
        });
        label.setText(title);

        inCart = User.ExistsInCart(new Book(bookID));

        // If book is in cart, cart icon is changed to trash.
        if (inCart) {
            button.setFill(new ImagePattern(removeImage));
        }
        else {
            button.setFill(new ImagePattern(cartImage));
        }

        // On click, do correct action
        button.setOnMouseClicked(event-> {
            if (!User.isLoggedIn()) {
                User.LoginPrompt();
            }
            else if (inCart) {
                button.setFill(new ImagePattern(cartImage));
                inCart = false;
                User.RemoveFromCart(new Book(bookID));
            }
            else {
                button.setFill(new ImagePattern(removeImage));
                inCart = true;
                User.AddToCart(new Book(bookID));
            }
        });
    }
}
