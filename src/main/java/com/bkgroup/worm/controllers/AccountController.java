package com.bkgroup.worm.controllers;

import com.bkgroup.worm.App;
import com.bkgroup.worm.utils.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

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
    private Button LogOutBtn;

    @FXML
    private Button SI_btn_create_account;

    @FXML
    private Button SI_btn_login;

    @FXML
    private PasswordField SI_txt_password;

    @FXML
    private TextField SI_txt_username;

    @FXML
    private Label emailProfile;

    @FXML
    private Label emailSettings;

    @FXML
    private TextField emailText;

    @FXML
    private TextField fNameText;

    @FXML
    private TextField lNameText;

    @FXML
    private Label nameProfile;

    @FXML
    private Label nameSettings;

    @FXML
    private GridPane pane_account_creation;

    @FXML
    private GridPane pane_account_login;

    @FXML
    private AnchorPane pane_account_page;

    @FXML
    private GridPane pane_account_settings_page;

    @FXML
    private ImageView profileIcon;

    @FXML
    private ImageView profileIcon1;

    @FXML
    private TextField pwText;

    @FXML
    private TextField uNameText;
    @FXML
    private VBox vbox_profile_displays;

    // Sign in page text fields
    TextField[] SI_textFields;
    // Account creation page text fields
    TextField[] AC_textFields;
    // Settings page text fields
    TextField[] S_textFields;

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
            S_textFields = new TextField[] { emailText, pwText,
                    fNameText, lNameText, uNameText };

            // Assign css to text fields
            for (TextField t : SI_textFields)
            {
                t.getStylesheets().add("/CSS/RedTextField.css");
            }
            for (TextField t : AC_textFields)
            {
                t.getStylesheets().add("/CSS/RedTextField.css");
            }
            for (TextField t : S_textFields)
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
        pane_account_settings_page.setVisible(false);
    }

    /**
     * Makes login page visible and all other pages invisible.
     */
    @FXML
    void GotoAccountLoginPage() {
        pane_account_creation.setVisible(false);
        pane_account_login.setVisible(true);
        pane_account_page.setVisible(false);
        pane_account_settings_page.setVisible(false);
    }

    /**
     * Makes profile page visible and all other pages invisible.
     */
    @FXML
    private void GotoProfilePage() {
        pane_account_creation.setVisible(false);
        pane_account_login.setVisible(false);
        pane_account_page.setVisible(true);
        pane_account_settings_page.setVisible(false);

        //call updateProfileDisplay to reset values to current user information
        updateProfileDisplay();
    }

    /**
     * Makes profile page visible and all other pages invisible.
     */
    @FXML
    private void GotoSettingsPage() {
        pane_account_creation.setVisible(false);
        pane_account_login.setVisible(false);
        pane_account_page.setVisible(false);
        pane_account_settings_page.setVisible(true);

        //call updateProfileDisplay to reset values to current user information
        updateProfileDisplay();

        //call updateSettingsDisplay to reset values to current user information
        updateSettingsDisplay();

        //fill user info into text fields
        fillUserFields();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * PROFILE FUNCTIONALITY * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Method used to update the Profile's GUI to display the corresponding information to a specific user.
     */
    private void updateProfileDisplay() {

        //declare String Builder Object to append the first and last name to
        StringBuilder fullName = new StringBuilder();

        //retrieve first and last name of current user from database and append to the newly declared String Builder
        fullName.append(User.getFirstName()).append(" ").append(User.getLastName());

        //set label to a converted String Builder Object to display the user's name
        nameProfile.setText(fullName.toString());

        //set label to a string holding the user's email pulled from the database
        emailProfile.setText(User.getEmail());

        DisplayCreator.createSection("Favorite Books",vbox_profile_displays,0.8);
        DisplayCreator.createBookList(User.getPreferences(true),vbox_profile_displays,0.5);

        DisplayCreator.createSection("Wishlist",vbox_profile_displays,0.8);
        DisplayCreator.createBookList(User.getWishlist(),vbox_profile_displays,0.5);

    }

    /**
     * Method used to update the Setting's GUI to display the corresponding information to a specific user.
     */
    private void updateSettingsDisplay() {

        //declare String Builder Object to append the first and last name to
        StringBuilder fullName = new StringBuilder();

        //retrieve first and last name of current user from database and append to the newly declared String Builder
        fullName.append(User.getFirstName()).append(" ").append(User.getLastName());

        //set label to a converted String Builder Object to display the user's name
        nameSettings.setText(fullName.toString());

        //set label to a string holding the user's email pulled from the database
        emailSettings.setText(User.getEmail());
    }

    /**
     * Method used to fill the fields storing the user's corresponding information to a specific user.
     */
    private void fillUserFields() {

        uNameText.setText(User.getUsername());
        fNameText.setText(User.getFirstName());
        lNameText.setText(User.getLastName());
        emailText.setText(User.getEmail());
        pwText.setText(User.getPassword());
    }

    /**
     * Method used to update user's information that they would've changed in the settings page.
     */
    @FXML
    private void saveChanges() {

        //check if user input is valid by calling helper function that does just that
        if(S_VerifyInputs()) {
            User.setUsername(uNameText.getText()); //set new username
            User.setFirstName(fNameText.getText()); //set new firstname
            User.setLastName(lNameText.getText()); //set new lastname
            User.setEmail(emailText.getText()); //set new email
            User.setPassword(pwText.getText()); //set new password
        }
        else { //else display popup
            Tools.ShowPopup(1, "Warning", "Invalid Input!");
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * ACCOUNT CREATION FUNCTIONALITY  * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Attempts to create account with supplied inputs. Validates inputs and checks if user already exists before
     * attempting to create an account. If account is created successfully, the user is logged in.
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
            User.Login(AC_txt_username.getText(),accountID);
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

    /**
     * Verifies user inputs and returns boolean True if all inputs are valid or false otherwise.
     * @return True if valid inputs; false otherwise
     */
    private boolean S_VerifyInputs()
    {
        // Reset background color
        AccountHelper.ResetBackground(S_textFields);

        // Set text field background to red and set validity to false if invalid input is found
        boolean valid = true;
        if (uNameText.getText().isBlank())
        {
            valid = false;
            uNameText.getStyleClass().add("error");
        }
        if (emailText.getText().isBlank() || !AccountHelper.ValidateEmail(emailText.getText()))
        {
            valid = false;
            emailText.getStyleClass().add("error");
        }
        if (pwText.getText().isBlank())
        {
            valid = false;
            pwText.getStyleClass().add("error");
        }
        if (fNameText.getText().isBlank())
        {
            valid = false;
            fNameText.getStyleClass().add("error");
        }
        if (lNameText.getText().isBlank())
        {
            valid = false;
            lNameText.getStyleClass().add("error");
        }

        return valid;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * LOGIN FUNCTIONALITY * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Attempts to log user in with supplied inputs after validating inputs. If no account exists, user is told to try
     * another username. If all inputs are valid, user is logged in.
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
        User.Login(SI_txt_username.getText(),accountID);
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
