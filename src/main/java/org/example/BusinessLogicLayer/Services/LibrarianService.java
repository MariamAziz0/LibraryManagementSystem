package org.example.BusinessLogicLayer.Services;

import org.example.BusinessLogicLayer.Entities.*;
import org.example.DatabaseLayer.DAOs.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class LibrarianService {
    private final BookDAO bookDAO;
    private final PatronDAO patronDAO;
    private final BorrowingDAO borrowingDAO;
    private final DateTimeFormatter formatter;

    public LibrarianService(JdbcTemplate jdbcTemplate) {
        this.bookDAO = new BookDAO(jdbcTemplate);
        this.patronDAO = new PatronDAO(jdbcTemplate);
        this.borrowingDAO = new BorrowingDAO(jdbcTemplate);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public int createBook(Book book) {
        int id = bookDAO.create(book);

        if (id > 0) {
            System.out.println(this.getClass().getName() + ", A book created");
        }
        else {
            System.out.println(this.getClass().getName() + ", A book can't be created");
        }

        return id;
    }

    public boolean updateBook(Book book) {
        boolean bookUpdated = bookDAO.update(book);

        if (bookUpdated) {
            System.out.println(this.getClass().getName() + ", A book updated");
        }
        else {
            System.out.println(this.getClass().getName() + ", A book of id " + book.getId() + " can't be updated");
        }

        return bookUpdated;
    }

    public boolean deleteBook(int bookId) {
        boolean bookDeleted = bookDAO.delete(bookId);

        if (bookDeleted) {
            System.out.println(this.getClass().getName() + ", A book deleted");
        }
        else {
            System.out.println(this.getClass().getName() + ", A book of id " + bookId + " can't be deleted");
        }

        return bookDeleted;
    }

    public List<Book> findAllBooks() {
        List<Book> books = bookDAO.findAll();

        if (!books.isEmpty()) {
            System.out.println(this.getClass().getName() + ", Books found");
        }
        else {
            System.out.println(this.getClass().getName() + ", Books can't be found");
        }

        return books;
    }

    public Book findBookById(int bookId) {
        Book book = bookDAO.findById(bookId);

        if (book != null) {
            System.out.println(this.getClass().getName() + ", A book found");
        }
        else {
            System.out.println(this.getClass().getName() + ", A book of id " + bookId + " can't be found");
        }

        return book;
    }

    public int createPatron(Patron patron) {
        int id = patronDAO.create(patron);

        if (id > 0) {
            System.out.println(this.getClass().getName() + ", A patron created");
        }
        else {
            System.out.println(this.getClass().getName() + ", A patron can't be created");
        }

        return id;
    }

    public boolean updatePatron(Patron patron) {
        boolean patronUpdated = patronDAO.update(patron);

        if (patronUpdated) {
            System.out.println(this.getClass().getName() + ", A patron updated");
        }
        else {
            System.out.println(this.getClass().getName() + ", A patron of id " + patron.getId() + " can't be updated");
        }

        return patronUpdated;
    }

    public boolean deletePatron(int patronId) {
        boolean patronDeleted = patronDAO.delete(patronId);

        if (patronDeleted) {
            System.out.println(this.getClass().getName() + ", A patron deleted");
        }
        else {
            System.out.println(this.getClass().getName() + ", A patron of id " + patronId + " can't be deleted");
        }

        return patronDeleted;
    }

    public List<Patron> findAllPatrons() {
        List<Patron> patrons = patronDAO.findAll();

        if (!patrons.isEmpty()) {
            System.out.println(this.getClass().getName() + ", Patrons found");
        }
        else {
            System.out.println(this.getClass().getName() + ", Patrons can't be found");
        }

        return patrons;
    }

    public Patron findPatronById(int patronId) {
        Patron patron = patronDAO.findById(patronId);

        if (patron != null) {
            System.out.println(this.getClass().getName() + ", A patron found");
        }
        else {
            System.out.println(this.getClass().getName() + ", A patron of id " + patronId + " can't be found");
        }

        return patron;
    }

    public boolean borrowBook(int bookId, int patronId) {
        Borrowing borrowing = new Borrowing();
        borrowing.setPatronId(patronId);
        borrowing.setBookId(bookId);
        borrowing.setBorrowingDate(LocalDate.now().format(formatter));

        boolean bookBorrowed = borrowingDAO.borrow(borrowing);

        if (bookBorrowed) {
            System.out.println(this.getClass().getName() + ", A patron of id (" + patronId + ") " +
                    "borrowed a book of id (" + bookId + ")");
        }
        else {
            System.out.println(this.getClass().getName() + ", A patron of id (" + patronId + ") " +
                    "can't borrow a book of id (" + bookId + ")");
        }

        return bookBorrowed;
    }

    public boolean returnBook(int bookId, int patronId) {
        Borrowing borrowing = new Borrowing();
        borrowing.setPatronId(patronId);
        borrowing.setBookId(bookId);
        borrowing.setReturnDate(LocalDate.now().format(formatter));

        boolean bookReturned = borrowingDAO.returnBorrowing(borrowing);

        if (bookReturned) {
            System.out.println(this.getClass().getName() + ", A patron of id (" + patronId + ") " +
                    "returned a book of id (" + bookId + ")");
        }
        else {
            System.out.println(this.getClass().getName() + ", A patron of id (" + patronId + ") " +
                    "can't return a book of id (" + bookId + ")");
        }

        return bookReturned;
    }
}
