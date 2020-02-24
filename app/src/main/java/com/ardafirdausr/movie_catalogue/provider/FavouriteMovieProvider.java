package com.ardafirdausr.movie_catalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.ardafirdausr.movie_catalogue.repository.local.MovieCatalogueDatabase;
import com.ardafirdausr.movie_catalogue.repository.local.dao.MovieDao;

public class FavouriteMovieProvider extends ContentProvider {

    private MovieDao movieDao;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String TABLE_NAME = "movies";
    private static final String AUTHORITY = "com.ardafirdausr.movie_catalogue.provider";

    private static final int ALL_FAVOURITE_MOVIES = 1;
    private static final int MOVIE_ID = 2;

    static {
        // content://com.ardafirdausr.movie_catalogue/movies
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, ALL_FAVOURITE_MOVIES);
        // content://com.ardafirdausr.movie_catalogue/movies/id
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ID);
    }

    public FavouriteMovieProvider() {
        if(getContext() != null){
            movieDao = MovieCatalogueDatabase.getInstance(getContext()).getMovieDao();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case ALL_FAVOURITE_MOVIES:
//                cursor = movieDao.getFavouriteMovies();
                break;
            case MOVIE_ID:
//                cursor = movieDao.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
