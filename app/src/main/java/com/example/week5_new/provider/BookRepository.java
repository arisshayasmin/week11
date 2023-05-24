package com.example.week5_new.provider;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {

    private BookDao mBookDao;
    private LiveData<List<BookItem>> mAllBooks;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBooks();
    }
    LiveData<List<BookItem>> getAllBooks() {
        return mAllBooks;
    }
    void insert(BookItem bookItem) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(bookItem));
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteAllBooks();
        });
    }

    // extra task w7
    void deleteBookBasedOnPrice(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteBookBasedOnPrice();
        });
    }
}


