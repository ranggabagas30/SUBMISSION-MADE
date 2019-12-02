package com.rangga.submission.data.database.entity;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rangga.submission.data.database.MovieCatalogueDb;

import java.util.List;

import io.reactivex.Completable;

public class MovieRepository {

    private MovieDao dao;

    public MovieRepository(Application application) {
        MovieCatalogueDb db = MovieCatalogueDb.getDatabase(application);
        dao = db.dao();
    }

    public LiveData<List<Movie>> getAllItems() {
        return dao.getAllItems();
    }

    public LiveData<List<Movie>> getItemsByType(int type) {
        return dao.getItemsByType(type);
    }

    public LiveData<List<Movie>> getFavoriteItemsByType(int type) {
        return dao.getFavoriteItemsByType(type);
    }

    public Completable insert(Movie ... movies) {
        return dao.insert(movies);
    }

    public Completable insert(List<Movie> movies) {
        return dao.insert(movies);
    }

    public Completable delete(Movie movie) {
        return dao.delete(movie);
    }
}
