package com.example.myapplication.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Film;
import com.example.myapplication.OnItemListener;
import com.example.myapplication.R;

import java.io.Serializable;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmViewHolder> implements Serializable {

    private List<Film> films;
    private OnItemListener listener;

    public FilmAdapter(List<Film> film,OnItemListener listener) {
        this.films = film;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_card, parent, false);
        return new FilmViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film currentFilm = films.get(position);
        holder.image.setImageBitmap(currentFilm.getImage());
        holder.title.setText(currentFilm.getTitle());
        holder.genre.setText(currentFilm.getGenre());
        holder.score.setText(String.valueOf(currentFilm.getScore()));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }
}
