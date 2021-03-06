package com.example.lab2.dto.bookcopy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BorrowedBookCopyDTO {

    private Date borrowDate;

    private String isbn;

    private String author;

    private String name;

    private String uniqueBookMark;

    private String imagePath;

    private Date deadline;

    public BorrowedBookCopyDTO(Date borrowDate, String isbn, String author, String name, String uniqueBookMark, String imagePath, Date deadline) {
        this.borrowDate = borrowDate;
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.uniqueBookMark = uniqueBookMark;
        this.imagePath = imagePath;
        this.deadline = deadline;
    }
}
