package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilmSingleton implements Serializable {
    private List<Film> films = new ArrayList<>();
    private static final FilmSingleton ourInstance = new FilmSingleton();
    public static FilmSingleton getInstance() {
        return ourInstance;
    }
    private FilmSingleton() {
    }
    void addFilm(Film film) {
        films.add(film);
    }
    void clearFilm(){
        films.clear();
    }

    List<Film> getFilm() {
        return films;
    }
}
