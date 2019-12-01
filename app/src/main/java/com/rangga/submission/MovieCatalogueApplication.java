package com.rangga.submission;

import android.app.Application;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.pixplicity.easyprefs.library.Prefs;

public class MovieCatalogueApplication extends Application {

    public static RxSharedPreferences rxSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        // prefs initialization
        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(BuildConfig.APPLICATION_ID)
                .setUseDefaultSharedPreference(true)
                .build();

        // add rx shared preferences
        SharedPreferences preferences = Prefs.getPreferences();
        rxSharedPreferences = RxSharedPreferences.create(preferences);
    }
}
