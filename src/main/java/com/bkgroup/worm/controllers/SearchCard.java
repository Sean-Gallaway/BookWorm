package com.bkgroup.worm.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class SearchCard {
    public Rectangle button;
    public ImageView image;
    public Label label;

    void setup (Image img, String string) {
        image.setImage(img);
        label.setText(string);
    }
}
