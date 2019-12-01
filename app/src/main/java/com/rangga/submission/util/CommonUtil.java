package com.rangga.submission.util;

import com.rangga.submission.data.network.APIConfig;
import com.rangga.submission.data.network.pojo.MovieResponse;
import com.rangga.submission.data.network.pojo.TvResponse;
import com.rangga.submission.model.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    public static String imageUrl(String size, String imagename) {
        return APIConfig.IMAGE_BASE_URL + size + imagename;
    }

    public static Movie convert(MovieResponse movieResponse) {
        Movie movie = new Movie();
        movie.setMovieName(movieResponse.getTitle());
        movie.setMovieDescription(movieResponse.getOverview());
        movie.setMovieRating(String.valueOf(movieResponse.getVoteAverage()));
        movie.setMovieRelease(convertDateFormat(movieResponse.getReleaseDate(), Constants.DATE_FORMAT1, Constants.DATE_FORMAT2));
        movie.setMovieImageUrl(imageUrl(Constants.IMAGESIZE_W342, movieResponse.getPosterPath()));
        return movie;
    }

    public static Movie convert(TvResponse tvResponse) {
        Movie movie = new Movie();
        movie.setMovieName(tvResponse.getOriginalName());
        movie.setMovieDescription(tvResponse.getOverview());
        movie.setMovieRating(String.valueOf(tvResponse.getVoteAverage()));
        movie.setMovieRelease(convertDateFormat(tvResponse.getFirstAirDate(), Constants.DATE_FORMAT1, Constants.DATE_FORMAT2));
        movie.setMovieImageUrl(imageUrl(Constants.IMAGESIZE_W342, tvResponse.getPosterPath()));
        return movie;
    }

    public static String convertDateFormat(String inputDateString, String dateInputFormat, String outputDateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateInputFormat);
        try {
            Date date = sdf.parse(inputDateString);
            return new SimpleDateFormat(outputDateFormat).format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDateString;
    }
}
