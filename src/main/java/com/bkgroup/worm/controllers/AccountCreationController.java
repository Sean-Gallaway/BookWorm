package com.bkgroup.worm.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountCreationController {

    @FXML
    private Button btnCart;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btn_createAccount;
    @FXML
    private TextField txt_email;
    @FXML
    private PasswordField txt_password;
    @FXML
    private TextField txt_password_confirm;
    @FXML
    private PasswordField txt_username;

    /**
     * Activates when "Create Account" button is pressed
     */
    @FXML
    void CreateAccount() {
        // TODO:
        // Check if inputs are valid and non-empty
        // Check if account exists
        // Check if passwords match
        // Create Account
        // Login user
        // Send to profile page

        // TODO CREATE HELPER FUNCTION TO HANDLE COMMONALITIES BETWEEN CREATE ACCOUNT AND LOGIN
    }
}
