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
import com.rangga.submission.data.database.entity.MovieViewModel;
import com.rangga.submission.data.network.APIHelper;
import com.rangga.submission.data.network.pojo.TvResponse;
import com.rangga.submission.data.database.entity.Movie;
import com.rangga.submission.util.CommonUtil;
import com.rangga.submission.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends BaseFragment {

    private final String TAG = TvShowFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private MovieAdapter rvMovieAdapter;
    private RecyclerView rvCategory;
    private MovieViewModel movieViewModel;

    private int scrollPosition = -1;
    private ArrayList<Movie> tvShows = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_DATA = "LIST_DATA";
    private final String SCROLL_POSITION = "SCROLL_POSITION";

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvShows = new ArrayList<>();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        initView(v);
        setupRecyclerView();
        loadData(savedInstanceState);
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        linearLayoutManager = (LinearLayoutManager) rvCategory.getLayoutManager();
        outState.putParcelableArrayList(LIST_DATA, tvShows);
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
        rvMovieAdapter.setOnItemClickListener(tvShow -> {
            Intent moveIntent = new Intent(getActivity(), DetailActivity.class);
            moveIntent.putExtra(DetailActivity.EXTRA_MOVIE, tvShow);
            getActivity().startActivity(moveIntent);
        });

        rvMovieAdapter.setOnFavoriteClickListener(tvShow -> {
            boolean isFavorite = !tvShow.isFavorite();
            tvShow.setFavorite(isFavorite);
            compositeDisposable.add(
                    movieViewModel.insert(tvShow)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        if (tvShow.isFavorite()) {
                                            Toast.makeText(getActivity(), tvShow.getMovieName() + " " + getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), tvShow.getMovieName() + " " + getString(R.string.success_remove_favorite), Toast.LENGTH_SHORT).show();
                                        }
                                    }, error -> Log.e(TAG, "failed update favorite: " + error.getMessage(), error)
                            )
            ); // update
        });

        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.setAdapter(rvMovieAdapter);
    }

    private void loadData(Bundle savedInstanceState) {
        movieViewModel.getItemsByType(Movie.TYPE_TV_SHOWS).observe(this, tvShows -> {
            this.tvShows.clear();
            if (tvShows.isEmpty()) {
                downloadTvShows();
            } else {
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

    private void downloadTvShows() {
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                APIHelper.getTvShows()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                tvListResponse -> {
                                    progressBar.setVisibility(View.GONE);
                                    List<TvResponse> moviesResponse = tvListResponse.getResults();
                                    List<Movie> tvShows = new ArrayList<>();
                                    for (TvResponse movieResponse : moviesResponse) {
                                        tvShows.add(CommonUtil.convert(movieResponse));
                                    }

                                    compositeDisposable.add(
                                            movieViewModel.insert(tvShows)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(
                                                            () -> {
                                                                Log.d(TAG, "--> insert tv shows done");
                                                            }, error -> {
                                                                Log.e(TAG, "--> failed insert tv shows: " + error.getMessage(), error);
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