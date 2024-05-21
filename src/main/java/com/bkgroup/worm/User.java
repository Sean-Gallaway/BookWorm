package com.bkgroup.worm;

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
    }

    /**
     * Adds book to users cart for use in checkout.
     * @param book Book
     */
    public static void AddToCart(Book book) {
        cart.add(book);
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
