package com.rangga.submission.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rangga.submission.data.database.entity.Movie;
import com.rangga.submission.data.database.entity.MovieDao;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieCatalogueDb extends RoomDatabase {

    public abstract MovieDao dao();

    private static volatile MovieCatalogueDb INSTANCE;

    public static MovieCatalogueDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieCatalogueDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieCatalogueDb.class, "MovieCatalogue")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
