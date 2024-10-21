package com.crezam.librarymanagement.services;


import com.crezam.librarymanagement.entities.Book;
import com.crezam.librarymanagement.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    private Book mockBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a mock Book object
        mockBook = new Book();
        mockBook.setIsbn("1234567890");
        mockBook.setTitle("Mock Title");
        mockBook.setAuthor("Mock Author");
        mockBook.setCategory("Fiction");
        mockBook.setAvailableCopies(10);
        mockBook.setPublishedYear(2021);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(Arrays.asList(mockBook));

        // Act
        var books = bookService.getAllBooks();

        // Assert
        assertEquals(1, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookByISBN_Success() {
        // Arrange
        when(bookRepository.findById("1234567890")).thenReturn(Optional.of(mockBook));

        // Act
        var book = bookService.getBookByISBN("1234567890");

        // Assert
        assertNotNull(book);
        assertEquals("Mock Title", book.getTitle());
        verify(bookRepository, times(1)).findById("1234567890");
    }

    @Test
    void testGetBookByISBN_NotFound() {
        // Arrange
        when(bookRepository.findById("invalidISBN")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookService.getBookByISBN("invalidISBN");
        });

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository, times(1)).findById("invalidISBN");
    }

    @Test
    void testAddBook() {
        // Arrange
        when(bookRepository.save(mockBook)).thenReturn(mockBook);

        // Act
        var savedBook = bookService.addBook(mockBook);

        // Assert
        assertNotNull(savedBook);
        assertEquals("Mock Title", savedBook.getTitle());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book updatedBook = new Book();
        updatedBook.setIsbn("1234567890");
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setCategory("Non-fiction");
        updatedBook.setAvailableCopies(5);
        updatedBook.setPublishedYear(2022);

        when(bookRepository.findById("1234567890")).thenReturn(Optional.of(mockBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // Act
        var result = bookService.updateBook("1234567890", updatedBook);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        verify(bookRepository, times(1)).findById("1234567890");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        // Arrange
        doNothing().when(bookRepository).deleteById("1234567890");

        // Act
        bookService.deleteBook("1234567890");

        // Assert
        verify(bookRepository, times(1)).deleteById("1234567890");
    }
}
