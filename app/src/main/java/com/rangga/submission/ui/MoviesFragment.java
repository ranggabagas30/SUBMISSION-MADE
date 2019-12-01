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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rangga.submission.R;
import com.rangga.submission.adapter.MovieAdapter;
import com.rangga.submission.data.network.APIHelper;
import com.rangga.submission.data.network.pojo.MovieResponse;
import com.rangga.submission.model.Movie;
import com.rangga.submission.util.CommonUtil;
import com.rangga.submission.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MovieAdapter.OnItemClickListener{

    private final String TAG = MoviesFragment.class.getSimpleName(); 
    private MovieAdapter rvMovieAdapter;
    private ProgressBar progressBar;
    private RecyclerView rvCategory;

    private int scrollPosition;
    private ArrayList<Movie> movies = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_DATA = "LIST_DATA";
    private final String SCROLL_POSITION = "SCROLL_POSITION";

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        initView(v);
        setupRecyclerView(savedInstanceState);
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
        outState.putParcelableArrayList(LIST_DATA, movies);
        outState.putInt(SCROLL_POSITION, linearLayoutManager.findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent moveIntent = new Intent(getActivity(), DetailActivity.class);
        moveIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        getActivity().startActivity(moveIntent);
    }

    private void initView(View rootView) {
        progressBar = rootView.findViewById(R.id.progressBar);
        rvCategory = rootView.findViewById(R.id.rv_list_movie);
        progressBar.setIndeterminate(true);
    }

    private void setupRecyclerView(Bundle savedInstanceState) {

        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        rvMovieAdapter = new MovieAdapter(getActivity());
        rvMovieAdapter.setOnItemClickListener(this);
        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.setAdapter(rvMovieAdapter);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(LIST_DATA);
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
            linearLayoutManager.scrollToPosition(scrollPosition);

            rvMovieAdapter.setData(movies);
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
        } else {
            downloadMovies();
        }
    }

    private void downloadMovies() {
        progressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).compositeDisposable.add(
                APIHelper.getMovies()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                movieListResponse -> {
                                    progressBar.setVisibility(View.GONE);
                                    List<MovieResponse> moviesResponse = movieListResponse.getResults();
                                    for (MovieResponse movieResponse : moviesResponse) {
                                        this.movies.add(CommonUtil.convert(movieResponse));
                                    }
                                    rvMovieAdapter.setData(this.movies);
                                }, error -> {
                                    progressBar.setVisibility(View.GONE);
                                    String errorMessage = NetworkUtil.handleApiError(getActivity(), error);
                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                        )
        );
    }
}

