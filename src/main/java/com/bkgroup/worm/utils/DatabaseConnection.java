package com.bkgroup.worm.utils;

import com.bkgroup.worm.App;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// a class using a singleton pattern to create a connection to the database.
public class DatabaseConnection {
    private static DatabaseConnection dbase;
    private static Connection con;

    /**
     * Do NOT push the changes to the URL, username or password. these should be different for each member of the group!
     */
    String url = ""; //jdbc:mysql://localhost:3306/bookwormdb so we can copy and paste it back in easier
    String username = "";
    String password = "";

    /**
     * creates an instance of the connection to the database.
     */
    private DatabaseConnection () {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected successfully!");
            App.stage.setOnCloseRequest(event -> {
                try {
                    con.close();
                    System.out.println("Disconnected from the database.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

        }
        catch (Exception e) {
            // Alert user database connection error occurred
            Tools.ShowPopup(0,"Server Connection Error","Could Not Connect to Server. Please Wait Then Try Again");

            // Print error
            System.err.println("Failed to connect to the database. Reason: " + e.getCause());
        }

    }

    /**
     * returns the Connection to the database. if one does not exist, it will create one.
     * @return the Connection object to this database.
     */
    public static Connection db () {
        if (con == null) {
            dbase = new DatabaseConnection();
        }
        return dbase.con;
    }
}
