package com.mycompany.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class BorrowingDAOImplTest {

    private Connection conn;
    private BorrowingDAOImpl borrowingDAO;

    @Before
    public void setUp() throws SQLException {
        // Replace these with your actual database credentials
        String url = "jdbc:postgresql://localhost:5432/library_db";
        String user = "postgres";
        String password = "postgres";
        conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(false); // For test isolation
        borrowingDAO = new BorrowingDAOImpl(conn);
    }

    @After
    public void tearDown() throws SQLException {
        if (conn != null) {
            conn.rollback(); // Rollback any changes made during tests
            conn.close();
        }
    }

    @Test
    public void testBorrowBook() throws SQLException {
        Date borrowDate = new Date();
        borrowingDAO.borrowBook(1, 2, borrowDate);
        
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM borrowings WHERE member_id = ? AND book_id = ?"
        );
        stmt.setInt(1, 1);
        stmt.setInt(2, 2);
        ResultSet rs = stmt.executeQuery();
        
        assertTrue(rs.next());
        assertEquals(1, rs.getInt("member_id"));
        assertEquals(2, rs.getInt("book_id"));
        assertEquals(borrowDate.getTime(), rs.getDate("borrow_date").getTime());
    }

    @Test
    public void testReturnBook() throws SQLException {
        // First borrow a book
        Date borrowDate = new Date();
        borrowingDAO.borrowBook(1, 2, borrowDate);
        
        // Then return it
        borrowingDAO.returnBook(1, 2);
        
        // Verify it's no longer in borrowings
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM borrowings WHERE member_id = ? AND book_id = ?"
        );
        stmt.setInt(1, 1);
        stmt.setInt(2, 2);
        ResultSet rs = stmt.executeQuery();
        
        assertFalse(rs.next());
    }

    @Test
    public void testGetBorrowedBooks() throws SQLException {
        // Setup: borrow some books
        Date borrowDate = new Date();
        borrowingDAO.borrowBook(1, 2, borrowDate);
        borrowingDAO.borrowBook(1, 3, borrowDate);
        
        List<String> books = borrowingDAO.getBorrowedBooks(1);
        
        assertEquals(2, books.size());
    }
}