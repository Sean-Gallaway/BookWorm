package com.bkgroup.worm.controllers;

import javafx.event.ActionEvent;
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
    private PasswordField txt_password_confirm;

    @FXML
    private TextField txt_username;

    /**
     * Activates when "Create Account" button is pressed
     */
    @FXML
    void CreateAccount(ActionEvent event) {
        // TODO:
        // Check if inputs are valid and non-empty
        // Check if account exists
        // Check if passwords match
        // Create Account
        // Login user
        // Send to profile page

        // TODO CREATE HELPER FUNCTION TO HANDLE COMMONALITIES BETWEEN CREATE ACCOUNT AND LOGIN

        VerifyInputs();
    }

    private boolean VerifyInputs()
    {
        byte flags = getErrorFlags();
        return false;
    }

    private byte getErrorFlags()
    {
        byte flags = 0;
        if (txt_username.getText().isBlank())
        {
            flags &= 0b0001;
        }
        if (txt_email.getText().isBlank())
        {
            flags &= 0b0010;
        }
        if (txt_password.getText().isBlank())
        {
            flags &= 0b0100;
        }
        if (txt_password_confirm.getText().isBlank())
        {
            flags &= 0b1000;
        }
        return flags;
    }
}
