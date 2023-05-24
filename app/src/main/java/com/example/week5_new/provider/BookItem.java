package com.example.week5_new.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookItem {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "bookID")
    private String bookID;
    @ColumnInfo(name = "bookTitle")
    private String bookTitle;
    @ColumnInfo(name = "bookAuthor")
    private String bookAuthor;
    @ColumnInfo(name = "bookDesc")
    private String bookDesc;
    @ColumnInfo(name = "bookPrice")
    private int bookPrice;
    @ColumnInfo(name = "bookISBN")
    private String bookISBN;


    public BookItem(@NonNull String bookID, String bookTitle, String bookAuthor, String bookDesc,
                    int bookPrice, String bookISBN )
    {
        this.bookID = bookID;
        this.bookAuthor = bookAuthor;
        this.bookDesc = bookDesc;
        this.bookISBN = bookISBN;
        this.bookPrice = bookPrice;
        this.bookTitle = bookTitle;
    }

    @NonNull
    public String getBookID() {
        return bookID;
    }

    public void setBookID(@NonNull String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public int getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(int bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }
}
