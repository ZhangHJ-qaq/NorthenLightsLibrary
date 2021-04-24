package com.example.lab2.dto;

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

    public BorrowedBookCopyDTO(Date borrowDate, String isbn, String author, String name, String uniqueBookMark) {
        this.borrowDate = borrowDate;
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.uniqueBookMark = uniqueBookMark;
    }
}
