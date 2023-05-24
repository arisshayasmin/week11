package com.example.week5_new.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepository mRepository;
    private LiveData<List<BookItem>> mAllBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllBooks = mRepository.getAllBooks();
    }

    public LiveData<List<BookItem>> getAllBooks() {
        return mAllBooks;
    }

    public void insert(BookItem bookItem) {
        mRepository.insert(bookItem);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void deleteBasedOnPrice(){
        mRepository.deleteBookBasedOnPrice();
    }

//    public void listAllBooks() { mRepository.listAll(); }

}
