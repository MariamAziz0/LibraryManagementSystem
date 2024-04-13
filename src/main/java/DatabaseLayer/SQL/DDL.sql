CREATE DATABASE LibraryManagement;
GO;

USE LibraryManagement;
GO;

CREATE TABLE BOOK (
    id INT PRIMARY KEY IDENTITY(1, 1),
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    publication_year INT,
    ISBN VARCHAR(20) UNIQUE,
    publisher VARCHAR(255)
);
GO;

CREATE TABLE PATRON (
    id INT PRIMARY KEY IDENTITY(1, 1),
    name VARCHAR(255),
    mobile VARCHAR(50)
);
GO;

CREATE TABLE BORROWING (
    patron_id INT,
    book_id INT,
    borrowing_date DATE NOT NULL,
    return_date DATE NOT NULL,
    PRIMARY KEY (patron_id, book_id),
    FOREIGN KEY (patron_id) REFERENCES PATRON(id),
    FOREIGN KEY (book_id) REFERENCES BOOK(id)
);
GO;