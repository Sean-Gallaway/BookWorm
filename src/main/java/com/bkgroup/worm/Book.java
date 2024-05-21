package com.bkgroup.worm;

public class Book {
    private final String title;
    private final String author;

    /**
     * Create book item to store in cart.
     * @param title Book title
     * @param author Book author
     */
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    /**
     * Returns String book title.
     * @return Title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns String author name
     * @return Author
     */
    public String getAuthor() {
        return this.author;
    }
}
