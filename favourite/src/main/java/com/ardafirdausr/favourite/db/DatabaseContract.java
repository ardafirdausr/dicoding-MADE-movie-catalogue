package com.ardafirdausr.favourite.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String AUTHORITY = "com.ardafirdausr.movie_catalogue";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class MovieColumns implements BaseColumns {

        public static final String TABLE_NAME = "movies";
        private static final int MOVIE_DIR = 0;
        private static final int MOVIE_ITEM = 1;

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String VOTE = "vote";
        public static final String RELEASE_DATE = "release_date";
        public static final String IMAGE_URL = "imageUrl";
        public static final String COVER_URL = "coverUrl";
        public static final String IS_FAVOURITE = "isFavourite";

        // create URI content://com.ardafirdausr.movie_catalogue.provider/movies
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
