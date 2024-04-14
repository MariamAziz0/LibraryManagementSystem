package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class BookDAO extends DAO<Book> {

    public BookDAO (JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Book.class, "BOOK");
    }

    @Transactional
    public boolean update(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Entity object to create can't be null");
        }

        String sql = """
                UPDATE BOOK
                SET title = ?, author = ?, publisher = ?,
                publication_year = ?, ISBN = ?
                WHERE id = ?
                """;

        int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getPublicationYear(), book.getIsbn(), book.getId());

        return rowsAffected > 0;
    }
}
