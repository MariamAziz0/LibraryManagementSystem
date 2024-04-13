package org.example.DatabaseLayer.DAOs;

import org.example.BusinessLogicLayer.Entities.Patron;
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
public class PatronDAOTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PatronDAO patronDAO;

    @BeforeAll
    public void setUp() {
        patronDAO = new PatronDAO(jdbcTemplate);
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Create valid patron")
    public void createValidPatron() {
        // Arrange
        Patron patron = new Patron();

        patron.setName("Patron");
        patron.setMobile("11111111111");

        // Act
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Create null patron")
    public void createNullPatron() {
        // Arrange

        // Act
        assertThrows(IllegalArgumentException.class, () -> patronDAO.create(null));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Create non-valid patron")
    public void createNonValidPatron() {
        // Arrange
        Patron patron = new Patron();

        patron.setName("Patron");

        // Act
        int patronId = patronDAO.create(patron);
        assertEquals(-1, patronId);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Find by ID valid patron")
    public void findByIdValidPatron() {
        // Arrange
        Patron patron = new Patron();

        patron.setName("Patron");
        patron.setMobile("11111111111");

        // Act
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        Patron foundPatron = patronDAO.findById(patronId);
        assertNotNull(foundPatron);
        assertEquals(patronId, foundPatron.getId());
        assertEquals(patron.getName(), foundPatron.getName());

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Find by ID non-valid patron")
    public void findByIdNonValidPatron() {
        // Arrange

        // Act
        Patron foundPatron = patronDAO.findById(-1);
        assertNull(foundPatron);

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Find All valid patrons")
    public void findAllValidPatrons() {
        // Arrange
        Patron patron = new Patron();

        patron.setName("Patron");
        patron.setMobile("11111111111");

        Patron patron2 = new Patron();
        patron2.setName("Patron2");
        patron2.setMobile("22222222222");

        // Act
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        int patronId2 = patronDAO.create(patron2);
        assertTrue(patronId2 > 0);
        patron2.setId(patronId2);

        List<Patron> foundPatrons = patronDAO.findAll();
        assertNotNull(foundPatrons);
        assertTrue(foundPatrons.contains(patron));
        assertTrue(foundPatrons.contains(patron2));

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId2);
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Delete valid patron")
    public void deleteValidPatron() {
        // Arrange
        Patron patron = new Patron();

        patron.setName("Patron");
        patron.setMobile("11111111111");

        // Act
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        assertTrue(patronDAO.delete(patronId));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Delete non-valid patron")
    public void deleteNonValidPatron() {
        // Arrange

        // Act
        assertFalse(patronDAO.delete(-1));

        // Clean
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Update valid patron")
    public void updateValidPatron() {
        // Arrange
        Patron patron = new Patron();

        patron.setName("Patron");
        patron.setMobile("11111111111");

        // Act
        int patronId = patronDAO.create(patron);
        assertTrue(patronId > 0);
        patron.setId(patronId);

        patron.setName("Another Patron");
        assertTrue(patronDAO.update(patron));

        Patron foundPatron = patronDAO.findById(patronId);
        assertEquals(patron.getName(), foundPatron.getName());

        // Clean
        jdbcTemplate.update("DELETE FROM PATRON WHERE id = ?", patronId);
    }

    @Test
    @Transactional
    @DisplayName("PatronDAO - Update non-valid patron")
    public void updateNonValidPatron() {
        // Arrange
        Patron patron = new Patron();
        patron.setId(-1);

        // Act
        assertFalse(patronDAO.update(patron));

        // Clean
    }

}
