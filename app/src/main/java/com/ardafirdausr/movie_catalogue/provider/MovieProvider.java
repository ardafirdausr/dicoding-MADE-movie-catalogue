package com.ardafirdausr.movie_catalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.ardafirdausr.movie_catalogue.repository.local.MovieCatalogueDatabase;
import com.ardafirdausr.movie_catalogue.repository.local.dao.MovieDao;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // TODO: Use static var in entity
    private static final String TABLE_NAME = "movies";
    private static final String AUTHORITY = "com.ardafirdausr.movie_catalogue";

    private static final int MOVIE_DIR = 0;
    private static final int MOVIE_ITEM = 1;

    static {
        // content://com.ardafirdausr.movie_catalogue/movies
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE_DIR);
        // content://com.ardafirdausr.movie_catalogue/movies/id
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ITEM);
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
        MovieDao movieDao = MovieCatalogueDatabase.getInstance(getContext()).getMovieDao();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case MOVIE_DIR:
                cursor = movieDao.getFavouriteMoviesCursor();
                break;
            case MOVIE_ITEM:
                if(uri.getLastPathSegment() != null){
                    cursor = movieDao.getMovieCursor(Long.valueOf(uri.getLastPathSegment()));
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        MovieDao movieDao = MovieCatalogueDatabase.getInstance(getContext()).getMovieDao();
        switch (uriMatcher.match(uri)) {
            case MOVIE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case MOVIE_ITEM:
                Movie movie = Movie.fromContentValues(values);
                movieDao.updateMovie(movie);
                if(getContext() != null){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return 1;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
