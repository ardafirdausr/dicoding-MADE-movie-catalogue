package com.ardafirdausr.favourite.helper;

import android.database.Cursor;
import android.util.Log;

import com.ardafirdausr.favourite.db.DatabaseContract;
import com.ardafirdausr.favourite.entity.Movie;

import java.util.ArrayList;

public class MovieMappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<Movie> movieList = new ArrayList<>();
        if(movieCursor != null){
            while (movieCursor.moveToNext()) {
                Movie movie = mapCursorToObject(movieCursor);
                movieList.add(movie);
            }
        }
        return movieList;
    }

    public static Movie mapCursorToObject(Cursor movieCursor) {
        long id = movieCursor.getLong(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID));
        String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
        String description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION));
        double vote = movieCursor.getDouble(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTE));
        String releaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE));
        String imageUrl = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.IMAGE_URL));
        String coverUrl = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.COVER_URL));
        boolean isFavourite = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.IS_FAVOURITE)) > 0;

        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseDate(releaseDate);
        movie.setVote(vote);
        movie.setImageUrl(imageUrl);
        movie.setCoverUrl(coverUrl);
        movie.setFavourite(isFavourite);

        return movie;
    }
}
