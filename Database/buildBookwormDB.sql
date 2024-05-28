-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema bookwormDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bookwormDB
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS bookwormDB;
CREATE SCHEMA IF NOT EXISTS `bookwormDB` DEFAULT CHARACTER SET utf8 ;
USE `bookwormDB` ;

-- -----------------------------------------------------
-- Table `bookwormDB`.`Book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`Book` (
  `bookID` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `series` VARCHAR(50) NULL,
  `seriesNum` INT NULL,
  `author` VARCHAR(50) NOT NULL,
  `length` INT NOT NULL,
  `publicationDate` DATE NOT NULL,
  `numAvailable` INT NOT NULL,
  PRIMARY KEY (`bookID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookwormDB`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`User` (
  `userID` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(30) NOT NULL,
  `password` VARCHAR(25) NOT NULL,
  `fName` VARCHAR(30) NOT NULL,
  `lName` VARCHAR(30) NOT NULL,
  `email` VARCHAR(30) NOT NULL,
  `profilePic` INT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE INDEX `username_UNIQUE` (`userID` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookwormDB`.`userPreferences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`userPreferences` (
  `userID` INT NOT NULL,
  `bookID` INT NOT NULL,
  `preference` INT NOT NULL COMMENT '-1 = dislike\n1 = like\nNULL = No data provided',
  PRIMARY KEY (`userID`, `bookID`),
  INDEX `userPreferences_fk_book_idx` (`bookID` ASC) VISIBLE,
  CONSTRAINT `userPreferences_fk_user`
    FOREIGN KEY (`userID`)
    REFERENCES `bookwormDB`.`User` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `userPreferences_fk_book`
    FOREIGN KEY (`bookID`)
    REFERENCES `bookwormDB`.`Book` (`bookID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookwormDB`.`wishlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`wishlist` (
  `bookID` INT NOT NULL,
  `userID` INT NOT NULL,
  PRIMARY KEY (`userID`, `bookID`),
  INDEX `wishlist_fk_book_idx` (`bookID` ASC) VISIBLE,
  CONSTRAINT `wishlist_fk_user`
    FOREIGN KEY (`userID`)
    REFERENCES `bookwormDB`.`User` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `wishlist_fk_book`
    FOREIGN KEY (`bookID`)
    REFERENCES `bookwormDB`.`Book` (`bookID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookwormDB`.`cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`cart` (
  `bookID` INT NOT NULL,
  `userID` INT NOT NULL,
  PRIMARY KEY (`userID`, `bookID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookwormDB`.`checkoutHistory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`checkoutHistory` (
  `bookID` INT NOT NULL,
  `userID` INT NOT NULL,
  PRIMARY KEY (`userID`, `bookID`),
  INDEX `checkoutHistory_fk_book_idx` (`bookID` ASC) VISIBLE,
  CONSTRAINT `checkoutHistory_fk_user`
    FOREIGN KEY (`userID`)
    REFERENCES `bookwormDB`.`User` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `checkoutHistory_fk_book`
    FOREIGN KEY (`bookID`)
    REFERENCES `bookwormDB`.`Book` (`bookID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookwormDB`.`Genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookwormDB`.`Genre` (
  `bookID` INT NOT NULL,
  `genre` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`bookID`, `genre`),
  CONSTRAINT `genre_to_book`
    FOREIGN KEY (`bookID`)
    REFERENCES `bookwormDB`.`Book` (`bookID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `bookwormDB`.`Book`
-- -----------------------------------------------------
START TRANSACTION;
USE `bookwormDB`;
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`, `author`, `length`, `publicationDate`, `numAvailable`) VALUES (1, 'The Nightingale', NULL,NULL, 'Kristin Hannah', 608, '2017-04-25', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (2, 'The Women', NULL,NULL, 'Kristin Hannah', 480, '2024-02-06', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (3, 'The Great Alone', NULL,NULL, 'Kristin Hannah', 448, '2018-02-06', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (4, 'Dune', 'Dune',1, 'Frank Herbert', 892, '1905-05-18', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (5, 'Dune Messiah', 'Dune',2, 'Frank Herbert', 350, '1905-05-29', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (6, 'Children of Dune', 'Dune',3, 'Frank Herbert', 623, '1905-05-29', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (7, 'God Emperor of Dune', 'Dune',4, 'Frank Herbert', 604, '1905-06-03', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (8, 'Heretics of Dune', 'Dune',5, 'Frank Herbert', 300, '1905-06-06', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (9, 'The Magician\'s Nephew', 'The Chronicles of Narnia',1, 'C.S. Lewis', 221, '1905-05-08', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (10, 'The Horse and His Boy', 'The Chronicles of Narnia',2, 'C.S. Lewis', 256, '1905-05-07', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (11, 'The Lion the Witch and the Wardrobe', 'The Chronicles of Narnia',3, 'C.S. Lewis', 224, '1905-05-03', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (12, 'Prince Caspian', 'The Chronicles of Narnia',4, 'C.S. Lewis', 256, '1905-05-04', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (13, 'The Silver Chair', 'The Chronicles of Narnia',5, 'C.S. Lewis', 272, '1905-05-06', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (14, 'The Last Battle', 'The Chronicles of Narnia',6, 'C.S. Lewis', 240, '1905-05-09', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (15, 'The Hobbit', NULL,NULL, 'J.R.R Tolkien', 300, '1937-09-21', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (16, 'The Silmarillion', NULL,NULL, 'J.R.R Tolkien', 384, '1977-09-15', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (17, 'The Fellowship of the Ring', 'Lord Of the Ring',1, 'J.R.R Tolkien', 432, '1954-07-29', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (18, 'The Two Towers', 'Lord Of the Ring',2, 'J.R.R Tolkien', 352, '1954-11-11', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (19, 'The Return of the King', 'Lord Of the Ring',3, 'J.R.R Tolkien', 432, '1955-10-20', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (20, 'The Hunger Games', 'Hunger Games',1, 'Suzanne Collins', 384, '2008-09-14', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (21, 'Catching Fire', 'Hunger Games',2, 'Suzanne Collins', 115, '2009-09-01', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (22, 'Mockingjay', 'Hunger Games',3, 'Suzanne Collins', 329, '2010-08-24', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (23, 'To Kill a Mockingbird', NULL,NULL, 'Harper Lee', 336, '1960-07-11', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (24, '1984', NULL,NULL, 'George Orwell', 328, '1949-06-08', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (25, 'The Great Gatsby', NULL,NULL, 'F. Scott Fitzgerald', 180, '1925-04-10', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (26, 'Pride and Prejudice', NULL,NULL, 'Jane Austen', 279, '1813-01-28', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (27, 'The Catcher in the Rye', NULL,NULL, 'J.D. Salinger', 224, '1951-07-16', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (28, 'The Da Vinci Code', NULL,NULL, 'Dan Brown', 489, '2003-03-18', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (29, 'The Alchemist', NULL,NULL, 'Paulo Coelho', 197, '1905-06-10', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (30, 'The Book Thief', NULL,NULL, 'Markus Zusak', 552, '2006-03-14', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (31, 'The Fault in Our Stars', NULL,NULL, 'John Green', 313, '2012-01-10', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (32, 'The Kite Runner', NULL,NULL, 'Khaled Hosseini', 371, '2003-05-29', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (33, 'The Girl with the Dragon Tattoo', NULL,NULL, 'Stieg Larsson', 672, '2005-08-01', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (34, 'The Help', NULL,NULL, 'Kathryn Stockett', 464, '2009-02-10', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (35, 'The Shadow of the Wind', NULL,NULL, 'Carlos Ruiz Zafon', 487, '1905-06-23', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (36, 'The Night Circus', NULL,NULL, 'Erin Morgenstern', 400, '2011-09-13', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (37, 'The Goldfinch', NULL,NULL, 'Donna Tartt', 864, '2013-10-22', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (38, 'The Picture of Dorian Gray', NULL,NULL, 'Oscar Wilde', 254, '1890-07-01', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (39, 'The Bell Jar', NULL,NULL, 'Sylvia Plath', 244, '1963-01-14', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (40, 'The Martian', NULL,NULL, 'Andy Weir', 369, '2014-02-11', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (41, 'Gone Girl', NULL,NULL, 'Gillian Flynn', 419, '2012-06-05', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (42, 'Little Women', NULL,NULL, 'Louisa May Alcott', 759, '1868-09-30', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (43, 'The Secret Garden', NULL,NULL, 'Frances Hodgson Burnett', 331, '1905-03-25', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (44, 'The Road', NULL,NULL, 'Cormac McCarthy', 287, '2006-09-26', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (45, 'Harry Potter and the Sorcerer\'s Stone', 'Harry Potter',1, 'J.K. Rowling', 320, '1997-06-26', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (46, 'Harry Potter and the Chamber of Secrets', 'Harry Potter',2, 'J.K. Rowling', 352, '1998-07-02', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (47, 'Harry Potter and the Prisoner of Azkaban', 'Harry Potter',3, 'J.K. Rowling', 448, '1999-07-08', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (48, 'Harry Potter and the Goblet of Fire', 'Harry Potter',4, 'J.K. Rowling', 752, '2000-07-08', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (49, 'Harry Potter and the Order of the Phoenix', 'Harry Potter',5, 'J.K. Rowling', 870, '2003-06-21', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (50, 'Harry Potter and the Half-Blood Prince', 'Harry Potter',6, 'J.K. Rowling', 652, '2005-07-16', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (51, 'Harry Potter and the Deathly Hallows', 'Harry Potter',7, 'J.K. Rowling', 784, '2007-07-21', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (52, 'Flowers For Algernon', NULL, NULL, 'Daniel Keyes', 311, '2004-06-14', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (53, 'Girl In The Blue Coat', NULL, NULL, 'Monica Hesse', 320, '2017-04-04', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (54, 'The Lake', NULL, NULL, 'Natasha Preston', 384, '2021-03-02', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (55, 'Life As We Knew It', 'Last Survivors', 1, 'Susan Beth Pfeffer', 352, '2008-05-01', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (56, 'The Dead And The Gone', 'Last Survivors', 2, 'Susan Beth Pfeffer', 336, '2010-01-18', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (57, 'This World We Live In', 'Last Survivors', 3, 'Susan Beth Pfeffer', 256, '2011-04-18', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (58, 'The Shade Of The Moon', 'Last Survivors', 4, 'Susan Beth Pfeffer', 304, '2014-09-16', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (59, 'Dog Man Unleashed', 'Dog Man', 2, 'Dav Pilkey', 224, '2016-12-27', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (60, 'Murder Your Employer', NULL, NULL, 'Rupert Holmes', 399, '2023-02-21', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (61, 'American Psycho', NULL, NULL, 'Bret Easton Ellis', 418, '2010-05-28', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (62, 'A Clockwork Orange', NULL, NULL, 'Anthony Burgess', 162, '2011-08-09', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (63, 'The Monster At The End Of This Book', NULL, NULL, 'Jon Stone', 24, '2012-02-13', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (64, 'The Shining', 'The Shining', 1, 'Stephen King', 673, '2008-06-24', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (65, 'Doctor Sleep', 'The Shining', 2, 'Stephen King', 655, '2013-09-24', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (67, 'Red Dragon', 'Hannibal Lecter', 1, 'Thomas Harris', 436, '2008-12-24', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (66, 'The Silence Of The Lambs', 'Hannibal Lecter', 2, 'Thomas Harris', 388, '2009-12-28', 5);
INSERT INTO `bookwormDB`.`Book` (`bookID`, `title`, `series`, `seriesNum`,`author`, `length`, `publicationDate`, `numAvailable`) VALUES (68, 'Hannibal', 'Hannibal Lecter', 3, 'Thomas Harris', 530, '2009-06-17', 5);

COMMIT;


-- -----------------------------------------------------
-- Data for table `bookwormDB`.`Genre`
-- -----------------------------------------------------
START TRANSACTION;
USE `bookwormDB`;
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (1, 'historical fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (1, 'romance');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (2, 'historical fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (3, 'military');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (3, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (3, 'romance');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (4, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (5, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (6, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (7, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (8, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (9, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (9, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (10, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (10, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (11, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (11, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (12, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (12, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (13, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (13, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (14, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (14, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (15, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (15, 'adventure');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (16, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (16, 'adventure');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (17, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (17, 'adventure');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (18, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (18, 'adventure');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (19, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (19, 'adventure');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (20, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (20, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (21, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (21, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (22, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (22, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (23, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (23, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (24, 'dystopian');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (25, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (25, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (26, 'romance');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (26, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (27, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (27, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (28, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (29, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (30, 'historical fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (31, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (32, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (33, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (33, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (34, 'historical fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (35, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (36, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (37, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (38, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (39, 'autobiographical ');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (40, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (41, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (42, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (43, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (44, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (45, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (46, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (47, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (48, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (49, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (50, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (51, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (52, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (52, 'romance');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (52, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (52, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (53, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (53, 'historical fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (53, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (54, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (54, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (54, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (54, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (55, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (55, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (55, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (56, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (56, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (56, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (57, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (57, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (57, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (58, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (58, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (58, 'young adult');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (59, 'children');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (59, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (59, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (60, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (60, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (60, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (61, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (61, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (61, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (62, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (62, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (62, 'science fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (62, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (63, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (64, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (64, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (64, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (64, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (65, 'fantasy');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (65, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (65, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (66, 'classic');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (66, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (66, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (67, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (67, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (67, 'thriller');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (68, 'fiction');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (68, 'mystery');
INSERT INTO `bookwormDB`.`Genre` (`bookID`, `genre`) VALUES (68, 'thriller');

COMMIT;

