package com.bkgroup.worm.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private static boolean loggedIn = false;
    private static String username;
    private static int pfpIndex;
    private static int userID;
    private static final ArrayList<Book> cart = new ArrayList<>();
    private static final ArrayList<Book> wishlist = new ArrayList<>();

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
        PopulateWishlist();
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
        wishlist.clear();
    }

    /**
     * Populates the local cart using the database upon user login.
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
     * Populates the local wishlist using the database upon user login
     */
    public static void PopulateWishlist() {
        ResultSet books = Query.select("wishlist","bookID",String.format("userID=%s",userID));

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
        if (book.getID() == -1) {
            Tools.ShowPopup(0, "Error", "Error adding book to cart.");
        }
        else if (ExistsInCart(book)) {
            Tools.ShowPopup(1, "Already In Cart", "The Selected Book Already Exists In Your Cart");
        }
        else {
            cart.add(book); // Add to local cart
            Query.insert("cart","*",String.format("%d,%d",book.getID(),userID));
        }
    }

    /**
     * Adds book to users wishlist for use in checkout or tells user book already exists in checkout if it is found.
     * @param book Book
     */
    public static void AddToWishlist(Book book) {
        if (ExistsInWishlist(book)) {
            Tools.ShowPopup(1, "Already In Wishlist", "The Selected Book Already Exists In Your Wishlist");
        } else {
            cart.add(book); // Add to local cart
            Query.insert("wishlist","*",String.format("%d,%d", book.getID(),userID));
        }
    }

    /**
     * Removes book from local cart and database.
     * @param book Book to remove
     */
    public static void RemoveFromCart(Book book) {
        if (ExistsInCart(book)) {
            cart.remove(book); // Remove from local cart
            Query.delete("cart",String.format("bookID=%d",book.getID()), String.format("userID=%d",userID));
        }
    }

    /**
     * Checks if book already exists in user's local cart which is synced with database.
     * @param book Book
     * @return True if book is in cart; false otherwise
     */
    public static boolean ExistsInCart(Book book) {
        // Search all books in cart for a matching ID
        for (Book b : cart) {
            if (b.getID() == book.getID()) {
                return true;
            }
        }

        // Book was not found in cart
        return false;
    }

    /**
     * Checks if book already exists in user's local wishlist which is synced with database.
     * @param book Book
     * @return True if book is in wishlist; false otherwise
     */
    public static boolean ExistsInWishlist(Book book) {
        // Search all books in wishlist for a matching ID
        for (Book b : wishlist) {
            if (b.getID() == book.getID()) {
                return true;
            }
        }

        // Book was not found in wishlist
        return false;
    }

    /**
     * Creates popup that informs user they must log in to complete action.
     */
    public static void LoginPrompt() {
        Tools.ShowPopup(1,"Login","Please log in to complete this action");
    }

    /**
     * Returns cart which is an arraylist of book items.
     * @return Arraylist cart
     */
    public static ArrayList<Book> getCart() {
        return cart;
    }

    /**
     * Returns wishlist which is an arraylist of book items.
     * @return Arraylist wishlist
     */
    public static ArrayList<Book> getWishlist() {
        return wishlist;
    }

    /**
     * Returns login status.
     * @return True if logged in; false otherwise
     */
    public static boolean isLoggedIn() {
        return loggedIn;
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

    /**
     * Clears user's local and database cart.
     */
    public static void clearCart() {
        cart.clear();
        Query.delete("cart",String.format("userID=%d",userID));
    }
}
