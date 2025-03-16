package com.mycompany.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import  java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.app.domain.Book;


public class BookDAOImpl implements BookDAO {
    private final List<Book> books = new ArrayList<>();
    private final Connection conn;

    public BookDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void addBook(Book book) {
         String sql = "INSERT INTO books (title, author, genre, available_copies) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getAvailableCopies());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId() == book.getBookId()) {
                books.set(i, book);
                System.out.println("Book updated: " + book);
                return;
            }
        }
        System.out.println("Book not found for update.");
    }

    @Override
    public void deleteBook(int bookId) {
        books.removeIf(book -> book.getBookId() == bookId);
        System.out.println("Book with ID " + bookId + " deleted.");
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int availableCopies = rs.getInt("available_copies");

                Book book = new Book();
                book.setBookId(bookId);
                book.setTitle(title);
                book.setAuthor(author);
                book.setGenre(genre);
                book.setAvailableCopies(availableCopies);
                bookList.add(new Book(bookId, title, author, genre, availableCopies));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    public Book getBookById(int bookId) {
        return books.stream().filter(book -> book.getBookId() == bookId).findFirst().orElse(null);
    }
}
