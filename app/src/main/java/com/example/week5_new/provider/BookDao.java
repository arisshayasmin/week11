package com.example.week5_new.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {


    @Query("select * from books")
    LiveData<List<BookItem>> getAllBooks();

    @Query("select * from books where bookTitle=:titleName")
    List<BookItem> getBook(String titleName);

    @Insert
    void addBook(BookItem bookItem);

    @Query("delete FROM books")
    void deleteAllBooks();


//    extra task: delete books where price > 50
    @Query("delete FROM books where bookPrice > 50")
    void deleteBookBasedOnPrice();
}
