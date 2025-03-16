package com.mycompany.app;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mycompany.app.dao.BookDAO;
import com.mycompany.app.dao.BookDAOImpl;
import com.mycompany.app.dao.MemberDAO;
import com.mycompany.app.dao.MemberDAOImpl;
import com.mycompany.app.domain.Book;
import com.mycompany.app.domain.Member;
import com.mycompany.app.exporter.DataExporter;
import com.mycompany.app.logger.Logger;


public class App {
    

        public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Logger logger = new Logger();
        DataExporter exporter = new DataExporter();
        logger.logActivity("Library system started.");

        
        Connection conn = ConnPool.getConnection();
        BookDAO bookDAO = new BookDAOImpl(conn);

        logger.logActivity("Adding books to the database.");

        bookDAO.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "Novel", 3));   
        bookDAO.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Novel", 5));
        bookDAO.addBook(new Book("1984", "George Orwell", "Novel", 2));
        bookDAO.addBook(new Book("Pride and Prejudice", "Jane Austen", "Novel", 4));
        bookDAO.addBook(new Book("Animal Farm", "George Orwell", "Novel", 1));
        bookDAO.addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "Novel", 3));
        bookDAO.addBook(new Book("To the Lighthouse", "Virginia Woolf", "Novel", 2));
        bookDAO.addBook(new Book("Brave New World", "Aldous Huxley", "Novel", 3));


        List<Book> books = bookDAO.getAllBooks();

        logger.logActivity("Exporting books to CSV file.");
        DataExporter.exportBooks(books);


        logger.logActivity("Add member to the database.");
        MemberDAO memberDAO = new MemberDAOImpl(conn);
        memberDAO.addMember(new Member("John Doe", "john.doe@email.com", "555-0123"));
        memberDAO.addMember(new Member("Jane Doe", "jane.doe@email.com", "555-0124"));
        memberDAO.addMember(new Member("Alice Smith", "alice.smith@email.com", "555-0125"));
        memberDAO.addMember(new Member("Bob Smith", "bob.smith@email.com", "555-0126"));
        memberDAO.addMember(new Member("Charlie Brown", "charlie.brown@email.com", "555-0127"));
        memberDAO.addMember(new Member("Lucy Brown", "lucy.brown@email.com", "555-0128"));

        logger.logActivity("Get all members from the database.");
        List<Member> members = memberDAO.getAllMembers();


        logger.logActivity("Exporting members to CSV file.");
        DataExporter.exportMembers(members);
        
    
        logger.logActivity("Library maagement system shutdown.");
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