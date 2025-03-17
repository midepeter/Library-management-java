package com.mycompany.app.dao;

import java.util.List;

import com.mycompany.app.domain.Book;

public interface BookDAO {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    List<Book> getAllBooks();
    Book getBookById(int bookId);
    List<Book> searchBook(String keyword);
}