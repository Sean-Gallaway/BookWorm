package com.bkgroup.worm.utils;

import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AccountHelper {

    private AccountHelper() {}

    /**
     * Checks if account exists and returns boolean.
     * @param username Profile username
     * @return True if account exists; false otherwise
     */
    public static boolean CheckAccountExistence(String username)
    {
        String condition = String.format("username='%s'",username);
        ResultSet user = Query.select("user","*",condition);

        // Return false if user result set is null or has no data
        try {
            return user.next();
        }
        catch (NullPointerException | SQLException e) {
            return false;
        }
    }

    /**
     * Attempts to log in by checking if the password provided matches the password in the database for the username.
     * @param username Username
     * @param password Password
     * @return True if passwords match; false otherwise
     */
    public static boolean AttemptLogin(String username, String password) {
        String condition = String.format("username='%s'",username);
        ResultSet user = Query.select("user","password",condition);

        // Check if passwords match
        try {
            user.next();
            return user.getString("password").equals(password);
        }
        catch (NullPointerException | SQLException e) {
            return false;
        }
    }

    /**
     * Returns profile picture index of account.
     * @param username Username
     * @return Profile picture index
     */
    public static int getPictureIndex(String username) {
        String condition = String.format("username='%s'",username);
        ResultSet user = Query.select("user","profilePic",condition);

        // Check if passwords match
        try {
            user.next();
            return user.getInt("profilePic");
        }
        catch (NullPointerException | SQLException e) {
            return 0;
        }
    }

    /**
     * Resets background color of text fields.
     * @param textFields Text fields to reset
     */
    public static void ResetBackground(TextField[] textFields)
    {
        // Reset background to white for all text fields
        for (TextField t : textFields)
        {
            t.getStyleClass().removeAll(Collections.singleton("error"));
        }
    }

    /**
     * Checks if email is valid and returns boolean representation.
     * @param email Email to check
     * @return True if valid; false otherwise
     */
    public static boolean ValidateEmail(String email)
    {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Attempts to create new account up to 5 times returning true if account was created or false otherwise.
     * @param user Username
     * @param pass Password
     * @param fName First Name
     * @param lName Last Name
     * @param email Email
     * @return True if account was created; false otherwise
     */
    public static boolean CreateAccountInDatabase(String user, String pass, String fName, String lName, String email)
    {
        // Try to create the new account up to 5 times
        boolean account_created = false;
        for (int i = 0; i < 5; ++i)
        {
            account_created = Query.insert("user","username,password,fName,lName,email,profilePic",
                    String.format("'%s','%s','%s','%s','%s',0",user,pass,fName,lName,email));

            if (account_created) { return account_created; }
        }

        // Return false if account could not be created
        return false;
    }
}
