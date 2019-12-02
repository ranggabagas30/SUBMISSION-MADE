package com.rangga.submission.data.database.entity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Movie ... movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Movie> movies);

    @Delete
    Completable delete(Movie movie);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAllItems();

    @Query("SELECT * FROM Movie WHERE type = :type")
    LiveData<List<Movie>> getItemsByType(int type);

    @Query("SELECT * FROM Movie WHERE type = :type AND isFavorite = 1")
    LiveData<List<Movie>> getFavoriteItemsByType(int type);
}
