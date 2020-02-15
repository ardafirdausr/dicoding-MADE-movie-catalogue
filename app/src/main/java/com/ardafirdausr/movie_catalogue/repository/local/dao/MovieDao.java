package com.ardafirdausr.movie_catalogue.repository.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movies WHERE id=:movieId")
    LiveData<Movie> getMovie(long movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovies(List<Movie> movies);

    @Query("UPDATE movies SET isFavourite = 1 WHERE id=:movieId")
    void addMovieToFavourite(long movieId);

    @Query("UPDATE movies SET isFavourite = 0 WHERE id=:movieId")
    void removeMovieFromFavourite(long movieId);

}
