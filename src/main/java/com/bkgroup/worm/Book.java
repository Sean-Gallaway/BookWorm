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
     * Returns book data in string array in the following format:
     * <ul><li>[0]: Title</li><li>[1]: Author</li></ul>
     * @return String array
     */
    public String[] getData() {
        return new String[] { this.title, this.author };
    }
}
