package com.rangga.submission.data.database.entity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAllItems() {
        return movieRepository.getAllItems();
    }

    public LiveData<List<Movie>> getItemsByType(int type) {
        return movieRepository.getItemsByType(type);
    }

    public LiveData<List<Movie>> getFavoriteItemsByType(int type) {
        return movieRepository.getFavoriteItemsByType(type);
    }

    public Completable insert(Movie ... movies) {
        return movieRepository.insert(movies);
    }

    public Completable insert(List<Movie> movies) {
        return movieRepository.insert(movies);
    }

    public Completable delete(Movie movie) {
        return movieRepository.delete(movie);
    }
}
