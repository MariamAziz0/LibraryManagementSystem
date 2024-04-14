package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Identifiable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

public abstract class DAO <T extends Identifiable>{
    protected JdbcTemplate jdbcTemplate;
    protected BeanPropertyRowMapper<T> rowMapper;
    private final String relationName;

    private static final Set<String> VALID_RELATION_NAMES = new HashSet<>();
    static {
        VALID_RELATION_NAMES.add("BOOK");
        VALID_RELATION_NAMES.add("PATRON");
    }

    public DAO(JdbcTemplate jdbcTemplate, Class<T> clazz, String relationName) {
        if (jdbcTemplate == null || clazz == null) {
            throw new IllegalArgumentException("Abstract DAO attributes must be not null.");
        }

        if (!VALID_RELATION_NAMES.contains(relationName)) {
            throw new IllegalArgumentException("Abstract DAO relationName is wrong " + relationName);
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
        String sql = "SELECT * FROM " + relationName;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Transactional
    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM " + relationName + " WHERE id = ?";

            return jdbcTemplate.update(sql, id) > 0;
        }

        catch (Exception e) {
            System.out.println(this.getClass().getName() + ", Error in " + relationName + " findById(): " + e.getMessage());
            return false;
        }
    }
}
