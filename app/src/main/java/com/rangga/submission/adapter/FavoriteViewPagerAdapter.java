package com.rangga.submission.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rangga.submission.R;
import com.rangga.submission.ui.FavoriteMoviesFragment;
import com.rangga.submission.ui.FavoriteTvShowsFragment;

public class FavoriteViewPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = {R.string.tab_text_1, R.string.tab_text_2};
    private Context context;

    public FavoriteViewPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment currentFragment = new FavoriteMoviesFragment();
        switch (position) {
            case 0: {
                currentFragment = new FavoriteMoviesFragment();
                break;
            }
            case 1: {
                currentFragment = new FavoriteTvShowsFragment();
                break;
            }
        }

        return currentFragment;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TAB_TITLES[position]);
    }
}
