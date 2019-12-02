package com.rangga.submission.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rangga.submission.R;
import com.rangga.submission.adapter.MovieAdapter;
import com.rangga.submission.data.database.entity.Movie;
import com.rangga.submission.data.database.entity.MovieViewModel;
import com.rangga.submission.data.network.APIHelper;
import com.rangga.submission.data.network.pojo.MovieResponse;
import com.rangga.submission.util.CommonUtil;
import com.rangga.submission.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends BaseFragment{

    private final String TAG = MoviesFragment.class.getSimpleName(); 
    private ProgressBar progressBar;
    private RecyclerView rvCategory;
    private MovieViewModel movieViewModel;

    private int scrollPosition = -1;
    private ArrayList<Movie> movies;
    private LinearLayoutManager linearLayoutManager;
    private MovieAdapter rvMovieAdapter;
    private final String LIST_DATA = "LIST_DATA";
    private final String SCROLL_POSITION = "SCROLL_POSITION";

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        movies = new ArrayList<>();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        initView(v);
        setupRecyclerView();
        loadData(savedInstanceState);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        linearLayoutManager = (LinearLayoutManager) rvCategory.getLayoutManager();
        outState.putParcelableArrayList(LIST_DATA, this.movies);
        outState.putInt(SCROLL_POSITION, linearLayoutManager.findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    private void initView(View rootView) {
        progressBar = rootView.findViewById(R.id.progressBar);
        rvCategory = rootView.findViewById(R.id.rv_list_movie);
        progressBar.setIndeterminate(true);
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvMovieAdapter = new MovieAdapter(getActivity());
        rvMovieAdapter.setOnItemClickListener(movie -> {
            Intent moveIntent = new Intent(getActivity(), DetailActivity.class);
            moveIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
            getActivity().startActivity(moveIntent);
        });

        rvMovieAdapter.setOnFavoriteClickListener(movie -> {
            boolean isFavorite = !movie.isFavorite();
            movie.setFavorite(isFavorite);
            compositeDisposable.add(
                    movieViewModel.insert(movie)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (movie.isFavorite()) {
                                        Toast.makeText(getActivity(), movie.getMovieName() + " " + getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), movie.getMovieName() + " " + getString(R.string.success_remove_favorite), Toast.LENGTH_SHORT).show();
                                    }
                                }, error -> Log.e(TAG, "failed update favorite: " + error.getMessage(), error)
                        )
            ); // update
        });

        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.setAdapter(rvMovieAdapter);
    }

    private void loadData(Bundle savedInstanceState) {
        movieViewModel.getItemsByType(Movie.TYPE_MOVIE).observe(this, movies -> {
            this.movies.clear();
            if (movies.isEmpty()) {
                downloadMovies();
            } else {
                this.movies.addAll(movies);
                if (savedInstanceState != null) {
                    Log.d(TAG, "load data from saved instance state");
                    if (savedInstanceState.getParcelableArrayList(LIST_DATA) != null) {
                        this.movies = savedInstanceState.getParcelableArrayList(LIST_DATA);
                    }
                    scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
                    linearLayoutManager.scrollToPosition(scrollPosition);
                }

                rvMovieAdapter.setData(this.movies);
                rvMovieAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        rvCategory.scrollToPosition(scrollPosition);
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                    }
                });
            }
        });
    }

    private void downloadMovies() {
        Log.d(TAG, "--> download movies");
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                APIHelper.getMovies()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                movieListResponse -> {
                                    progressBar.setVisibility(View.GONE);
                                    List<MovieResponse> moviesResponse = movieListResponse.getResults();
                                    List<Movie> movies = new ArrayList<>();
                                    for (MovieResponse movieResponse : moviesResponse) {
                                        movies.add(CommonUtil.convert(movieResponse));
                                    }

                                    compositeDisposable.add(
                                            movieViewModel.insert(movies)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                        () -> {
                                                            Log.d(TAG, "--> insert movies done");
                                                        }, error -> {
                                                            Log.e(TAG, "--> failed insert movies: " + error.getMessage(), error);
                                                        }
                                                )
                                    );

                                }, error -> {
                                    progressBar.setVisibility(View.GONE);
                                    String errorMessage = NetworkUtil.handleApiError(getActivity(), error);
                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                        )
        );
    }
}

