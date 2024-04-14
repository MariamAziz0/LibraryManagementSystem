package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.*;
import org.example.librarymanagementsystem.LibraryManagementSystemApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = LibraryManagementSystemApplication.class)
public class BorrowingDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BorrowingDAO borrowingDAO;
    private Patron patron;
    private Book book;
    private DateTimeFormatter formatter;

    @BeforeAll
    public void setUp() {
        borrowingDAO = new BorrowingDAO(jdbcTemplate);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Arrange
        PatronDAO patronDAO = new PatronDAO(jdbcTemplate);
        BookDAO bookDAO = new BookDAO(jdbcTemplate);

        patron = new Patron();
        patron.setName("Patron");
        patron.setMobile("11111111111");

        book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);

        // Act
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);
        book.setId(bookId);
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Create non-valid borrowingDAO")
    public void createNonValidBorrowingDAO() {
        // Arrange

        // Act
        assertThrows(IllegalArgumentException.class, () -> new BorrowingDAO(null));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Create valid borrowing")
    public void createValidBorrowing() {
        // Arrange
        Borrowing borrowing = new Borrowing();

        borrowing.setPatronId(patron.getId());
        borrowing.setBookId(book.getId());
        borrowing.setBorrowingDate(LocalDate.now().format(formatter));

        // Act
        boolean borrowBook = borrowingDAO.borrow(borrowing);
        assertTrue(borrowBook);

        // Clean
        jdbcTemplate.update("DELETE FROM BORROWING WHERE patron_id = ? AND book_id = ?",
                patron.getId(), book.getId());
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Create double borrowing")
    public void createDoubleBorrowing() {
        // Arrange
        Borrowing borrowing = new Borrowing();

        borrowing.setPatronId(patron.getId());
        borrowing.setBookId(book.getId());
        borrowing.setBorrowingDate(LocalDate.now().format(formatter));

        // Act
        boolean borrowBook = borrowingDAO.borrow(borrowing);
        assertTrue(borrowBook);

        borrowBook = borrowingDAO.borrow(borrowing);
        assertFalse(borrowBook);

        // Clean
        jdbcTemplate.update("DELETE FROM BORROWING WHERE patron_id = ? AND book_id = ?",
                patron.getId(), book.getId());
    }


    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Create null borrowing")
    public void createNullBorrowing() {
        // Arrange

        // Act
        assertThrows(IllegalArgumentException.class, () -> borrowingDAO.borrow(null));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Create non-valid borrowing")
    public void createNonValidBorrowing() {
        // Arrange
        Borrowing borrowing = new Borrowing();

        // Act
        boolean borrowBook = borrowingDAO.borrow(borrowing);
        assertFalse(borrowBook);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Update valid borrowing")
    public void updateValidBorrowing() {
        // Arrange
        Borrowing borrowing = new Borrowing();

        borrowing.setPatronId(patron.getId());
        borrowing.setBookId(book.getId());
        borrowing.setBorrowingDate(LocalDate.now().format(formatter));

        // Act
        boolean borrowBook = borrowingDAO.borrow(borrowing);
        assertTrue(borrowBook);

        borrowing.setReturnDate(LocalDate.now().format(formatter));

        boolean returnBorrowingBook = borrowingDAO.returnBorrowing(borrowing);
        assertTrue(returnBorrowingBook);

        // Clean
        jdbcTemplate.update("DELETE FROM BORROWING WHERE patron_id = ? AND book_id = ?",
                patron.getId(), book.getId());
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Update null borrowing")
    public void updateNullBorrowing() {
        // Arrange

        // Act
        assertThrows(IllegalArgumentException.class, () -> borrowingDAO.returnBorrowing(null));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Update non-valid borrowing")
    public void updateNonValidBorrowing() {
        // Arrange
        Borrowing borrowing = new Borrowing();
        borrowing.setPatronId(-1);
        borrowing.setBookId(-1);

        // Act
        boolean borrowBook = borrowingDAO.returnBorrowing(borrowing);
        assertFalse(borrowBook);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("BorrowingDAO - Delete borrowed book")
    public void deleteBorrowedBook() {
        // Arrange
        Borrowing borrowing = new Borrowing();

        borrowing.setPatronId(patron.getId());
        borrowing.setBookId(book.getId());
        borrowing.setBorrowingDate(LocalDate.now().format(formatter));

        // Act
        boolean borrowBook = borrowingDAO.borrow(borrowing);
        assertTrue(borrowBook);

        PatronDAO patronDAO = new PatronDAO(jdbcTemplate);
        boolean bookDeleted = patronDAO.delete(patron.getId());
        assertFalse(bookDeleted);

        // Clean
        jdbcTemplate.update("DELETE FROM BORROWING WHERE patron_id = ? AND book_id = ?",
                patron.getId(), book.getId());
    }


    @AfterAll
    public void tearDown() {
        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patron.getId());
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", book.getId());
    }
}
