package com.bkgroup.worm.utils;

import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AccountHelper {

    private AccountHelper() {}

    /**
     * Checks if account exists and returns ID for further account queries.
     * @param username Profile username
     * @return user ID
     */
    public static int CheckAccountExistence(String username)
    {
        // Gets account ID by username
        ResultSet user = Query.select("user","userID",String.format("username='%s'",username));

        // Return false if user result set is null or has no data; else, return account ID
        try {
            user.next();
            return user.getInt("userID");
        }
        catch (NullPointerException | SQLException e) {
            return -1;
        }
    }

    /**
     * Attempts to log in by checking if the password provided matches the password in the database for the username.
     * @param ID User ID
     * @param password Password
     * @return True if passwords match; false otherwise
     */
    public static boolean AttemptLogin(int ID, String password) {
        // Get account password by ID
        ResultSet user = Query.select("user","password",String.format("userID='%s'",ID));

        // Return false if password is incorrect; else, return true
        try {
            user.next();
            return user.getString("password").equals(password);
        }
        catch (NullPointerException | SQLException e) {
            return false;
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
        //create random object to use when generating random number for profile pic
        Random rand = new Random();

        //generate random number between 0 and 7
        int randNum = rand.nextInt(8);

        // Try to create the new account up to 5 times
        boolean account_created = false;
        for (int i = 0; i < 5; ++i)
        {
            account_created = Query.insert("user","username,password,fName,lName,email,profilePic",
                    String.format("'%s','%s','%s','%s','%s',%d",user,pass,fName,lName,email,randNum));

            if (account_created) { return account_created; }
        }

        // Return false if account could not be created
        return false;
    }

    /**
     * Resets background color of text fields to white.
     * @param textFields Text fields to reset
     */
    public static void ResetBackground(TextField[] textFields)
    {
        for (TextField t : textFields)
        {
            t.getStyleClass().removeAll(Collections.singleton("error"));
        }
    }

    /**
     * Clears all text in a given array of text fields.
     * @param textFields Text fields
     */
    public static void ClearText(TextField[] textFields)
    {
        for (TextField t : textFields)
        {
            t.clear();
        }
    }
}
