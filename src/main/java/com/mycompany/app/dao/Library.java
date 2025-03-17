package com.mycompany.app.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.app.domain.Book;

public class Library {
    private  List<Book> books = new ArrayList<>();
    private Map<Integer, Integer> borrowedBooks = new HashMap<>();

    public void addBook(Book book) {
        books.add(book);
        
    }
    public void borrowBook(int bookId, int memberId) {
        borrowedBooks.put(bookId, memberId);
    }
}   