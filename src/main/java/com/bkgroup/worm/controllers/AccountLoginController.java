package com.bkgroup.worm.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountLoginController {
    @FXML
    private Button btnCart;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btn_login;
    @FXML
    private PasswordField txt_password;
    @FXML
    private TextField txt_username;

    /**
     * Activates when login button is pressed
     */
    @FXML
    void Login() {
        // TODO:
        // Check if inputs are valid and non-empty
        // Check if account exists
        // Check if passwords match
        // Login user
        // Send to profile page

        // TODO CREATE HELPER FUNCTION TO HANDLE COMMONALITIES BETWEEN CREATE ACCOUNT AND LOGIN
    }

}
