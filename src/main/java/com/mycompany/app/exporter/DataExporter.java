package com.mycompany.app.exporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.mycompany.app.domain.Book;
import com.mycompany.app.domain.Member;

public class DataExporter {
    private static final String BOOKS_CSV = "books.csv";
    private static final String MEMBERS_CSV = "members.csv";

    public static void exportBooks(List<Book> books) {
        exportToCSV(BOOKS_CSV, books, new String[]{"ID", "Title", "Author", "Year"});
    }

    public static void exportMembers(List<Member> members) {
        exportToCSV(MEMBERS_CSV, members, new String[]{"ID", "Name", "Email", "Phone"});
    }

    private static <T> void exportToCSV(String fileName, List<T> data, String[] headers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.join(",", headers));
            writer.newLine();
            for (T record : data) {
                if (record instanceof Book) {
                    Book book = (Book) record;
                    writer.write(String.format("%d,%s,%s,%s", 
                        book.getBookId(), 
                        book.getTitle(), 
                        book.getAuthor(), 
                        book.getGenre()));
                } else if (record instanceof Member) {
                    Member member = (Member) record;
                    writer.write(String.format("%d,%s,%s,%s",
                        member.getMemberId(),
                        member.getName(),
                        member.getEmail(),
                        member.getPhone()));
                }
                writer.newLine();
            }
            System.out.println("Exported data to " + fileName);
        } catch (IOException e) {
            System.err.println("Error exporting to " + fileName + ": " + e.getMessage());
        }
    }
}
