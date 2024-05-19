package com.bkgroup.worm.utils;

import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AccountHelper {

    private AccountHelper() {}

    /**
     * Checks if account exists and returns boolean.
     * @param userName Profile username
     * @return True if account exists; false otherwise
     */
    public static boolean CheckAccountExistance(String userName)
    {
        String condition = String.format("username='%s'",userName);
        ResultSet user = Query.select("user","*",condition);

        // Return false if user result set is null or has no data
        try {
            if (user == null) { return false; }
            return user.next();
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    /**
     * Resets background color of text fields.
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
}
