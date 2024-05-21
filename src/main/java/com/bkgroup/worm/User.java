package com.bkgroup.worm;

import com.bkgroup.worm.utils.Query;
import com.bkgroup.worm.utils.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private static boolean loggedIn = false;
    private static String username;
    private static int pfpIndex;
    private static int userID;
    private static final ArrayList<Book> cart = new ArrayList<>();

    private User() {}

    /**
     * Assign data to user when logged in.
     * @param name Username
     * @param ID User ID
     * @param pictureIndex Profile picture index
     */
    public static void Login(String name, int ID, int pictureIndex) {
        username = name;
        pfpIndex = pictureIndex;
        userID = ID;
        loggedIn = true;
        cart.clear();
        PopulateCart();
    }

    /**
     * Populates the cart using the database upon user login.
     */
    public static void PopulateCart() {
        ResultSet books = Query.select("cart","bookID",String.format("userID=%s",userID));

        try {
            while (books.next()) {
                cart.add(new Book(books.getInt("bookID")));
            }
        }
        catch (NullPointerException | SQLException e) {
            System.err.println("Error copying database cart to local");
        }
    }

    /**
     * Adds book to users cart for use in checkout or tells user book already exists in cart if it is found.
     * @param book Book
     */
    public static void AddToCart(Book book) {
        if (ExistsInCart(book)) {
            Tools.ShowPopup(1,"Book Already In Cart","The Selected Book Already Exists In Your Cart");
        }
        else {
            Query.insert("cart","*",String.format("%d,%d", book.getID(), userID));
        }
    }

    // TODO COMMENT
    public static void RemoveFromCart(Book book) {
        if (ExistsInCart(book)) {
            Query.delete("cart",String.format("bookID=%d",book.getID()), String.format("userID=%d",userID));
        }
    }

    /**
     * Checks if book already exists in user's cart.
     * @param book Book
     * @return True if book is in cart; false otherwise
     */
    private static boolean ExistsInCart(Book book) {
        ResultSet result = Query.select("cart","bookID",String.format("userID=%s",userID));

        try {
            while (result.next()) {
                if (result.getInt("bookID") == book.getID()) {
                    return true;
                }
            }
        }
        catch (NullPointerException | SQLException e) {
            return false;
        }

        // Book was not found in cart
        return false;
    }

    /**
     * Returns cart which is an arraylist of book items.
     * @return Arraylist cart
     */
    public static ArrayList<Book> getCart() {
        return cart;
    }

    /**
     * Returns login status.
     * @return True if logged in; false otherwise
     */
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Log use out and reset all profile data to prevent errors.
     */
    public static void Logout() {
        username = "";
        pfpIndex = 0;
        loggedIn = false;
        userID = -1;
        cart.clear();
    }

    /**
     * Returns profile username.
     * @return Username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Returns profile picture index.
     * @return Profile picture index
     */
    public static int getPfpIndex() {
        return pfpIndex;
    }

    // TODO COMMENT INDICES AND ENSURE INDEX WITHIN BOUNDS
    /**
     * Sets profile picture index.
     * @param index index
     */
    public static boolean setPfpIndex(int index) {
        pfpIndex = index;
        return true; // TODO ERROR CHECKING INDEX BOUNDS
    }
}
