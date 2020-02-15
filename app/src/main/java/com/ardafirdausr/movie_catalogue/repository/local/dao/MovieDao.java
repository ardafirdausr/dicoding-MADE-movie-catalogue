package com.ardafirdausr.movie_catalogue.repository.local.dao;

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

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movies WHERE id=:id")
    LiveData<Movie> getMovie(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovies(List<Movie> movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

}
