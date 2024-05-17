package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MenuController {
    @FXML AnchorPane centerPane;//The AnchorPane for the center panel of the BorderPane

    //Menu Buttons
    @FXML Button btnHome;
    @FXML Button btnSearch;
    @FXML Button btnCart;
    @FXML Button btnProfile;

    /**
     * Sets the images of all menu buttons to be icons instead of text.
     */
    @FXML
    public void initialize() {

        Image homeImg = new Image("images/home.png");
        ImageView homeView = new ImageView(homeImg);
        homeView.setFitHeight(80);
        homeView.setPreserveRatio(true);
        btnHome.setGraphic(homeView);

        Image searchImg = new Image("images/search.png");
        ImageView searchView = new ImageView(searchImg);
        searchView.setFitHeight(80);
        searchView.setPreserveRatio(true);
        btnSearch.setGraphic(searchView);

        Image cartImg = new Image("images/cart.png");
        ImageView cartView = new ImageView(cartImg);
        cartView.setFitHeight(80);
        cartView.setPreserveRatio(true);
        btnCart.setGraphic(cartView);

        Image profileImg = new Image("images/profile.png");
        ImageView profileView = new ImageView(profileImg);
        profileView.setFitHeight(80);
        profileView.setPreserveRatio(true);
        btnProfile.setGraphic(profileView);


        //Default to loading the home screen on initialization
        Tools.switchContent(centerPane, "/com/bkgroup/worm/controllers/Home.fxml", true);
    }

    /**
     * Load the home window
     * @param event The button event
     */
    @FXML
    public void clickHome(ActionEvent event) {
        Tools.switchContent(centerPane, "/com/bkgroup/worm/controllers/Home.fxml", true);
    }

    /**
     * Load the search window
     * @param event The button event
     */
    @FXML
    public void clickSearch(ActionEvent event) {
        Tools.switchContent(centerPane, "/com/bkgroup/worm/controllers/Search.fxml", true);
    }

    /**
     * Load the cart window
     * @param event The button event
     */
    @FXML
    public void clickCart(ActionEvent event) {
        Tools.switchContent(centerPane, "/com/bkgroup/worm/controllers/Cart.fxml", true);
    }

    /**
     * Load the profile window
     * @param event The button event
     */
    @FXML
    public void clickProfile(ActionEvent event) {
        Tools.switchContent(centerPane, "/com/bkgroup/worm/controllers/Profile.fxml", true);
    }
}
