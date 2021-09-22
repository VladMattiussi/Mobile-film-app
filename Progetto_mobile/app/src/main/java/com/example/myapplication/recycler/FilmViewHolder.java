package com.example.myapplication.recycler;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.OnItemListener;
import com.example.myapplication.R;

import java.io.Serializable;

public class FilmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable {

    public ImageView image;
    public TextView title;
    public TextView genre;
    public EditText synopsis;
    public TextView score;
    private OnItemListener itemListener;

    public FilmViewHolder(@NonNull View itemView,OnItemListener listener)  {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        title = itemView.findViewById(R.id.title);
        genre = itemView.findViewById(R.id.genre);
        score = itemView.findViewById(R.id.score);
        synopsis= itemView.findViewById(R.id.synopsis);
        itemListener = listener;


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(getAdapterPosition());
    }

}
