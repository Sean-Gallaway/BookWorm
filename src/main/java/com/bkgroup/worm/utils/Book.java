package com.bkgroup.worm.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    private final int ID;

    /**
     * Create book item to store in cart.
     * @param ID Book ID
     */
    public Book(int ID) {
        this.ID = ID;
    }

    /**
     * Returns int book ID.
     * @return Book ID
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Returns String book title.
     * @return Title
     */
    public String getTitle() {
        ResultSet title = Query.select("book","title",String.format("bookID=%d",this.ID));

        try {
            title.next();
            return title.getString("title");
        }
        catch (NullPointerException | SQLException e) {
            return "err";
        }
    }

    /**
     * Returns String author name
     * @return Author
     */
    public String getAuthor() {
        ResultSet author = Query.select("book","author",String.format("bookID=%d",this.ID));

        try {
            author.next();
            return author.getString("author");
        }
        catch (NullPointerException | SQLException e) {
            return "err";
        }
    }
}
