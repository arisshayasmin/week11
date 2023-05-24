package com.example.week5_new;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week5_new.provider.BookItem;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    List<BookItem> data = new ArrayList<>();

    public MyRecyclerViewAdapter() {}

    public void setData(List<BookItem> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {

        // full text to be shown on card
        String idText = "ID: " + data.get(position).getBookID();
        String titleText = "Title: " + data.get(position).getBookTitle();
        String descText = "Description: " + data.get(position).getBookDesc();
        String authorText = "Author: " + data.get(position).getBookAuthor();
        String isbnText = "ISBN: " + data.get(position).getBookISBN();
        String priceText = "Price: " + data.get(position).getBookPrice();

        holder.bookID.setText(idText);
        holder.bookTitle.setText(titleText);
        holder.bookDescription.setText(descText);
        holder.bookAuthor.setText(authorText);
        holder.bookISBN.setText(isbnText);
        holder.bookPrice.setText(priceText);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView bookTitle;
        public TextView bookID;
        public TextView bookISBN;
        public TextView bookPrice;
        public TextView bookAuthor;
        public TextView bookDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            bookTitle = itemView.findViewById(R.id.book_title);
            bookID = itemView.findViewById(R.id.book_id);
            bookISBN = itemView.findViewById(R.id.book_ISBN);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookDescription = itemView.findViewById(R.id.book_desc);
        }
    }
}
