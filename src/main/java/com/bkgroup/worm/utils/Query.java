package com.bkgroup.worm.utils;

import java.sql.*;
import java.util.ArrayList;

import static com.bkgroup.worm.utils.DatabaseConnection.db;

public class Query {
    // do not make an instance of this class.
    private Query () {}

    /**
     * method that creates and executes a SELECT query to the database.
     * An example on how to use:<br><br>
     * Query.resultSetToArrayList(Query.select("book", "*", Query.where("title", "Harry Spotter and the Dead-lifter of Azkaban")));
     * @param table the table to use
     * @param attributes the attributes we want. "*" for all attributes. Query.project() can be used to format properly for this.
     * @param conditions conditions for an optional WHERE clause.
     * @return A result set containing tuples.
     */
    public static ResultSet select (String table, String attributes, String... conditions) {
        try {
            String base = String.format("SELECT %s FROM %s", attributes, table);

            // If conditions exist, add WHERE statement in query
            if (conditions.length != 0) {
                String conditionFormatted = String.format(" WHERE %s", String.join(", ",conditions));
                return db().createStatement().executeQuery(base + conditionFormatted);
            }

            // Get query with no conditions
            return db().createStatement().executeQuery(base);
        }
        catch (SQLException e) {
            System.err.println("SQL ERROR IN \"select()\":\"Query.java\"");
            return null;
        }
    }

    /**
     * Grabs all books from specified genre and returns ResultSet.
     * @param genre genre to search for
     * @return ResultSet of all books in specified genre
     */
    public static ResultSet populateGenre (String genre) {
        try {
            String query = String.format("SELECT b.title FROM Book b JOIN Genre g ON b.bookID = g.bookID WHERE g.genre = '%s'", genre);
            return db().createStatement().executeQuery(query);
        }
        catch (SQLException e) {
            System.err.println("SQL ERROR IN \"populateGenre()\":\"Query.java\"");
            return null;
        }
    }

    /**
     * Inserts a tuple into the given table.
     * @param table the table to insert into.
     * @param columns the column names to insert into. * indicates the following values are all used.
     * @param values the values of a tuple to be inserted.
     * @return the status on the success of the insert.
     */
    public static boolean insert (String table, String columns, String values) {
        try {
            String query = "";
            // If columns are defined
            if (!columns.equals("*")) {
                query = String.format("INSERT INTO %s (%s) VALUES (%s)",table,columns,values);
            }
            // If columns are not defined
            else {
                query = String.format("INSERT INTO %s VALUES (%s)",table,values);
            }
            db().createStatement().execute(query);
            return true;
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Failed to insert due to duplicate primary key.");
            return false;
        }
        catch (SQLException e) {
            System.err.println("SQL ERROR IN \"insert()\":\"Query.java\"");
            return false;
        }
    }

    /**
     * Iterates through a result set and packages it as an ArrayList of Strings. Please be aware that the ResultSet cursor will be OOB, thus
     * when trying to iterate through it a second time, unless the cursor was reset, will yield no result.
     * @param rs A given result set
     * @return the packaged contents of the ResultSet.
     */
    public static ArrayList<String[]> resultSetToArrayList (ResultSet rs) {
        if (rs == null) {
            System.err.println("Error: Result set is null.");
            return new ArrayList<>();
        }
        try {
            // initialize the data structure to return, and grab the metadata of the result set to see how many Strings in each array.
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
        catch (SQLException e) {
            System.err.println("Failed to parse ResultSet, Reason: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper function to properly format conditions for the WHERE clause.
     * @param condition The condition to format. ex: SELECT * FROM books WHERE [condition] = '?';
     * @param value the value of the condition. ex: SELECT * FROM books WHERE condition = '[value]';
     * @return a formatted string for use with WHERE clause.
     */
    public static String where (String condition, String value) {
        return String.format("%s='%s'",condition,value);
    }

    /**
     * Helper function to properly format a project.
     * @param column the columns that are wanted from the table.
     * @return a formatted string for use within a statement.
     */
    public static String project (String... column) {
        return String.join(", ", column);
    }

    /**
     * Helper function to properly format values to be given to INSERT.
     * @param value a given array of values.
     * @param <T> denotes that this method takes generic values.
     * @return a formatted string for use with insert.
     */
    @SafeVarargs
    public static <T> String values (T... value) {
        String[] strings = new String[value.length];
        for (int a = 0; a < value.length; a++) {
            if (value[a] instanceof Double || value[a] instanceof Integer) {
                strings[a] = String.valueOf(value[a]);
            }
            else {
                strings[a] = "'"+value[a]+"'";
            }
        }
        return String.join(", ", strings);
    }
}
