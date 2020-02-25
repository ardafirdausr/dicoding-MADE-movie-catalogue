package com.ardafirdausr.movie_catalogue.repository.local.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY release_date DESC")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movies WHERE isFavourite = 1")
    LiveData<List<Movie>> getFavouriteMovies();

    @Query("SELECT * FROM movies WHERE isFavourite = 1")
    List<Movie> getFavouriteMoviesData();

    @Query("SELECT * FROM movies WHERE isFavourite = 1")
    Cursor getFavouriteMoviesCursor();

    @Query("SELECT * FROM movies WHERE id=:movieId")
    LiveData<Movie> getMovie(long movieId);

    @Query("SELECT * FROM movies WHERE id=:movieId")
    Cursor getMovieCursor(long movieId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMovies(List<Movie> movies);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMovie(Movie movie);

    @Update
    void updateMovie(Movie movie);

    @Query("UPDATE movies SET isFavourite = 1 WHERE id=:movieId")
    void addMovieToFavourite(long movieId);

    @Query("UPDATE movies SET isFavourite = 0 WHERE id=:movieId")
    void removeMovieFromFavourite(long movieId);

}
