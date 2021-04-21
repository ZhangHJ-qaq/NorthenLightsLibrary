package com.example.lab2.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookCopy {

    //书籍的四种状态
    public static final String AVAILABLE = "available";
    public static final String BORROWED = "borrowed";
    public static final String RESERVED = "reserved";
    public static final String DAMAGED = "damaged";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long bookCopyID;

    @Column
    private String status;

    @JsonIgnore
    private String isbn;

    @Column(unique = true)
    private String uniqueBookMark;//ISBN-001,ISBN-002,ISBN-003......

    @Column(unique = false)
    @JsonIgnore
    private Long libraryID;

    private Date lastRentDate;

    private Date lastReturnDate;

    @JsonIgnore
    private Long adminID;

    private String borrower;

    private String libraryName;

    public BookCopy(String status, String isbn, String uniqueBookMark, Long libraryID, Date lastRentDate, Date lastReturnDate, Long adminID) {
        this.status = status;
        this.isbn = isbn;
        this.uniqueBookMark = uniqueBookMark;
        this.libraryID = libraryID;
        this.lastRentDate = lastRentDate;
        this.lastReturnDate = lastReturnDate;
        this.adminID = adminID;
    }

    public BookCopy(Long bookCopyID, String status, String uniqueBookMark, String borrower, String libraryName, Long
            libraryID) {
        this.bookCopyID = bookCopyID;
        this.status = status;
        this.uniqueBookMark = uniqueBookMark;
        this.borrower = borrower;
        this.libraryName = libraryName;
        this.libraryID = libraryID;
    }
}