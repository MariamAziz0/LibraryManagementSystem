package org.example.APILayer;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.BusinessLogicLayer.Entities.*;
import org.example.DatabaseLayer.DAOs.*;
import org.example.librarymanagementsystem.LibraryManagementSystemApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = LibraryManagementSystemApplication.class)
@AutoConfigureMockMvc
public class LibrarianAPITests {
    private final MockMvc mockMvc;
    private final JdbcTemplate jdbcTemplate;
    private final BookDAO bookDAO;
    private final PatronDAO patronDAO;
    private final BorrowingDAO borrowingDAO;
    private Patron patron;
    private Book book;
    private DateTimeFormatter formatter;

    @Autowired
    public LibrarianAPITests(final MockMvc mockMvc, final JdbcTemplate jdbcTemplate) {
        this.mockMvc = mockMvc;
        this.jdbcTemplate = jdbcTemplate;
        this.bookDAO = new BookDAO(jdbcTemplate);
        this.patronDAO = new PatronDAO(jdbcTemplate);
        this.borrowingDAO = new BorrowingDAO(jdbcTemplate);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public void setUp() {
        patron = new Patron();
        patron.setName("Patron");
        patron.setMobile("11111111111");

        book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPublisher("New Publisher");
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationYear(2015);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Create valid book")
    public void createValidBook() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8088/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andReturn();

        int status = result.getResponse().getStatus();
        int id = Integer.parseInt(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(id > 0);

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", id);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Create non-valid book")
    public void createNonValidBook() throws Exception {
        // Arrange
        Book nonValidBook = new Book();

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8088/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nonValidBook)))
                .andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Update valid book")
    public void updateValidBook() throws Exception {
        // Arrange
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8088/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean updated = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(updated);

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Update non-valid book")
    public void updateNonValidBook() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8088/api/books/" + -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean updated = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(updated);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Delete valid book")
    public void deleteValidBook() throws Exception {
        // Arrange
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);

        // Act
        MvcResult result = mockMvc.perform(delete("http://localhost:8088/api/books/" + bookId))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean deleted = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(deleted);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Delete non-valid book")
    public void deleteNonValidBook() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(delete("http://localhost:8088/api/books/" + -1))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean deleted = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(deleted);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Get All book")
    public void getAllBooks() throws Exception {
        // Arrange
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);
        book.setId(bookId);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8088/api/books"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        List<Book> books = new ObjectMapper().readValue(content, new TypeReference<>() {});

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(books.contains(book));

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Get valid book by ID")
    public void getValidBookById() throws Exception {
        // Arrange
        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);
        book.setId(bookId);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8088/api/books/" + bookId))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        Book bookFound = new ObjectMapper().readValue(content, new TypeReference<>() {});

        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(book, bookFound);

        // Clean
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Get non-valid book by ID")
    public void getNonValidBookById() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8088/api/books/" + -1))
                .andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(HttpStatus.NOT_FOUND.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        // Clean
    }

    //---------------------------------------------------------------------------------------

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Create valid patron")
    public void createValidPatron() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8088/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patron)))
                .andReturn();

        int status = result.getResponse().getStatus();
        int id = Integer.parseInt(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(id > 0);

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", id);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Create non-valid patron")
    public void createNonValidPatron() throws Exception {
        // Arrange
        Patron nonValidPatron = new Patron();

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8088/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nonValidPatron)))
                .andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Update valid patron")
    public void updateValidPatron() throws Exception {
        // Arrange
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8088/api/patrons/" + patronId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patron)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean updated = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(updated);

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Update non-valid patron")
    public void updateNonValidPatron() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8088/api/patrons/" + -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patron)))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean updated = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(updated);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Delete valid patron")
    public void deleteValidPatron() throws Exception {
        // Arrange
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);

        // Act
        MvcResult result = mockMvc.perform(delete("http://localhost:8088/api/patrons/" + patronId))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean deleted = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(deleted);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Delete non-valid patron")
    public void deleteNonValidPatron() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(delete("http://localhost:8088/api/patrons/" + -1))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean deleted = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(deleted);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Get All Patrons")
    public void getAllPatrons() throws Exception {
        // Arrange
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8088/api/patrons"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        List<Patron> patrons = new ObjectMapper().readValue(content, new TypeReference<>() {});

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(patrons.contains(patron));

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Get valid Patron by ID")
    public void getValidPatronById() throws Exception {
        // Arrange
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8088/api/patrons/" + patronId))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        Patron patronFound = new ObjectMapper().readValue(content, new TypeReference<>() {});

        assertEquals(HttpStatus.OK.value(), status);
        assertEquals(patron, patronFound);

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Get non-valid Patron by ID")
    public void getNonValidPatronById() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(get("http://localhost:8088/api/patrons/" + -1))
                .andReturn();

        int status = result.getResponse().getStatus();

        assertEquals(HttpStatus.NOT_FOUND.value(), status);
        assertEquals("", result.getResponse().getContentAsString());

        // Clean
    }

    //---------------------------------------------------------------------------------------

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Patron borrows book")
    public void patronBorrowBook() throws Exception {
        // Arrange
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);

        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8088/api/borrow/" + bookId + "/patron/" + patronId))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean borrowed = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(borrowed);

        // Clean
        jdbcTemplate.update("DELETE FROM BORROWING WHERE patron_id = ? AND book_id = ?",
                patronId, bookId);
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Patron borrows book with wrong IDs")
    public void patronBorrowBookWithWrongIDs() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(post("http://localhost:8088/api/borrow/" + -1 + "/patron/" + -1))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean borrowed = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(borrowed);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Patron returns book")
    public void patronReturnBook() throws Exception {
        // Arrange
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);

        int bookId = bookDAO.create(book);
        assertTrue(bookId > 0);

        Borrowing borrowing = new Borrowing();
        borrowing.setPatronId(patronId);
        borrowing.setBookId(bookId);
        borrowing.setBorrowingDate(LocalDate.now().format(formatter));

        boolean borrowed = borrowingDAO.borrow(borrowing);
        assertTrue(borrowed);

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8088/api/borrow/" + bookId + "/patron/" + patronId))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean returned = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), status);
        assertTrue(returned);

        // Clean
        jdbcTemplate.update("DELETE FROM BORROWING WHERE patron_id = ? AND book_id = ?",
                patronId, bookId);
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
        jdbcTemplate.update("DELETE FROM BOOK WHERE id = ?", bookId);
    }

    @Test
    @Transactional
    @DisplayName("LibrarianAPI - Patron returns book with wrong IDs")
    public void patronReturnBookWithWrongIDs() throws Exception {
        // Arrange

        // Act
        MvcResult result = mockMvc.perform(put("http://localhost:8088/api/borrow/" + -1 + "/patron/" + -1))
                .andReturn();

        int status = result.getResponse().getStatus();
        boolean returned = Boolean.parseBoolean(result.getResponse().getContentAsString());

        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        assertFalse(returned);

        // Clean
    }
}
