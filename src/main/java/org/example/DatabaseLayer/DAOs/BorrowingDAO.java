package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Borrowing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.*;

public class BorrowingDAO {
    private final JdbcTemplate jdbcTemplate;

    public BorrowingDAO(JdbcTemplate jdbcTemplate) {
        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("JdbcTemplate Object shouldn't be a null object.");
        }

        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public boolean borrow(Borrowing borrowing) {
        if (borrowing == null) {
            throw new IllegalArgumentException("Entity object to create can't be null");
        }

        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("BORROWING");
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(borrowing);

            return jdbcInsert.execute(parameterSource) > 0;
        }

        catch (Exception e) {
            System.out.println(this.getClass().getName() + ", Error in BORROWING" + " create(): " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean returnBorrowing(Borrowing borrowing) {
        if (borrowing == null) {
            throw new IllegalArgumentException("Entity object to create can't be null");
        }

        String sql = """
                UPDATE BORROWING
                SET return_date = ?
                WHERE patron_id = ? AND book_id = ?
                """;

        int rowsAffected = jdbcTemplate.update(sql, borrowing.getReturnDate(), borrowing.getPatronId(), borrowing.getBookId());

        return rowsAffected > 0;
    }
}
