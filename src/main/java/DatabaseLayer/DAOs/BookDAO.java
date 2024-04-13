package DatabaseLayer.DAOs;

import BusinessLogicLayer.Entities.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class BookDAO extends DAO<Book> {

    public BookDAO (JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Book.class, "BOOK");
    }

    @Transactional
    public boolean update(Book book) {
        String sql = """
                UPDATE BOOK
                SET title = ?, author = ?, publisher = ?,
                publication_year = ?, ISBN = ?
                WHERE id = ?
                """;

        int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getPublication_year(), book.getIsbn(), book.getId());

        return rowsAffected > 0;
    }
}
