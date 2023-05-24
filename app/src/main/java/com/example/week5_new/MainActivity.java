package com.example.week5_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week5_new.provider.BookItem;
import com.example.week5_new.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //     non view data
    String bookTitleString;
    String bookISBNString;
    DrawerLayout drawerLayout;

    private BookViewModel mBookViewModel;

//    ArrayList<String> bookList = new ArrayList<String>();
    ArrayAdapter adapter;

    //week 6
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerViewAdapter recyclerAdapter;
    ArrayList<BookItem> data = new ArrayList<>();

    DatabaseReference myRef;

    // week 10
    View frameLayout;
    View constraintLayout;
    int x_coordinate;
    int y_coordinate;

    // week 11
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        Log.i("lab3", "onCreate");

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS},0);

        // intent
        IntentFilter myIntentFilter = new IntentFilter("sendData");
        registerReceiver(receiver, myIntentFilter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // week 6 implementation
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook(view);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        drawerLayout = findViewById(R.id.dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView= findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // new adapter class
        recyclerAdapter = new MyRecyclerViewAdapter();
        recyclerAdapter.setData(data);

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBookViewModel.getAllBooks().observe(this, newData -> {
            recyclerAdapter.setData(newData);
            recyclerAdapter.notifyDataSetChanged();
        });

        // database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Books");

        // week 10
        frameLayout = findViewById(R.id.frame_id);
        constraintLayout = findViewById(R.id.constraint_id);

        // week 11
        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                gestureDetector.onTouchEvent(motionEvent);
                return true;

            }
        });

    }

    // simpleOnGesture... is a convenience class to gesture detector
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        // week 11 task 1
        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {

            RandomIDString randomId = new RandomIDString();

            EditText bookIdInput = findViewById(R.id.bookIDInput);
            bookIdInput.setText(randomId.generateNewRandomString());
            return super.onSingleTapUp(e);
        }

        // week 11 task 1
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {

            //clear all fields
            clearInput(findViewById(android.R.id.content));
            return super.onDoubleTap(e);
        }

        // week 11 task 2
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {

            EditText priceInput = findViewById(R.id.priceInput);
            int price = Integer.parseInt(String.valueOf(priceInput.getText()));

            EditText titleInput = findViewById(R.id.bookTitleInput);
            int differenceX = (int) (e2.getX() - e1.getX());
            int differenceY = (int) (e2.getY() - e1.getY());


            if (differenceX > 0 && Math.abs(differenceY) < 40) {
                // left to right: increment
                priceInput.setText(String.valueOf(price + Math.abs(differenceX)));
            } else if (differenceX < 0 && Math.abs(differenceY) < 40){
                // right to left: decrement
                if (price - Math.abs(differenceX) < 0) {
                    priceInput.setText("0");
                }
                else {
                    priceInput.setText(String.valueOf(price - Math.abs(differenceX)));
                }
            }

            // week 11 extra task
            if (Math.abs(differenceY) > 40) {
                titleInput.setText("untitled");
            }

//            Toast.makeText(MainActivity.this, "e1 x: " + e1.getX() + " e2 x: "
//                    + e2.getX() + " e1 y: " + e1.getY() + " e2 y: " + e2.getY(), Toast.LENGTH_SHORT).show();

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        // week 11 task 3
        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(e2.getX() - e1.getX()) < 150 && Math.abs(velocityX) > 1000) {
                moveTaskToBack(true);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        // week 11 task 3
        @Override
        public void onLongPress(@NonNull MotionEvent e) {

            // load default/saved values
            loadBook(findViewById(android.R.id.content));
            super.onLongPress(e);
        }
    }




    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.addBooks) {
                addBook(findViewById(android.R.id.content));
                recyclerAdapter.notifyDataSetChanged();
            } else if (id == R.id.removeLastBook) {
                // Do something
                removeBook(findViewById(android.R.id.content));
                recyclerAdapter.notifyDataSetChanged();
            } else if (id == R.id.removeAllBooks) {
                removeAllBooks(findViewById(android.R.id.content));
                recyclerAdapter.notifyDataSetChanged();
            } else if (id == R.id.close) {
                finish();
            } else if (id == R.id.deleteBook) {
                deleteBooksBasedOnPrice(findViewById(android.R.id.content));
                recyclerAdapter.notifyDataSetChanged();
            } else if (id == R.id.listBooks) {
                startActivity(new Intent(MainActivity.this, BookListActivity.class));
                recyclerAdapter.notifyDataSetChanged();
//                getSupportFragmentManager().beginTransaction().replace(R.id.bookListFragmentContainer, new FragmentList()).addToBackStack("f1").commit();
            }
            // close the drawer
            drawerLayout.closeDrawers();
            // tell the OS
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.load) {
            loadBook(findViewById(android.R.id.content));
        } else if (id == R.id.clear) {
            clearInput(findViewById(android.R.id.content));
        } else if (id == R.id.totalBooks) {
            Toast.makeText(MainActivity.this,"Total books is "+data.size(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lab3", "onStart");

        EditText idInput = findViewById(R.id.bookIDInput);
        EditText titleInput = findViewById(R.id.bookTitleInput);
        EditText isbnInput = findViewById(R.id.ISBNInput);
        EditText authorInput = findViewById(R.id.authorInput);
        EditText descInput = findViewById(R.id.descriptionInput);
        EditText priceInput = findViewById(R.id.priceInput);

        SharedPreferences myData = getSharedPreferences("savedData", 0);
        idInput.setText(myData.getString("id", ""));
        titleInput.setText(myData.getString("title", ""));
        isbnInput.setText(myData.getString("isbn", ""));
        authorInput.setText(myData.getString("author", ""));
        descInput.setText(myData.getString("desc", ""));
        priceInput.setText(myData.getString("price", ""));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lab3", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lab3", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lab3", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lab3", "onDestroy");
    }

    //    save the view data on screen
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState); //run all instruction from parent

        Log.i("lab3", "onSaveInstanceState");
//
        EditText bookTitle = findViewById(R.id.bookTitleInput);
        EditText bookISBN = findViewById(R.id.ISBNInput);
        bookTitleString = bookTitle.getText().toString();
        bookISBNString = bookISBN.getText().toString();
        outState.putString("key1", bookTitleString);
        outState.putString("key2", bookISBNString);
    }

    //    restore the view data to screen
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i("lab3", "onRestoreInstanceState");

        bookTitleString = savedInstanceState.getString("key1");
        bookISBNString = savedInstanceState.getString("key2");

        // clear the rest of the input
        EditText bookID = findViewById(R.id.bookIDInput);
        EditText bookAuthor = findViewById(R.id.authorInput);
        EditText bookDesc = findViewById(R.id.descriptionInput);
        EditText bookPrice = findViewById(R.id.priceInput);
        bookID.getText().clear();
        bookAuthor.getText().clear();
        bookDesc.getText().clear();
        bookPrice.getText().clear();
    }

    public void showMessage(View v) {
        TextView price = findViewById(R.id.priceInput);
        TextView title = findViewById(R.id.bookTitleInput);
        Toast confirmMessage = Toast.makeText(MainActivity.this, "Book title is " + title.getText()
                + " and the cost is " + price.getText(), Toast.LENGTH_SHORT);
        confirmMessage.show();

    }

    //        to add the book
    public void addBook(View v) {
        EditText titleInput = findViewById(R.id.bookTitleInput);
        EditText idInput = findViewById(R.id.bookIDInput);
        EditText isbnInput = findViewById(R.id.ISBNInput);
        EditText authorInput = findViewById(R.id.authorInput);
        EditText descInput = findViewById(R.id.descriptionInput);
        EditText priceInput = findViewById(R.id.priceInput);

        //String inputs
        String id = idInput.getText().toString();
        String title = titleInput.getText().toString();
        String isbn = isbnInput.getText().toString();
        String author = authorInput.getText().toString();
        String desc = descInput.getText().toString();
        String price = priceInput.getText().toString();

        SharedPreferences mySavedData = getSharedPreferences("savedData", 0);
        SharedPreferences.Editor myEditor = mySavedData.edit();
        myEditor.putString("id", id);
        myEditor.putString("title", title);
        myEditor.putString("isbn", isbn);
        myEditor.putString("author", author);
        myEditor.putString("desc", desc);
        myEditor.putString("price", price);
        myEditor.commit();

        showMessage(v);

        // listing all book titles W5
//        bookList.add(title);

        //week 6
        //call function to add new card
        addBookItem(id, title, isbn, author, desc, Integer.parseInt(price));

    }

    public void removeBook(View v){

        //week 6
        data.remove(data.size() - 1);
        recyclerAdapter.notifyDataSetChanged();
    }

    public void removeAllBooks(View v){

        // week 6
//        data.clear();
        mBookViewModel.deleteAll();
        myRef.removeValue();
        recyclerAdapter.notifyDataSetChanged();
    }

    // week 7 extra
    public void deleteBooksBasedOnPrice(View v){
        mBookViewModel.deleteBasedOnPrice();
        recyclerAdapter.notifyDataSetChanged();
    }

//    public void listBooks(View v){
//        mBookViewModel.listAllBooks();
//        recyclerAdapter.notifyDataSetChanged();
//    }

    public void clearInput(View v) {
        EditText bookTitle = findViewById(R.id.bookTitleInput);
        EditText bookID = findViewById(R.id.bookIDInput);
        EditText bookISBN = findViewById(R.id.ISBNInput);
        EditText bookAuthor = findViewById(R.id.authorInput);
        EditText bookDesc = findViewById(R.id.descriptionInput);
        EditText bookPrice = findViewById(R.id.priceInput);

        bookTitle.getText().clear();
        bookID.getText().clear();
        bookISBN.getText().clear();
        bookAuthor.getText().clear();
        bookDesc.getText().clear();
        bookPrice.getText().clear();
    }

    public void loadBook(View v) {

        EditText idInput = findViewById(R.id.bookIDInput);
        EditText titleInput = findViewById(R.id.bookTitleInput);
        EditText isbnInput = findViewById(R.id.ISBNInput);
        EditText authorInput = findViewById(R.id.authorInput);
        EditText descInput = findViewById(R.id.descriptionInput);
        EditText priceInput = findViewById(R.id.priceInput);

        SharedPreferences myData = getSharedPreferences("savedData", 0);
        idInput.setText(myData.getString("id", ""));
        titleInput.setText(myData.getString("title", ""));
        isbnInput.setText(myData.getString("isbn", ""));
        authorInput.setText(myData.getString("author", ""));
        descInput.setText(myData.getString("desc", ""));
        priceInput.setText(myData.getString("price", ""));
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // what do u want to do
            TextView idInput = findViewById(R.id.bookIDInput);
            TextView titleInput = findViewById(R.id.bookTitleInput);
            TextView isbnInput = findViewById(R.id.ISBNInput);
            TextView authorInput = findViewById(R.id.authorInput);
            TextView descInput = findViewById(R.id.descriptionInput);
            TextView priceInput = findViewById(R.id.priceInput);

            String[] receivedData = intent.getStringExtra("bookData").split("[|]");

            idInput.setText(receivedData[0]);
            titleInput.setText(receivedData[1]);
            isbnInput.setText(receivedData[2]);
            authorInput.setText(receivedData[3]);
            descInput.setText(receivedData[4]);
            priceInput.setText(receivedData[5]);

        }
    };

    public void addBookItem(String id, String title, String isbn, String author,
                            String desc, int price)
    {
        BookItem book = new BookItem(id, title,author,desc,price,isbn);
        mBookViewModel.insert(book);
        myRef.push().setValue(book);
        recyclerAdapter.notifyDataSetChanged();
    }

}