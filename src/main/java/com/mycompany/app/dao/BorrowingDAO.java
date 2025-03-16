package com.mycompany.app.dao;


import java.util.Date;
import java.util.List;

public interface BorrowingDAO {
    void borrowBook(int memberId, int bookId, Date borrowDate);
    void returnBook(int memberId, int bookId);
    List<String> getBorrowedBooks(int memberId);
}
