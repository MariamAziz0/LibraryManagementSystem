package DatabaseLayer.DAOs;

import BusinessLogicLayer.Entities.Patron;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class PatronDAO extends DAO<Patron> {

    public PatronDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, Patron.class, "PATRON");
    }

    @Transactional
    public boolean update(Patron patron) {
        String sql = """
                UPDATE PATRON
                SET name = ?, mobile = ?
                WHERE id = ?
                """;

        int rowsAffected = jdbcTemplate.update(sql, patron.getName(), patron.getMobile(), patron.getId());

        return rowsAffected > 0;
    }
}
