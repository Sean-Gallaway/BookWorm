package com.bkgroup.worm.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import static com.bkgroup.worm.utils.DatabaseConnection.db;

public class Query {
    // do not make an instance of this class.
    private Query () {};

    /**
     * method that creates and executes a SELECT query to the database.
     * An example on how to use:<br><br>
     * Query.resultSetToArrayList(Query.select("book", "*", Query.where("title", "Harry Spotter and the Dead-lifter of Azkaban")));
     * @param table the table to use
     * @param attributes the attributes we want. "*" for all attributes.
     * @param conditions conditions for an optional WHERE clause.
     * @return A result set containing tuples.
     */
    public static ResultSet select (String table, String attributes, String... conditions) {
        try {
            // build the query
            StringBuilder query = new StringBuilder("SELECT " + attributes + " FROM " + table);
            if (conditions.length != 0){
                query.append(" WHERE ");
            }
            query.append(String.join(", ", conditions)).append(";");
            Statement ps = db().createStatement();

            // execute and return
            return ps.executeQuery(query.toString());
        }
        catch (Exception e) {
            System.out.println("Could not build query, Reason: " + e.getMessage());
        }
        return null;
    }

    /**
     * Iterates through a result set and packages it as an ArrayList of Strings. Please be aware that the ResultSet cursor will be OOB, thus
     * when trying to iterate through it a second time, unless the cursor was reset, will yield no result.
     * @param rs A given result set
     * @return the packaged contents of the ResultSet.
     */
    public static ArrayList<String[]> resultSetToArrayList (ResultSet rs) {
        if (rs == null) {
            System.out.println("Error: Result set is null.");
            return new ArrayList<>();
        }
        try {
            // initialize the data structure to return, and grab the meta data of the result set to see how many Strings in each array.
            ArrayList<String[]> packagedResults = new ArrayList<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int d = rsmd.getColumnCount();

            // iterate through the result set, adding the tuple to the ArrayList.
            while (rs.next()) {
                String[] result = new String[d];
                for (int a = 1; a < d+1; a++) {
                    result[a-1] = rs.getString(a);
                }
                packagedResults.add(result);
            }

            // return results.
            return packagedResults;
        }
        catch (Exception e) {
            System.out.println("Failed to parse ResultSet, Reason: " + e.getMessage());
        }
        return null;
    }

    /**
     * Helper function to properly format conditions for the WHERE clause.
     * @param condition The condition to format. ex: SELECT * FROM books WHERE [condition] = '?';
     * @param conditionValue the value of the condition. ex: SELECT * FROM books WHERE title = '[value]';
     * @return a formatted string for use with WHERE clause.
     */
    public static String where (String condition, String conditionValue) {
        return condition + " = '" + conditionValue + "'";
    }
}
