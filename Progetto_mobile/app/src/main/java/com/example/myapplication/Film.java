package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Film implements Serializable, Parcelable {

    private Bitmap image;
    private String title;
    private String genre;
    private String synopsis;
    private int score;

    public Film(Bitmap image, String title, String genre,int score, String synopsis) {
        this.image = image;
        this.title = title;
        this.genre = genre;
        this.score = score;
        this.synopsis = synopsis;
    }

    protected Film(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        title = in.readString();
        genre = in.readString();
        synopsis = in.readString();
        score = in.readInt();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(genre);
        dest.writeString(synopsis);
        dest.writeInt(score);
    }
}
