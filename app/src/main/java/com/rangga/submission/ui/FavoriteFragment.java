package com.rangga.submission.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rangga.submission.R;
import com.rangga.submission.adapter.FavoriteViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends BaseFragment {

    private TabLayout tabFavorite;
    private ViewPager favoriteViewPager;
    private FavoriteViewPagerAdapter favoriteViewPagerAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoriteViewPager = root.findViewById(R.id.viewpager_favorite);
        tabFavorite = root.findViewById(R.id.tabs_favorite);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoriteViewPagerAdapter = new FavoriteViewPagerAdapter(getActivity(), getChildFragmentManager());
        favoriteViewPager.setAdapter(favoriteViewPagerAdapter);
        tabFavorite.setupWithViewPager(favoriteViewPager);
    }
}
