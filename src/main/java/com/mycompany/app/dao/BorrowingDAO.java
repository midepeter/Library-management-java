package com.mycompany.app.dao;


import java.util.Date;
import java.util.List;

import com.mycompany.app.domain.Borrowing;

public interface BorrowingDAO {
    void borrowBook(int memberId, int bookId, Date borrowDate);
    void returnBook(int memberId, int bookId);
    List<Borrowing> getAllBorrowings();
    List<Borrowing> getBorrowingsByBookId(int bookId);
    List<Borrowing> getBorrowingsByMemberId(int memberId);
}
