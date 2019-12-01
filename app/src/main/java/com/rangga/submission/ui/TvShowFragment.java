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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rangga.submission.R;
import com.rangga.submission.adapter.MovieAdapter;
import com.rangga.submission.data.network.APIHelper;
import com.rangga.submission.data.network.pojo.TvResponse;
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
public class TvShowFragment extends Fragment implements MovieAdapter.OnItemClickListener {

    private final String TAG = TvShowFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private MovieAdapter rvMovieAdapter;
    private RecyclerView rvCategory;

    private int scrollPosition;
    private ArrayList<Movie> tvshows = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_DATA = "LIST_DATA";
    private final String SCROLL_POSITION = "SCROLL_POSITION";

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movies, container, false);

        initView(v);
        setupRecyclerView(savedInstanceState);
        downloadTvShows();
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        linearLayoutManager = (LinearLayoutManager) rvCategory.getLayoutManager();
        outState.putParcelableArrayList(LIST_DATA, tvshows);
        outState.putInt(SCROLL_POSITION, linearLayoutManager.findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(Movie tvshow) {
        Intent moveIntent = new Intent(getActivity(), DetailActivity.class);
        moveIntent.putExtra(DetailActivity.EXTRA_MOVIE, tvshow);
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
            tvshows = savedInstanceState.getParcelableArrayList(LIST_DATA);
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
            linearLayoutManager.scrollToPosition(scrollPosition);

            rvMovieAdapter.setData(tvshows);
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
            downloadTvShows();
        }
    }

    private void downloadTvShows() {
        progressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).compositeDisposable.add(
                APIHelper.getTvShows()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                tvListResponse -> {
                                    progressBar.setVisibility(View.GONE);
                                    List<TvResponse> moviesResponse = tvListResponse.getResults();
                                    for (TvResponse movieResponse : moviesResponse) {
                                        this.tvshows.add(CommonUtil.convert(movieResponse));
                                    }
                                    rvMovieAdapter.setData(this.tvshows);
                                }, error -> {
                                    progressBar.setVisibility(View.GONE);
                                    String errorMessage = NetworkUtil.handleApiError(getActivity(), error);
                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                        )
        );
    }
}