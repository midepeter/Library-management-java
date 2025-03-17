package com.mycompany.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mycompany.app.domain.Borrowing;


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
    public List<Borrowing> getAllBorrowings() {
        String sql = "SELECT * FROM borrow_records";
        List<Borrowing> borrowings = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Borrowing borrowing = new Borrowing(
                    rs.getInt("member_id"),
                    rs.getInt("book_id"),
                    rs.getDate("borrow_date")
                );
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all borrowings", e);
        }
        return borrowings;
    }

    @Override
    public List<Borrowing> getBorrowingsByBookId(int bookId) {
        String sql = "SELECT * FROM borrow_records WHERE book_id = ?";
        List<Borrowing> borrowings = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Borrowing borrowing = new Borrowing(
                    rs.getInt("member_id"),
                    rs.getInt("book_id"),
                    rs.getDate("borrow_date")
                );
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting borrowings by book ID", e);
        }
        return borrowings;
    }

    @Override 
    public List<Borrowing> getBorrowingsByMemberId(int memberId) {
        String sql = "SELECT * FROM borrow_records WHERE member_id = ?";
        List<Borrowing> borrowings = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Borrowing borrowing = new Borrowing(
                    rs.getInt("member_id"),
                    rs.getInt("book_id"),
                    rs.getDate("borrow_date")
                );
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting borrowings by member ID", e);
        }
        return borrowings;
    }
}