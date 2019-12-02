package com.rangga.submission.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowsFragment extends BaseFragment {
    
    private static final String TAG = FavoriteMoviesFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private RecyclerView rvMoviesFavorite;
    private MovieViewModel movieViewModel;
    private TextView tvNoItems;

    private int scrollPosition = -1;
    private ArrayList<Movie> tvShows;
    private LinearLayoutManager linearLayoutManager;
    private MovieAdapter rvMovieAdapter;
    private final String LIST_DATA = "LIST_DATA";
    private final String SCROLL_POSITION = "SCROLL_POSITION";

    public FavoriteTvShowsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvShows = new ArrayList<>();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_movie_favorite, container, false);
        initView(root);
        setupRecyclerView();
        loadData(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        linearLayoutManager = (LinearLayoutManager) rvMoviesFavorite.getLayoutManager();
        outState.putParcelableArrayList(LIST_DATA, this.tvShows);
        outState.putInt(SCROLL_POSITION, linearLayoutManager.findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    private void initView(View root) {
        progressBar = root.findViewById(R.id.progressBar);
        rvMoviesFavorite = root.findViewById(R.id.rv_list_movie);
        tvNoItems = root.findViewById(R.id.tv_no_item);
    }

    private void setupRecyclerView() {
        rvMovieAdapter = new MovieAdapter(getActivity(), true);
        rvMovieAdapter.setOnFavoriteClickListener(movie -> {
            movie.setFavorite(false);
            compositeDisposable.add(
                    movieViewModel.insert(movie)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> Log.d(TAG, "removal done"),
                                    error -> Log.e(TAG, "error remove: " + error.getMessage(), error)
                            )
            );
        });

        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvMoviesFavorite.setLayoutManager(linearLayoutManager);
        rvMoviesFavorite.setAdapter(rvMovieAdapter);
    }

    private void loadData(Bundle savedInstanceState) {
        progressBar.setVisibility(View.VISIBLE);
        movieViewModel.getFavoriteItemsByType(Movie.TYPE_TV_SHOWS).observe(this, tvShows -> {
            progressBar.setVisibility(View.GONE);
            this.tvShows.clear();
            if (tvShows.isEmpty()) {
                //Toast.makeText(getActivity(), getString(R.string.error_empty_data), Toast.LENGTH_LONG).show();
                tvNoItems.setVisibility(View.VISIBLE);
            } else {
                tvNoItems.setVisibility(View.GONE);
                this.tvShows.addAll(tvShows);
                if (savedInstanceState != null) {
                    Log.d(TAG, "load data from saved instance state");
                    if (savedInstanceState.getParcelableArrayList(LIST_DATA) != null)
                        this.tvShows = savedInstanceState.getParcelableArrayList(LIST_DATA);
                    scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
                    linearLayoutManager.scrollToPosition(scrollPosition);
                }

                rvMovieAdapter.setData(this.tvShows);
                rvMovieAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        rvMoviesFavorite.scrollToPosition(scrollPosition);
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                    }
                });
            }
        });
    }
}
