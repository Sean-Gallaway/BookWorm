package com.bkgroup.worm.controllers;

import com.bkgroup.worm.utils.AccountHelper;
import com.bkgroup.worm.utils.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountCreationController {

    @FXML
    private TextField txt_username;
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
    void CreateAccount(ActionEvent event) {
        // TODO:
        // Create Account
        // Login user
        // Send to profile page

        // TODO CREATE HELPER FUNCTION TO HANDLE COMMONALITIES BETWEEN CREATE ACCOUNT AND LOGIN

        if (!VerifyInputs()) { return; }

        // TODO GO TO LOGIN PAGE IF ACCOUNT EXISTS
        if (AccountHelper.CheckAccountExistance(txt_username.getText())) { return; }

        // TODO: FIX
        // Temp data
        String values = String.format("62,'%s','%s','test','test','%s',3",txt_username.getText(),txt_password.getText(),txt_email.getText());
        Query.insert("user","*",values);
    }

    /**
     * Verify inputs and ensure all inputs have data.
     * @return True if inputs are valid; false otherwise
     */
    private boolean VerifyInputs()
    {
        AccountHelper.ResetBackground(textFields);

        // Validate inputs
        byte flags = getErrorFlags();
        return flags == 0;
    }

    /**
     * Determine error and assign text field background to red if an error is found.
     * @return byte with 1 at flagged values
     */
    private byte getErrorFlags()
    {
        byte flags = 0;
        if (txt_username.getText().isBlank())
        {
            flags |= 0b0001;
            txt_username.getStyleClass().add("error");
        }
        if (txt_email.getText().isBlank() || !AccountHelper.ValidateEmail(txt_email.getText()))
        {
            flags |= 0b0010;
            txt_email.getStyleClass().add("error");
        }
        if (txt_password.getText().isBlank())
        {
            flags |= 0b0100;
            txt_password.getStyleClass().add("error");
        }
        if (txt_password_confirm.getText().isBlank() || !txt_password_confirm.getText().equals(txt_password.getText()))
        {
            flags |= 0b1000;
            txt_password_confirm.getStyleClass().add("error");
        }
        return flags;
    }
}
