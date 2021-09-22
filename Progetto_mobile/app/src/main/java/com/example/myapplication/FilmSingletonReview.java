package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilmSingletonReview implements Serializable {
        private List<Film> films = new ArrayList<>();
        private static final FilmSingletonReview ourInstance = new FilmSingletonReview();
        public static FilmSingletonReview getInstance() {
        return ourInstance;
    }
    private FilmSingletonReview() {
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
