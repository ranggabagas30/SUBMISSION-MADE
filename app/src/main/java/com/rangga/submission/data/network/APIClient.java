package com.rangga.submission.data.network;

import com.rangga.submission.data.network.pojo.GenreListResponse;
import com.rangga.submission.data.network.pojo.MovieListResponse;
import com.rangga.submission.data.network.pojo.TvListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface APIClient {
    // get movies
    @GET("movie")
    Single<MovieListResponse> getMovies();

    // get tv shows
    @GET("tv")
    Single<TvListResponse> getTvShows();

    @GET("/genre/movie/list")
    Single<GenreListResponse> getMovieGenre();

    // get tv genre
    @GET("/genre/tv/list")
    Single<GenreListResponse> getTvGenre();
}
