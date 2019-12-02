package com.rangga.submission.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.rangga.submission.R;
import com.rangga.submission.adapter.MainViewPagerAdapter;
import com.rangga.submission.data.database.entity.MovieViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationItem bnMovie;
    private AHBottomNavigationItem bnTvShows;
    private AHBottomNavigationItem bnFavorite;

    private MovieViewModel movieViewModel;
    private MainViewPagerAdapter viewPagerAdapter;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager_main);
        bottomNavigation = findViewById(R.id.bottomnav_main);

        setSupportActionBar(toolbar);

        bnMovie = new AHBottomNavigationItem(R.string.tab_text_1, R.drawable.ic_movie_24dp, R.color.color_tab_movies);
        bnTvShows = new AHBottomNavigationItem(R.string.tab_text_2, R.drawable.ic_live_tv_24dp, R.color.color_tab_tv_shows);
        bnFavorite = new AHBottomNavigationItem(R.string.tab_text_3, R.drawable.ic_favorite_24dp, R.color.color_tab_favorite);

        bottomNavigation.addItem(bnMovie);
        bottomNavigation.addItem(bnTvShows);
        bottomNavigation.addItem(bnFavorite);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.color_inactive));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.color_accent));
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setOnTabSelectedListener(onTabSelectedListener);
        viewPagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount() + 1);
        viewPager.setAdapter(viewPagerAdapter);

        compositeDisposable = new CompositeDisposable();

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getAllItems().observe(this, movies -> {
            // if there's movies update
            bottomNavigation.restoreBottomNavigation();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private AHBottomNavigation.OnTabSelectedListener onTabSelectedListener = (position, wasSelected) -> {
        viewPager.setCurrentItem(position, false);
        if (viewPager.getAdapter() != null)
            if (((MainViewPagerAdapter)viewPager.getAdapter()).getCurrentFragment() instanceof FavoriteFragment) {

            }
        return true;
    };
}
