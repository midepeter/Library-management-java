package com.mycompany.app;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mycompany.app.dao.BookDAO;
import com.mycompany.app.dao.BookDAOImpl;
import com.mycompany.app.dao.BorrowingDAO;
import com.mycompany.app.dao.BorrowingDAOImpl;
import com.mycompany.app.dao.MemberDAO;
import com.mycompany.app.dao.MemberDAOImpl;
import com.mycompany.app.domain.Book;
import com.mycompany.app.domain.Borrowing;
import com.mycompany.app.domain.Member;
import com.mycompany.app.exporter.DataExporter;
import com.mycompany.app.logger.Logger;


public class App {
        public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Logger logger = new Logger();
        logger.logActivity("Library system started.");

        
        LibraryCommandProcessor processor = new LibraryCommandProcessor();
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            System.out.println("Enter library commands (Enter 10 to exit):");
            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();
                if (command.trim().isEmpty()) {
                continue;
                }
                processor.processCommand(command);
            }
        }
        
    }
}

class LibraryCommandProcessor {
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final BorrowingDAO borrowingDAO;
    private final Logger logger;
    private final Connection conn;

    public LibraryCommandProcessor() throws SQLException, ClassNotFoundException {
        this.conn = ConnPool.getConnection();
        this.bookDAO = new BookDAOImpl(conn);
        this.memberDAO = new MemberDAOImpl(conn);
        this.borrowingDAO = new BorrowingDAOImpl(conn);
        this.logger = new Logger();
    }

    public void processCommand(String command) {
        String[] parts = command.split("\\s+");
        int cmd = Integer.parseInt(parts[0]);

        try {
            switch (cmd) {
                case 1 -> { 
                    bookDAO.addBook(new Book(parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
                    logger.logActivity("Added new book: " + parts[1]);
                }
                    
                case 2 -> { 
                    Book bookUpdate = new Book(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
                    bookDAO.updateBook(bookUpdate);
                }

                case 3 -> {
                    bookDAO.deleteBook(Integer.parseInt(parts[1]));
                    logger.logActivity("Deleted book: " + parts[1]);
                }

                case 4 -> {
                    List<Book> foundBooks = bookDAO.searchBook(parts[1]);
                    foundBooks.forEach(System.out::println);
                }

                case 5 -> {
                    List<Book> allBooks = bookDAO.getAllBooks();
                    allBooks.forEach(book -> {
                        String bookInfo = String.format("Book: %s by %s (%s) - %d copies available",
                                book.getTitle(),
                                book.getAuthor(),
                                book.getGenre(),
                                book.getAvailableCopies());
                        System.out.println(bookInfo);
                    });
                }

                case 6 -> {
                    memberDAO.addMember(new Member(parts[1], parts[2], parts[3]));
                    logger.logActivity("Added new member: " + parts[1]);
                }

                case 7 -> {
                    Member member = memberDAO.getMemberById(Integer.parseInt(parts[1]));
                    if (member != null) {
                        member.setName(parts[2]);
                        member.setPhone(parts[3]);
                        memberDAO.updateMember(member);
                        logger.logActivity("Updated member: " + parts[1]);
                    }
                }

                case 8 -> {
                    memberDAO.deleteMember(Integer.parseInt(parts[1]));
                    logger.logActivity("Deleted member: " + parts[1]);
                }

                case 9 -> {
                    List<Member> allMembers = memberDAO.getAllMembers();
                    allMembers.forEach(m -> System.out.println(m.toString()));
                }
                case 11 -> {
                    List<Book> borrowBook = bookDAO.searchBook(parts[1]);
                    Member borrower = memberDAO.getMemberById(Integer.parseInt(parts[2]));
                    if (borrowBook != null && !borrowBook.isEmpty() && borrower != null) {
                        for (Book book : borrowBook) {
                            if (book.getAvailableCopies() > 0) {
                                book.setAvailableCopies(book.getAvailableCopies() - 1);
                                bookDAO.updateBook(book);
                                borrowingDAO.borrowBook(borrower.getMemberId(), book.getBookId(), (java.sql.Date) new Date());
                                logger.logActivity("Member " + borrower.getName() + " borrowed " + book.getTitle());
                            }
                        }
                    }
                }

                case 12 -> {
                    Book returnBook = bookDAO.getBookById(Integer.parseInt(parts[1]));
                    Member returner = memberDAO.getMemberById(Integer.parseInt(parts[2]));
                    if (returnBook != null && returner != null) {
                        returnBook.setAvailableCopies(returnBook.getAvailableCopies() + 1);
                        bookDAO.updateBook(returnBook);
                        borrowingDAO.returnBook(returner.getMemberId(), returnBook.getBookId());
                        logger.logActivity("Member " + returner.getName() + " returned " + returnBook.getTitle());
                    }
                }

                case 13 -> { 
                    List<Borrowing> borrowings = borrowingDAO.getAllBorrowings();
                    borrowings.forEach(borrowing -> System.out.println(borrowing.toString()));
                }

                case 14 -> {
                    List<Member> membersToExport = memberDAO.getAllMembers();
                    DataExporter.exportMembers(membersToExport);
                    logger.logActivity("Exported members to CSV");
                }

                case 15 -> {
                    List<Book> booksToExport = bookDAO.getAllBooks();
                    DataExporter.exportBooks(booksToExport);
                    logger.logActivity("Exported books to CSV");
                }
                case 10 -> {
                    ConnPool.releaseConnection(conn);
                    logger.logActivity("System shutdown");
                    System.exit(0);
                }

                default -> System.out.println("Invalid command");
            }
        } catch (Exception e) {
            logger.logActivity("Error: " + e.getMessage());
        }
    }
}

class ConnPool {
    private static final int POOL_SIZE = 5;
    private static final BlockingQueue<Connection> connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
    
    static {
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection conn = createNewConnection();
                if (conn != null) {
                    connectionPool.offer(conn);
                }
            }
            System.out.println("Database connection pool initialized with " + POOL_SIZE + " connections.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection createNewConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://localhost:5432/dreamdevs";
        String user = "midepeter";
        String password = "password";

        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        try {
            Connection conn = connectionPool.poll(3, TimeUnit.SECONDS);
            if (conn != null) {
                System.out.println("Retrieved connection from pool");
                return conn;
            }
            System.out.println("Connection pool exhausted, creating new connection");
            return createNewConnection();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.offer(conn);
            System.out.println("Connection returned to pool");
        }
    }
}