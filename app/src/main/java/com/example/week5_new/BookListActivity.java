package com.example.week5_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

//        getSupportFragmentManager().beginTransaction().replace(R.id.bookListFragmentContainer, new FragmentList()).addToBackStack("f1").commit();

    }
}