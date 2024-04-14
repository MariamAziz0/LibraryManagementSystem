package org.example.APILayer;

import org.example.BusinessLogicLayer.Entities.*;
import org.example.BusinessLogicLayer.Services.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@ComponentScan(basePackages = {"org.example.BusinessLogicLayer.Services", "org.example.APILayer"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/")
public class LibrarianAPI {
    private final JdbcTemplate jdbcTemplate;
    private final LibrarianService librarianService;

    @Autowired
    public LibrarianAPI(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.librarianService = new LibrarianService(jdbcTemplate);
    }

    @GetMapping("books")
    @ResponseBody
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = this.librarianService.findAllBooks();

        return (!books.isEmpty())
                ? new ResponseEntity<>(books, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("books/{id}")
    @ResponseBody
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        Book book = this.librarianService.findBookById(id);

        return (book != null)
                ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("books")
    @ResponseBody
    public ResponseEntity<Integer> addBook(@RequestBody Book book) {
        int id  = this.librarianService.createBook(book);

        return (id > 0)
                ? new ResponseEntity<>(id, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("books/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> updateBook(@PathVariable int id, @RequestBody Book book) {
        book.setId(id);
        boolean bookUpdated = this.librarianService.updateBook(book);

        return (bookUpdated)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("books/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteBook(@PathVariable int id) {
        boolean bookDeleted = this.librarianService.deleteBook(id);

        return (bookDeleted)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("patrons")
    @ResponseBody
    public ResponseEntity<List<Patron>> getAllPatrons() {
        List<Patron> patrons = this.librarianService.findAllPatrons();

        return (!patrons.isEmpty())
                ? new ResponseEntity<>(patrons, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("patrons/{id}")
    @ResponseBody
    public ResponseEntity<Patron> getPatronById(@PathVariable int id) {
        Patron patron = this.librarianService.findPatronById(id);

        return (patron != null)
                ? new ResponseEntity<>(patron, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("patrons")
    @ResponseBody
    public ResponseEntity<Integer> addPatron(@RequestBody Patron patron) {
        int id  = this.librarianService.createPatron(patron);

        return (id > 0)
                ? new ResponseEntity<>(id, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("patrons/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> updatePatron(@PathVariable int id, @RequestBody Patron patron) {
        patron.setId(id);
        boolean patronUpdated = this.librarianService.updatePatron(patron);

        return (patronUpdated)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("patrons/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> deletePatron(@PathVariable int id) {
        boolean patronDeleted = this.librarianService.deletePatron(id);

        return (patronDeleted)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("borrow/{bookId}/patron/{patronId}")
    @ResponseBody
    public ResponseEntity<Boolean> borrowBook(@PathVariable int bookId, @PathVariable int patronId) {
        boolean borrowed = this.librarianService.borrowBook(bookId, patronId);

        return (borrowed)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("borrow/{bookId}/patron/{patronId}")
    @ResponseBody
    public ResponseEntity<Boolean> returnBook(@PathVariable int bookId, @PathVariable int patronId) {
        boolean returned = this.librarianService.returnBook(bookId, patronId);

        return (returned)
                ? new ResponseEntity<>(true, HttpStatus.OK)
                : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}
