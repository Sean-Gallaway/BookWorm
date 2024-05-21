package com.bkgroup.worm.controllers;

import com.bkgroup.worm.User;
import com.bkgroup.worm.utils.AccountHelper;
import com.bkgroup.worm.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountLoginController {

    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_create_account;

    TextField[] textFields;

    /**
     * Assign variables and CSS sheet used for changing text field background color.
     */
    public void initialize()
    {
        textFields = new TextField[] { txt_username, txt_password };
        for (TextField t : textFields)
        {
            t.getStylesheets().add("/CSS/RedTextField.css");
        }
    }

    /**
     * Activates when "create account" button is pressed
     */
    @FXML
    void GotoAccountCreationPage() {
        // TODO MAKE IT GO TO ACCOUNT CREATION PAGE
    }

    /**
     * Activates when "login" button is pressed
     */
    @FXML
    void Login() {
        // Verify user inputs and warn user if anything is wrong
        if (!VerifyInputs()) {
            Tools.ShowPopup(1,"Invalid Input","At Least One Invalid Input Had Been Detected. " +
                    "Please Verify Information And Try Again.");
            return;
        }

        // Alert user if account with specified username does not exist
        if (!AccountHelper.CheckAccountExistence(txt_username.getText())) {
            Tools.ShowPopup(1,"No Account Found","No Account With That Username Was Found. " +
                    "Please Create an Account or Verify The Given Username.");
            return;
        }

        // Show error if passwords don't match
        if (!AccountHelper.AttemptLogin(txt_username.getText(),txt_password.getText())) {
            Tools.ShowPopup(1,"Login Error","Incorrect Password. Please Try Again.");
            return;
        }

        User.Login(txt_username.getText(),AccountHelper.getPictureIndex(txt_username.getText()));
    }

    /**
     * Verifies user inputs and returns boolean True if all inputs are valid or false otherwise.
     * @return True if valid inputs; false otherwise
     */
    private boolean VerifyInputs()
    {
        // Reset background color
        AccountHelper.ResetBackground(textFields);

        // Set text field background to red and set validity to false if invalid input is found
        boolean valid = true;
        if (txt_username.getText().isBlank())
        {
            valid = false;
            txt_username.getStyleClass().add("error");
        }
        if (txt_password.getText().isBlank())
        {
            valid = false;
            txt_password.getStyleClass().add("error");
        }

        return valid;
    }
}
