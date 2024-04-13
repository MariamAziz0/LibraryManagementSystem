package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Identifiable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

public abstract class DAO <T extends Identifiable>{
    protected JdbcTemplate jdbcTemplate;
    protected BeanPropertyRowMapper<T> rowMapper;
    private final String relationName;


    public DAO(JdbcTemplate jdbcTemplate, Class<T> clazz, String relationName) {
        if (jdbcTemplate == null || clazz == null) {
            throw new IllegalArgumentException("Abstract DAO attributes must be not null.");
        }
        if (Objects.equals(relationName, "")) {
            throw new IllegalArgumentException("Abstract DAO relationName can't be an empty string.");
        }

        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new BeanPropertyRowMapper<>(clazz);
        this.rowMapper.setPrimitivesDefaultedForNullValue(true);
        this.relationName = relationName;
    }

    @Transactional
    public int create(T objOfTypeT) {
        if (objOfTypeT == null) {
            throw new IllegalArgumentException("Entity object to create can't be null");
        }

        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(relationName).usingGeneratedKeyColumns("id");
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(objOfTypeT);

            return jdbcInsert.executeAndReturnKey(parameterSource).intValue();
        }

        catch (Exception ex) {
            System.out.println(this.getClass().getName() + ", Error in " + relationName + " create(): " + ex.getMessage());
            return -1;
        }
    }

    @Transactional
    public T findById(int id) {
        try {
            String sql = "SELECT * FROM " + relationName + " WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        }

        catch (Exception e) {
            System.out.println(this.getClass().getName() + ", Error in " + relationName + " findById(): " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<T> findAll() {
        try {
            String sql = "SELECT * FROM " + relationName;
            return jdbcTemplate.query(sql, rowMapper);
        }

        catch (Exception e) {
            System.out.println(this.getClass().getName() + ", Error in " + relationName + " findAll(): " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM " + relationName + " WHERE id = ?";
            return jdbcTemplate.update(sql, id) > 0;
        }

        catch (Exception ex) {
            System.out.println(this.getClass().getName() + ", Error in " + relationName + " delete(): " + ex.getMessage());
            return false;
        }
    }
}
