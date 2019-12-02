package com.rangga.submission.data.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movie")
public class Movie implements Parcelable {

    public static final int TYPE_MOVIE = 0;
    public static final int TYPE_TV_SHOWS = 1;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo
    private String movieName;

    @ColumnInfo
    private String movieDescription;

    @ColumnInfo
    private String movieRelease;

    @ColumnInfo
    private String movieGenre;

    @ColumnInfo
    private String movieRating;

    @ColumnInfo
    private String movieImageUrl;

    @ColumnInfo
    private boolean isFavorite = false;

    @ColumnInfo
    private int type;

    @ColumnInfo
    private int imgResource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        dest.writeInt(this.isFavorite ? 1 : 0);
        dest.writeInt(this.type);
        dest.writeInt(this.imgResource);
    }

    public Movie() {
    }

    public Movie(int id, String movieName, String movieDescription, String movieRelease, String movieGenre, String movieRating, String movieImageUrl, boolean isFavorite, int type, int imgResource) {
        this.id = id;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.movieRelease = movieRelease;
        this.movieGenre = movieGenre;
        this.movieRating = movieRating;
        this.movieImageUrl = movieImageUrl;
        this.isFavorite = isFavorite;
        this.type = type;
        this.imgResource = imgResource;
    }

    protected Movie(Parcel in) {
        // urutan baris disini sangat berpengaruh
        this.movieName = in.readString();
        this.movieDescription = in.readString();
        this.movieRelease = in.readString();
        this.movieGenre = in.readString();
        this.movieImageUrl = in.readString();
        this.movieRating = String.valueOf(in.readFloat());
        this.isFavorite = in.readInt() == 1;
        this.type = in.readInt();
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