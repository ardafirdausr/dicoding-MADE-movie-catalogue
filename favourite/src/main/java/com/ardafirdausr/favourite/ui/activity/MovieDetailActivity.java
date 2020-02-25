package com.ardafirdausr.favourite.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ardafirdausr.favourite.R;
import com.ardafirdausr.favourite.db.DatabaseContract;
import com.ardafirdausr.favourite.entity.Movie;
import com.ardafirdausr.favourite.helper.MovieMappingHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity
        implements LoadMovieCallback, UpdatedMovieCallback {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView tvTitle, tvDescription, tvRating, tvReleaseDate;
    private ImageView ivPoster, ivCover;
    private Toolbar toolbar;
    private FloatingActionButton fabFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        bindView();
        setActionBar();
        initDataObserver();
    }

    private void bindView(){
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.app_bar);
        tvTitle = findViewById(R.id.tv_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvRating = findViewById(R.id.tv_rating);
        tvDescription = findViewById(R.id.tv_description);
        ivPoster = findViewById(R.id.iv_poster);
        ivCover = findViewById(R.id.iv_cover);
        toolbar = findViewById(R.id.toolbar);
        fabFavourite = findViewById(R.id.fab_favourite);
    }

    private void setActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setActionBarTitle(final String title){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initDataObserver(){
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this);

        Bundle bundle = getIntent().getExtras();
        long movieId = bundle.getLong("movieId");
        Uri detailUri = Uri.parse(DatabaseContract.MovieColumns.CONTENT_URI + "/" + movieId);
        getContentResolver().registerContentObserver(detailUri, true, myObserver);

        new LoadMovieAsyncTask(this, this).execute(movieId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(final Movie movie) {
        if(movie == null) return;
        setActionBarTitle(movie.getTitle());
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvRating.setText(Double.toString(movie.getVote()));
        tvDescription.setText(movie.getDescription());
        Picasso.get().load(movie.getCoverUrl()).into(ivCover);
        Picasso.get()
                .load(movie.getImageUrl())
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(10, 0))
                .into(ivPoster);
        int favouriteIconDrawableId = movie.getIsFavourite()
                ? R.drawable.ic_favorite_brown_24dp
                : R.drawable.ic_favorite_border_brown_24dp;
        final UpdateMovieAsyncTask updateAction = new UpdateMovieAsyncTask(this, this);
        fabFavourite.setImageDrawable(getDrawable(favouriteIconDrawableId));
        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(movie.getIsFavourite()){
                            movie.setFavourite(false);
                            updateAction.execute(movie);
                            Toast.makeText(
                                    getApplicationContext(),
                                    R.string.success_remove_from_favourite,
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else {
                            movie.setFavourite(true);
                            updateAction.execute(movie);
                            Toast.makeText(
                                    getApplicationContext(),
                                    R.string.success_add_to_favourite,
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
            }
        });
    }

    @Override
    public void updated() {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(intent);
        onBackPressed();
    }

    private static class LoadMovieAsyncTask extends AsyncTask<Long, Void, Movie> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsyncTask(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Movie doInBackground(Long... longs) {
            Context context = weakContext.get();
            Uri detailUri = Uri.parse(DatabaseContract.MovieColumns.CONTENT_URI + "/" + longs[0]);
            Cursor dataCursor = context.getContentResolver()
                    .query(detailUri,
                            null,
                            null,
                            null,
                            null);
            List<Movie> movies = MovieMappingHelper.mapCursorToArrayList(dataCursor);
            return movies.get(0);
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<UpdatedMovieCallback> weakCallback;

        private UpdateMovieAsyncTask(Context context, UpdatedMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            Context context = weakContext.get();
            Movie movie = movies[0];
            Uri detailUri = Uri.parse(DatabaseContract.MovieColumns.CONTENT_URI + "/" + movie.getId());
            context.getContentResolver()
                    .update(detailUri,
                            Movie.toContentValues(movie),
                            null,
                            null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            weakCallback.get().updated();
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new MovieDetailActivity.LoadMovieAsyncTask(context, (LoadMovieCallback) context).execute();
        }
    }
}

interface LoadMovieCallback {
    void preExecute();
    void postExecute(Movie movie);
}

interface UpdatedMovieCallback{
    void updated();
}


