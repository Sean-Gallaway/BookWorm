package com.bkgroup.worm.controllers;

import com.bkgroup.worm.User;
import com.bkgroup.worm.utils.AccountHelper;
import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PREFIXES:
 * SI -> Sign in page
 * AC -> Account creation page
 * PF -> Profile page
 */
public class AccountController {

    @FXML
    private Button AC_btn_createAccount;
    @FXML
    private Button AC_btn_login;
    @FXML
    private TextField AC_txt_email;
    @FXML
    private TextField AC_txt_name_first;
    @FXML
    private TextField AC_txt_name_last;
    @FXML
    private PasswordField AC_txt_password;
    @FXML
    private PasswordField AC_txt_password_confirm;
    @FXML
    private TextField AC_txt_username;
    @FXML
    private Button SI_btn_create_account;
    @FXML
    private Button SI_btn_login;
    @FXML
    private PasswordField SI_txt_password;
    @FXML
    private TextField SI_txt_username;
    @FXML
    private GridPane pane_account_creation;
    @FXML
    private GridPane pane_account_login;
    @FXML
    private GridPane pane_account_page;

    // Sign in page text fields
    TextField[] SI_textFields;
    // Account creation page text fields
    TextField[] AC_textFields;
    boolean initialized = false;

    /**
     * Assign variables and CSS sheet used for changing text field background color.
     */
    public void initialize()
    {
        // Go to account page if logged in or login page if not.
        if (User.isLoggedIn()) {
            GotoProfilePage();
        }
        else {
            GotoAccountLoginPage();
        }

        // Initialize variables once
        if (!initialized) {
            // Assign text fields
            SI_textFields = new TextField[] { SI_txt_username, SI_txt_password };
            AC_textFields = new TextField[] { AC_txt_email, AC_txt_password, AC_txt_password_confirm,
                    AC_txt_name_first, AC_txt_name_last, AC_txt_username };

            // Assign css to text fields
            for (TextField t : SI_textFields)
            {
                t.getStylesheets().add("/CSS/RedTextField.css");
            }
            for (TextField t : AC_textFields)
            {
                t.getStylesheets().add("/CSS/RedTextField.css");
            }
        }
    }

    /**
     * Makes account creation page visible and all other pages invisible.
     */
    @FXML
    void GotoAccountCreationPage() {
        pane_account_creation.setVisible(true);
        pane_account_login.setVisible(false);
        pane_account_page.setVisible(false);
    }

    /**
     * Makes login page visible and all other pages invisible.
     */
    @FXML
    void GotoAccountLoginPage() {
        pane_account_creation.setVisible(false);
        pane_account_login.setVisible(true);
        pane_account_page.setVisible(false);
    }

    /**
     * Makes profile page visible and all other pages invisible.
     */
    private void GotoProfilePage() {
        pane_account_creation.setVisible(false);
        pane_account_login.setVisible(false);
        pane_account_page.setVisible(true);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * ACCOUNT CREATION FUNCTIONALITY  * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Activates when "Create Account" button is pressed
     */
    @FXML
    void CreateAccount() {
        // Verify user inputs and warn user if anything is wrong
        if (!AC_VerifyInputs()) {
            Tools.ShowPopup(1,"Invalid Input","At Least One Invalid Input Had Been Detected. " +
                    "Please Verify Information And Try Again.");
            return;
        }

        // Alert user if account username is already being used
        int accountID = AccountHelper.CheckAccountExistence(AC_txt_username.getText());
        if (accountID != -1) {
            Tools.ShowPopup(1,"Account Exists","An Account With That Username Already Exists. " +
                    "Please Login or Choose A Different Username.");
            return;
        }

        // Add account to server
        boolean accountCreated = AccountHelper.CreateAccountInDatabase(
                AC_txt_username.getText(),AC_txt_password.getText(),AC_txt_name_first.getText(),
                AC_txt_name_last.getText(),AC_txt_email.getText()
        );

        // Verify if user account was or was not created. Log user in if account was created
        if (accountCreated) {
            Tools.ShowPopup(2,"Account Created","Account Created Successfully.");
            User.Login(AC_txt_username.getText(),accountID,AccountHelper.getPictureIndex(accountID));
            AccountHelper.ResetBackground(AC_textFields);
            AccountHelper.ClearText(AC_textFields);
            GotoProfilePage();
        }
        else {
            Tools.ShowPopup(0,"Account Error","Account Could Not Be Created. Please Wait And Try Again.");
        }
    }

    /**
     * Verifies user inputs and returns boolean True if all inputs are valid or false otherwise.
     * @return True if valid inputs; false otherwise
     */
    private boolean AC_VerifyInputs()
    {
        // Reset background color
        AccountHelper.ResetBackground(AC_textFields);

        // Set text field background to red and set validity to false if invalid input is found
        boolean valid = true;
        if (AC_txt_username.getText().isBlank())
        {
            valid = false;
            AC_txt_username.getStyleClass().add("error");
        }
        if (AC_txt_email.getText().isBlank() || !AccountHelper.ValidateEmail(AC_txt_email.getText()))
        {
            valid = false;
            AC_txt_email.getStyleClass().add("error");
        }
        if (AC_txt_password.getText().isBlank())
        {
            valid = false;
            AC_txt_password.getStyleClass().add("error");
        }
        if (AC_txt_password_confirm.getText().isBlank() || !AC_txt_password_confirm.getText().equals(AC_txt_password.getText()))
        {
            valid = false;
            AC_txt_password_confirm.getStyleClass().add("error");
        }
        if (AC_txt_name_first.getText().isBlank())
        {
            valid = false;
            AC_txt_name_first.getStyleClass().add("error");
        }
        if (AC_txt_name_last.getText().isBlank())
        {
            valid = false;
            AC_txt_name_last.getStyleClass().add("error");
        }

        return valid;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * LOGIN FUNCTIONALITY * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Logs user in if inputs are all valid.
     */
    @FXML
    void Login() {
        // Verify user inputs and warn user if anything is wrong
        if (!SI_VerifyInputs()) {
            Tools.ShowPopup(1,"Invalid Input","At Least One Invalid Input Had Been Detected. " +
                    "Please Verify Information And Try Again.");
            return;
        }

        // Alert user if account with specified username does not exist
        int accountID = AccountHelper.CheckAccountExistence(SI_txt_username.getText());
        if (accountID == -1) {
            Tools.ShowPopup(1,"No Account Found","No Account With That Username Was Found. " +
                    "Please Create an Account or Verify The Given Username.");
            return;
        }

        // Show error if passwords don't match
        if (!AccountHelper.AttemptLogin(accountID,SI_txt_password.getText())) {
            Tools.ShowPopup(1,"Login Error","Incorrect Password. Please Try Again.");
            return;
        }

        // Log in, go to profile page, and reset login page
        User.Login(SI_txt_username.getText(),accountID,AccountHelper.getPictureIndex(accountID));
        AccountHelper.ResetBackground(SI_textFields);
        AccountHelper.ClearText(SI_textFields);
        GotoProfilePage();
    }

    /**
     * Verifies user inputs and returns boolean True if all inputs are valid or false otherwise.
     * @return True if valid inputs; false otherwise
     */
    private boolean SI_VerifyInputs()
    {
        // Reset background color
        AccountHelper.ResetBackground(SI_textFields);

        // Set text field background to red and set validity to false if invalid input is found
        boolean valid = true;
        if (SI_txt_username.getText().isBlank())
        {
            valid = false;
            SI_txt_username.getStyleClass().add("error");
        }
        if (SI_txt_password.getText().isBlank())
        {
            valid = false;
            SI_txt_password.getStyleClass().add("error");
        }

        return valid;
    }
}