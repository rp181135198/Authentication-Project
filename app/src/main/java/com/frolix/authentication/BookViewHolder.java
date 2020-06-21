package com.frolix.authentication;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

class BookViewHolder extends RecyclerView.ViewHolder {

    private View view;

    BookViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }

    void setDetails(Context context, String bookName, String bookAuthor, String bookImage) {
        TextView textViewBookName = view.findViewById(R.id.book_name);
        TextView textViewAuthor = view.findViewById(R.id.book_author);
        ImageView imageViewBookImage = view.findViewById(R.id.book_image);

        textViewBookName.setText(bookName);
        textViewAuthor.setText(bookAuthor);
        Picasso.get().load(bookImage).into(imageViewBookImage);
    }
}
