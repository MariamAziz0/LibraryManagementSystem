package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Book;
import org.example.librarymanagementsystem.LibraryManagementSystemApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = LibraryManagementSystemApplication.class)
public class BookDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BookDAO bookDAO;

    @BeforeAll
    public void setUp() {
        bookDAO = new BookDAO(jdbcTemplate);
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Create valid book")
    public void createValidBook() {
        // Arrange
        Book book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);

        // Act
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Create null book")
    public void createNullBook() {
        // Arrange

        // Act
        assertThrows(IllegalArgumentException.class, () -> bookDAO.create(null));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Create non-valid book")
    public void createNonValidBook() {
        // Arrange
        Book book = new Book();

        // Act
        int bookId = bookDAO.create(book);
        assertEquals(-1, bookId);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Find by ID valid book")
    public void findByIdValidBook() {
        // Arrange
        Book book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);

        // Act
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);
        book.setId(bookId);

        Book foundBook = bookDAO.findById(bookId);
        assertNotNull(foundBook);
        assertEquals(book, foundBook);

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Find by ID non-valid book")
    public void findByIdNonValidBook() {
        // Arrange

        // Act
        Book foundBook = bookDAO.findById(-1);
        assertNull(foundBook);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Find All valid book")
    public void findAllValidBook() {
        // Arrange
        Book book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);

        Book book2 = new Book();
        book2.setTitle("New Title2");
        book2.setAuthor("New Author2");
        book2.setPublisher("New Publisher2");
        book2.setIsbn("111-1-11-111111-1");
        book2.setPublicationYear(2015);

        // Act
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);
        book.setId(bookId);

        int bookId2 = bookDAO.create(book2);
        assertTrue(bookId2 > 0);
        book2.setId(bookId2);

        List<Book> foundBooks = bookDAO.findAll();
        assertNotNull(foundBooks);
        assertFalse(foundBooks.isEmpty());
        assertTrue(foundBooks.contains(book));
        assertTrue(foundBooks.contains(book2));

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId2);
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Delete valid book")
    public void deleteValidBook() {
        // Arrange
        Book book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);

        // Act
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);

        assertTrue(bookDAO.delete(bookId));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Delete non-valid book")
    public void deleteNonValidBook() {
        // Arrange

        // Act
        assertFalse(bookDAO.delete(-1));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Update valid book")
    public void updateValidBook() {
        // Arrange
        Book book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);

        // Act
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);
        book.setId(bookId);

        book.setAuthor("Another Author");
        assertTrue(bookDAO.update(book));

        Book foundBook = bookDAO.findById(bookId);
        assertEquals(book, foundBook);

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Update non-valid book")
    public void updateNonValidBook() {
        // Arrange
        Book book = new Book();
        book.setId(-1);

        // Act
        assertFalse(bookDAO.update(book));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BookDAO - Update null book")
    public void updateNullBook() {
        // Arrange

        // Act
        assertThrows(IllegalArgumentException.class, () -> bookDAO.update(null));

        // Clean
    }
}
