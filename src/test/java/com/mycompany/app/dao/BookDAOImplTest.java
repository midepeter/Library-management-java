package com.mycompany.app.dao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.mycompany.app.domain.Book;

public class BookDAOImplTest {
    @Test
    public void testAddBookSuccessful() {
        BookDAOImpl bookDAO = new BookDAOImpl(null);
        Book book = new Book(1, "Test Title", "Test Author", "Test Genre", 5);
        
        bookDAO.addBook(book);
        
        List<Book> books = bookDAO.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Test Title", books.get(0).getTitle());
    }
    
    @Test
    public void testUpdateBookSuccessful() {
        BookDAOImpl bookDAO = new BookDAOImpl(null);
        Book book = new Book(1, "Updated Title", "Updated Author", "Updated Genre", 10);
        
        bookDAO.updateBook(book);
        
        List<Book> books = bookDAO.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Updated Title", books.get(0).getTitle());
    }
    
    @Test
    public void testDeleteBookSuccessful() {
        BookDAOImpl bookDAO = new BookDAOImpl(null);
        
        bookDAO.deleteBook(1);
        
        List<Book> books = bookDAO.getAllBooks();
        assertEquals(0, books.size());
    }
    
    @Test
    public void testGetAllBooksNoBookFound() {
        BookDAOImpl bookDAO = new BookDAOImpl(null);
        List<Book> books = bookDAO.getAllBooks();
        
        assertEquals(0, books.size());
    }
    
    @Test
    public void testGetBookById() {
        BookDAOImpl bookDAO = new BookDAOImpl(null);
        Book book = new Book(1, "Test Title", "Test Author", "Test Genre", 5);
        bookDAO.addBook(book);
        
        Book foundBook = bookDAO.getBookById(1);
        
        assertNotNull(foundBook);
        assertEquals("Test Title", foundBook.getTitle());
        assertEquals("Test Author", foundBook.getAuthor());
        assertEquals("Test Genre", foundBook.getGenre());
        assertEquals(5, foundBook.getAvailableCopies());

    }
}