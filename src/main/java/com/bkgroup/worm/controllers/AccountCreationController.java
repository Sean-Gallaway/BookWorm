package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.AccountHelper;
import com.bkgroup.worm.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountCreationController {

    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_name_first;
    @FXML
    private TextField txt_name_last;
    @FXML
    private TextField txt_email;
    @FXML
    private PasswordField txt_password;
    @FXML
    private PasswordField txt_password_confirm;
    @FXML
    private Button btn_createAccount;

    TextField[] textFields;

    /**
     * Assign variables and CSS sheet used for changing text field background color.
     */
    public void initialize()
    {
        textFields = new TextField[] { txt_username, txt_email, txt_password, txt_password_confirm };
        for (TextField t : textFields)
        {
            t.getStylesheets().add("/CSS/RedTextField.css");
        }
    }

    /**
     * Activates when "Create Account" button is pressed
     */
    @FXML
    void CreateAccount() {
        // Verify user inputs and warn user if anything is wrong
        if (!VerifyInputs()) {
            Tools.ShowPopup(1,"Invalid Input","At Least One Invalid Input Had Been Detected. " +
                    "Please Verify Information And Try Again.");
        }

        // Alert user if account username is already being used
        if (AccountHelper.CheckAccountExistence(txt_username.getText())) {
            Tools.ShowPopup(1,"Account Exists","An Account With That Username Already Exists. " +
                    "Please Login or Choose A Different Username.");
        }

        // Add account to server
        boolean accountCreated = AccountHelper.CreateAccountInDatabase(
                txt_username.getText(),txt_password.getText(),txt_name_first.getText(),
                txt_name_last.getText(),txt_email.getText()
        );

        // Verify if user account was or was not created
        if (accountCreated) {
            Tools.ShowPopup(2,"Account Created","Account Created Successfully. " +
                    "Please Proceed to Login Page");
        }
        else {
            Tools.ShowPopup(0,"Account Not Created","Account Could Not Be Created. " +
                    "Please Wait And Try Again.");
        }
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
        if (txt_email.getText().isBlank() || !AccountHelper.ValidateEmail(txt_email.getText()))
        {
            valid = false;
            txt_email.getStyleClass().add("error");
        }
        if (txt_password.getText().isBlank())
        {
            valid = false;
            txt_password.getStyleClass().add("error");
        }
        if (txt_password_confirm.getText().isBlank() || !txt_password_confirm.getText().equals(txt_password.getText()))
        {
            valid = false;
            txt_password_confirm.getStyleClass().add("error");
        }
        if (txt_name_first.getText().isBlank())
        {
            valid = false;
            txt_name_first.getStyleClass().add("error");
        }
        if (txt_name_last.getText().isBlank())
        {
            valid = false;
            txt_name_last.getStyleClass().add("error");
        }

        return valid;
    }
}
