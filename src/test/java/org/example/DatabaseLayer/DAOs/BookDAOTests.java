package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Book;
import org.example.librarymanagementsystem.LibraryManagementSystemApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
    @DisplayName("BookDAO - Create valid book")
    public void createValidBook() {
        Book book = new Book();

        book.setTitle("New Title");
        book.setAuthor("New Author");
    }
}
