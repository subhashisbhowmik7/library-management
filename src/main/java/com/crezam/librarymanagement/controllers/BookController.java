package com.crezam.librarymanagement.controllers;


import com.crezam.librarymanagement.entities.Book;
import com.crezam.librarymanagement.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByISBN(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByISBN(isbn));
    }

    @PreAuthorize("hasRole('ACTIVE')")
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(isbn, book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }
}
