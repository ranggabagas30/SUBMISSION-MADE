package com.rangga.submission.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String movieName;
    private String movieDescription;
    private String movieRelease;
    private String movieGenre;
    private String movieRating;
    private String movieImageUrl;
    private int imgResource;


    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public void setMovieRelease(String movieRelease) {
        this.movieRelease = movieRelease;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieName);
        dest.writeString(this.movieDescription);
        dest.writeString(this.movieRelease);
        dest.writeString(this.movieGenre);
        dest.writeString(this.movieImageUrl);
        dest.writeFloat(Float.parseFloat(String.valueOf(this.movieRating)));
        dest.writeInt(this.imgResource);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        // urutan baris disini sangat berpengaruh
        this.movieName = in.readString();
        this.movieDescription = in.readString();
        this.movieRelease = in.readString();
        this.movieGenre = in.readString();
        this.movieImageUrl = in.readString();
        this.movieRating = String.valueOf(in.readFloat());
        this.imgResource = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}