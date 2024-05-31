package com.bkgroup.worm.utils;

public class SearchBuilder {
    boolean constructed = false;
    int pgCount;
    String query = "";

    /**
     * Construction method to return the finished Query String.
     * @return the finalized query.
     */
    public String construct () {
        if (!constructed) {
            query = """
                SELECT SUM(num) as tNum,
                r1.title,
                r1.bookID
                FROM (
                    """
                + query +
                """
                \n) AS r1
                    JOIN book b2
                        ON r1.bookID = b2.bookID
                WHERE length >\s""" +
                pgCount + "\n" +
                """
                GROUP BY r1.title, r1.bookID
                HAVING tNum >= 1
                ORDER BY tNum DESC;
                """;
            constructed = true;
        }
        return query;
    }

    /**
     * Search for books that contain all words of a given Array of Strings.
     * If the query is not empty, then it will add a UNION ALL statement before it adds the rest.
     * @param nameSections A given Array of Strings representing a title.
     * @return this Builder object.
     */
    public SearchBuilder fullName (String[] nameSections) {
        if (nameSections.length == 0 || nameSections[0].isEmpty()) {
            return this;
        }
        StringBuilder full = new StringBuilder("""
                \tSELECT bookID,
                \t    title,
                \t    CEIL(COUNT(*)*3.5) as num
                \tFROM book
                \tWHERE""");
        for (int a = 0; a < nameSections.length; a++) {
            full.append(" title LIKE '%");
            full.append(nameSections[a]);
            full.append("%'");
            if (a != nameSections.length-1) {
                full.append(" AND");
            }
        }
        full.append("\n\tGROUP BY bookID");
        if (!query.isEmpty()) {
            query+="\nUNION ALL\n";
        }
        query += full.toString();
        return this;
    }

    /**
     * Search for books that match with the given partial name.
     * If the query is not empty, then it will add a UNION ALL statement before it adds the rest.
     * @param pName A given partial name.
     * @return this Builder object.
     */
    public SearchBuilder partialName (String pName) {
        if (pName.isEmpty()) {
            return this;
        }
        if (!query.isEmpty()) {
            query+="\nUNION ALL\n";
        }
        query += """
                \tSELECT bookID,
                \t    title,
                \t    COUNT(*) as num
                \tFROM book
                \tWHERE""" + " title LIKE '%" + pName + "%'" +
                "\n\tGROUP BY bookID";
        return this;
    }

    /**
     * Search for books that match one or more genres.
     * If the query is not empty, then it will add a UNION ALL statement before it adds the rest.
     * @param genres an Array of Strings that represent the genres.
     * @return this Builder object.
     */
    public SearchBuilder genre (String[] genres) {
        if (genres.length == 0) {
            return this;
        }
        StringBuilder wantedGenre = new StringBuilder("""
                \tSELECT a.bookID,
                \t    b.title,
                \t    COUNT(*) AS num
                \tFROM genre a
                \t    JOIN book b
                \t        ON a.bookID = b.bookID""");
        wantedGenre.append("\n\tWHERE");
        for (int a = 0; a < genres.length; a++) {
            wantedGenre.append(" a.genre = '");
            wantedGenre.append(genres[a]);
            wantedGenre.append("'");
            if (a != genres.length-1) {
                wantedGenre.append(" OR");
            }
        }
        wantedGenre.append("\n\tGROUP BY a.bookID");
        if (!query.isEmpty()) {
            query+="\nUNION ALL\n";
        }
        query += wantedGenre.toString();
        return this;
    }

    /**
     * Builder method that adds a publication field to the sub-query.
     * If the query is not empty, then it will add a UNION ALL statement before it adds the rest.
     * @param yearMin The minimum publication year to be specified.
     * @param yearMax The maximum publication year to be specified.
     * @return this Builder object.
     */
    public SearchBuilder publication (int yearMin, int yearMax) {
        if (!query.isEmpty()) {
            query+="\nUNION ALL\n";
        }
        query += """
                \tSELECT bookID,
                \t    title,
                \t    COUNT(*) as num
                \tFROM book
                \tWHERE""" + " publicationDate >= '" +
                yearMin + "-01" + "-01'" +
                " AND publicationDate < '" +
                yearMax + "-01" + "-01'" +
                "\n\tGROUP BY bookID";
        return this;
    }

    /**
     * Set page count minimum to query.
     * @param pgCount Minimum page count
     * @return SearchBuilder
     */
    public SearchBuilder pageCount (int pgCount) {
        this.pgCount = pgCount;
        return this;
    }
}
