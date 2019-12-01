package com.rangga.submission.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rangga.submission.R;
import com.rangga.submission.model.Movie;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar      = findViewById(R.id.toolbar);
        TextView tvMovieName = findViewById(R.id.tv_detail_name);
        TextView tvMovieRate = findViewById(R.id.tv_detail_rate);
        TextView tvMovieGenre = findViewById(R.id.tv_detail_genre);
        TextView tvMovieRelease = findViewById(R.id.tv_detail_release);
        TextView tvMovieDesc = findViewById(R.id.tv_detail_desc);
        ImageView imgMoviePoster = findViewById(R.id.iv_detail_poster);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            setActionBarTitle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        tvMovieName.setText(movie.getMovieName());
        tvMovieRate.setText(movie.getMovieRating());
        tvMovieGenre.setText(movie.getMovieGenre());
        tvMovieRelease.setText(movie.getMovieRelease());
        tvMovieDesc.setText(movie.getMovieDescription());
        Glide.with(this)
                .load(movie.getMovieImageUrl())
                .apply(new RequestOptions())
                .into(imgMoviePoster);

        Log.d(EXTRA_MOVIE, "movie image url: " + movie.getMovieImageUrl());
    }

    private void setActionBarTitle(){
        Objects.requireNonNull(getSupportActionBar()).setTitle("Detail Movie");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}