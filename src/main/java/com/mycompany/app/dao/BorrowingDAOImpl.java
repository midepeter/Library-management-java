package com.mycompany.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BorrowingDAOImpl implements BorrowingDAO {
    private final Connection conn;

    public BorrowingDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void borrowBook(int memberId, int bookId, Date borrowDate) {
        String sql = "INSERT INTO borrow_records (member_id, book_id, borrow_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);
            stmt.setDate(3, new java.sql.Date(borrowDate.getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error borrowing book", e);
        }
    }

    @Override
    public void returnBook(int memberId, int bookId) {
        String sql = "DELETE FROM borrow_records WHERE member_id = ? AND book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error returning book", e);
        }
    }

    @Override
    public List<String> getBorrowedBooks(int memberId) {
        String sql = "SELECT * FROM borrow_records WHERE member_id = ?";
        List<String> borrowedBooks = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String record = rs.getInt("member_id") + "," + 
                              rs.getInt("book_id") + "," + 
                              rs.getDate("borrow_date");
                borrowedBooks.add(record);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting borrowed books", e);
        }
        return borrowedBooks;
    }
}