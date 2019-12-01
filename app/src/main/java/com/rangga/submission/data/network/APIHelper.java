package com.rangga.submission.data.network;

import com.rangga.submission.data.network.pojo.GenreListResponse;
import com.rangga.submission.data.network.pojo.MovieListResponse;
import com.rangga.submission.data.network.pojo.TvListResponse;

import io.reactivex.Single;

public class APIHelper {

    // get movies
    public static Single<MovieListResponse> getMovies() {
        return APIConfig.createService(APIClient.class).getMovies();
    }

    // get tv shows
    public static Single<TvListResponse> getTvShows() {
        return APIConfig.createService(APIClient.class).getTvShows();
    }

    // get movie genre
    public static Single<GenreListResponse> getMovieGenre() {
        return APIConfig.createService(APIClient.class).getMovieGenre();
    }

    // get tv genre
    public static Single<GenreListResponse> getTvGenre() {
        return APIConfig.createService(APIClient.class).getTvGenre();
    }
}
