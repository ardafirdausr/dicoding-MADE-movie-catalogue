package com.ardafirdausr.movie_catalogue.repository.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ardafirdausr.movie_catalogue.repository.local.dao.MovieDao;
import com.ardafirdausr.movie_catalogue.repository.local.dao.TvShowDao;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;

@Database(entities = {Movie.class, TvShow.class}, exportSchema = false, version = 1)
public abstract class MovieCatalogueDatabase extends RoomDatabase {

    private static final String DB_NAME = "movie_catalogue_db";
    private static MovieCatalogueDatabase movieCatalogueDBInstance;

    public MovieCatalogueDatabase(){}

    public static synchronized MovieCatalogueDatabase getInstance(Context context) {
        if (movieCatalogueDBInstance == null) {
            movieCatalogueDBInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MovieCatalogueDatabase.class,
                    DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return movieCatalogueDBInstance;
    }

    public abstract MovieDao getMovieDao();

    public abstract TvShowDao getTvShowDao();

}


