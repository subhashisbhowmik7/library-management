package com.crezam.librarymanagement.services;

import com.crezam.librarymanagement.entities.Book;
import com.crezam.librarymanagement.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByISBN(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(String isbn, Book book) {
        Book existingBook = getBookByISBN(isbn);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setCategory(book.getCategory());
        existingBook.setAvailableCopies(book.getAvailableCopies());
        existingBook.setPublishedYear(book.getPublishedYear());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
