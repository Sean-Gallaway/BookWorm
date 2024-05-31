package com.bkgroup.worm.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static boolean loggedIn = false;
    private static String password;
    private static String email;
    private static String username;
    private static String firstName;
    private static String lastName;
    private static int pfpIndex;
    private static int userID;
    private static final ArrayList<Book> cart = new ArrayList<>();
    private static final ArrayList<Book> wishlist = new ArrayList<>();
    private static final HashMap<Integer,Integer> preferences = new HashMap<>();

    private User() {}

    /**
     * Assign data to user when logged in.
     * @param name Username
     * @param ID User ID
     */
    public static void Login(String name, int ID) {
        username = name;
        userID = ID;
        LoadUserData();
        PopulateCart();
        PopulateWishlist();
        PopulatePreferences();
        loggedIn = true;
    }

    /**
     * Log use out and reset all profile data to prevent errors.
     */
    public static void Logout() {
        username = "";
        firstName = "";
        lastName = "";
        password = "";
        email = "";
        pfpIndex = -1;
        userID = -1;
        cart.clear();
        wishlist.clear();
        preferences.clear();
        loggedIn = false;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * CART FUNCTIONALITY  * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Populates the local cart using the database upon user login.
     */
    private static void PopulateCart() {
        ResultSet books = Query.select("cart","bookID",String.format("userID=%s",userID));

        // Adds all books in cart under user's account into local cart 
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
            cart.add(book);
            Query.insert("cart","*",String.format("%d,%d",book.getID(),userID));
        }
    }

    /**
     * Removes book from local cart and database.
     * @param book Book to remove
     */
    public static void RemoveFromCart(Book book) {
        if (ExistsInCart(book)) {
            cart.removeIf(b -> b.getID() == book.getID());
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
     * Returns cart which is an arraylist of book items.
     * @return Arraylist cart
     */
    public static ArrayList<Book> getCart() {
        return cart;
    }

    /**
     * Clears user's local and database cart.
     */
    public static void clearCart() {
        cart.clear();
        Query.delete("cart",String.format("userID=%d",userID));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * WISHLIST FUNCTIONALITY  * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Populates the local wishlist using the database upon user login.
     */
    private static void PopulateWishlist() {
        ResultSet books = Query.select("wishlist","bookID",String.format("userID=%s",userID));

        // Adds all books in wishlist under user's profile to local wishlist
        try {
            while (books.next()) {
                wishlist.add(new Book(books.getInt("bookID")));
            }
        }
        catch (NullPointerException | SQLException e) {
            System.err.println("Error copying database wishlist to local");
        }
    }

    /**
     * Adds book to users wishlist for use in checkout or tells user book already exists in checkout if it is found.
     * @param book Book
     */
    public static void AddToWishlist(Book book) {
        if (book.getID() == -1) {
            Tools.ShowPopup(0, "Error", "Error adding book to wishlist.");
        }
        else if (ExistsInWishlist(book)) {
            Tools.ShowPopup(1, "Already In Wishlist", "The Selected Book Already Exists In Your Wishlist");
        }
        else {
            wishlist.add(book);
            Query.insert("wishlist","*",String.format("%d,%d", book.getID(),userID));
        }
    }

    /**
     * Removes book from local wishlist and database.
     * @param book Book to remove
     */
    public static void RemoveFromWishlist(Book book) {
        if (ExistsInWishlist(book)) {
            wishlist.removeIf(b -> b.getID() == book.getID());
            Query.delete("wishlist",String.format("bookID=%d",book.getID()), String.format("userID=%d",userID));
        }
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
     * Returns wishlist which is an arraylist of book items.
     * @return Arraylist wishlist
     */
    public static ArrayList<String[]> getWishlist() {
        ArrayList<String[]> books = new ArrayList<>();
        for (Book b : wishlist) {
            books.add(Tools.getBookData(b));
        }
        return books;
    }

    /**
     * Clears user's local and database wishlist.
     */
    public static void clearWishlist() {
        cart.clear();
        Query.delete("wishlist",String.format("userID=%d",userID));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * PREFERENCES FUNCTIONALITY * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Populates the local preference list using the database upon user login.
     */
    private static void PopulatePreferences() {
        ResultSet books = Query.select("userpreferences","*",String.format("userID=%d",userID));

        // Adds all books with preference under user's profile to local map
        try {
            while (books.next()) {
                preferences.put(books.getInt("bookID"),books.getInt("preference"));
            }
        }
        catch (NullPointerException | SQLException e) {
            System.err.println("Error copying database preferences to local");
        }
    }

    /**
     * Adds book to preferences with a rating depending on argument.
     * @param book Book to add
     * @param isLiked True if book was liked; false otherwise
     */
    public static void AddToPreferences(Book book, boolean isLiked) {
        preferences.put(book.getID(), isLiked ? 1 : 0);
        Query.insert("userpreferences","*", String.format("%d,%d,%d",userID,book.getID(),isLiked ? 1 : 0)
        );
    }

    /**
     * Removes book from local and database preferences.
     * @param book Book to remove
     */
    public static void RemoveFromPreferences(Book book) {
        if (preferences.containsKey(book.getID())) {
            preferences.remove(book.getID());
            Query.delete("userpreferences",String.format("bookID=%d",book.getID()), String.format("userID=%d",userID));
        }
    }

    /**
     * Returns preference of book or -1 if it is not in preferences.
     * @param book Book
     * @return -1: DNE, 0: Disliked, 1: Liked
     */
    public static int ExistsInPreferences(Book book) {
        if (!preferences.containsKey(book.getID())) {
            return -1;
        }

        return preferences.get(book.getID());
    }

    /**
     * Gets all rated books and adds books to arraylist to return. Rating grabbed depends on input argument.
     * @param liked True grabs liked books; False grabs disliked books
     * @return Book arraylist
     */
    public static ArrayList<String[]> getPreferences(boolean liked) {
        // Iterates through all books with preference
        ArrayList<String[]> list = new ArrayList<>();
        for (Map.Entry<Integer,Integer> book : preferences.entrySet()) {
            // Returns liked books
            if (liked && book.getValue() == 1) {
                list.add(Tools.getBookData(new Book(book.getKey())));
            }
            // Returns disliked books
            else if (!liked && book.getValue() == 0) {
                list.add(Tools.getBookData(new Book(book.getKey())));
            }
        }
        return list;
    }

    /**
     * Clears user's local and database preferences.
     */
    public static void clearPreferences() {
        preferences.clear();
        Query.delete("userpreferences",String.format("userID=%d",userID));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * HELPER FUNCTIONALITY  * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Creates popup that informs user they must log in to complete action.
     */
    public static void LoginPrompt() {
        Tools.ShowPopup(1,"Login","Please log in to complete this action");
    }

    /**
     * Returns login status.
     * @return True if logged in; false otherwise
     */
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Loads user data including first name, last name, email, and profile picture index.
     */
    private static void LoadUserData() {
        ResultSet user = Query.select("user","*",String.format("userID=%d",userID));

        try {
            user.next();
            firstName = user.getString("fName");
            lastName = user.getString("lName");
            pfpIndex = user.getInt("profilePic");
            email = user.getString("email");
            password = user.getString("password");
        }
        catch (NullPointerException | SQLException e) {
            System.err.println("Error loading user data");
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * GETTERS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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

    /**
     * Returns profile first name.
     * @return First name
     */
    public static String getFirstName() {
        return firstName;
    }

    /**
     * Returns profile last name.
     * @return Last name
     */
    public static String getLastName() {
        return lastName;
    }

    /**
     * Returns profile email.
     * @return email
     */
    public static String getEmail() {
        return email;
    }

    /**
     * Returns profile password.
     * @return password
     */
    public static String getPassword() {
        return password;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * SETTERS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Sets profile picture index.
     * @param index index
     */
    public static void setPfpIndex(int index) {
        pfpIndex = index;
        Query.update("user",String.format("userID=%d",userID),String.format("profilePic=%d",pfpIndex));
    }

    /**
     * Sets profile first name.
     * @param name Name
     */
    public static void setFirstName(String name) {
        firstName = name;
        Query.update("user",String.format("userID=%d",userID),String.format("fName=\"%s\"",name));
    }

    /**
     * Sets profile first name.
     * @param name Name
     */
    public static void setLastName(String name) {
        lastName = name;
        Query.update("user",String.format("userID=%d",userID),String.format("lName=\"%s\"",name));
    }

    /**
     * Sets profile email.
     * @param mail Email
     */
    public static void setEmail(String mail) {
        email = mail;
        Query.update("user",String.format("userID=%d",userID),String.format("email=\"%s\"",mail));
    }

    /**
     * Sets profile username.
     * @param name Username
     */
    public static void setUsername(String name) {
        username = name;
        Query.update("user",String.format("userID=%d",userID),String.format("username=\"%s\"",name));
    }

    /**
     * Sets profile password.
     * @param name password
     */
    public static void setPassword(String name) {
        password = name;
        Query.update("user",String.format("userID=%d",userID),String.format("password=\"%s\"",name));
    }
}
