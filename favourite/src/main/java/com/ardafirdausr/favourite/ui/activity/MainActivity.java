package com.ardafirdausr.favourite.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardafirdausr.favourite.R;
import com.ardafirdausr.favourite.db.DatabaseContract;
import com.ardafirdausr.favourite.helper.MovieMappingHelper;
import com.ardafirdausr.favourite.ui.adapter.MovieAdapter;
import com.ardafirdausr.favourite.entity.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadFavouriteMoviesCallback {

    private RecyclerView rvMovies;
    private TextView tvState;
    private ProgressBar pbLoading;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initAdapter();
        initDataObserver();
    }

    private void bindView(){
        rvMovies = findViewById(R.id.rv_movie_list);
        tvState = findViewById(R.id.tv_state);
        pbLoading = findViewById(R.id.pb_loading);
    }

    private void initAdapter(){
        movieAdapter = new MovieAdapter();
    }

    private void initDataObserver(){
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(
                DatabaseContract.MovieColumns.CONTENT_URI,
                true, myObserver);

        new LoadFavouriteMoviesAsyncTask(this, this).execute();
    }

    private void renderMovieList(List<Movie> movies){
        movieAdapter.setMovies(movies);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, Movie movie) {
                Intent toDetailMovie = new Intent(getBaseContext(), MovieDetailActivity.class);
                toDetailMovie.putExtra("movieId", movie.getId());
                startActivity(toDetailMovie);
            }
        });
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
    }

    private void hideAllViews(){
        tvState.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
    }

    private void showLoadingState(){
        hideAllViews();
        tvState.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void showEmptyList(){
        hideAllViews();
        tvState.setVisibility(View.VISIBLE);
    }

    private void showMoviesList(){
        hideAllViews();
        rvMovies.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvState.setText("Loading...");
                showLoadingState();
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        if (movies.size() > 0) {
            renderMovieList(movies);
            showMoviesList();
        } else {
            renderMovieList(new ArrayList<Movie>());
            tvState.setText(getString(R.string.no_favourite_movie));
            showEmptyList();
        }
    }

    private static class LoadFavouriteMoviesAsyncTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavouriteMoviesCallback> weakCallback;

        private LoadFavouriteMoviesAsyncTask(Context context, LoadFavouriteMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver()
                    .query(DatabaseContract.MovieColumns.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
            return MovieMappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
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
            new LoadFavouriteMoviesAsyncTask(context, (LoadFavouriteMoviesCallback) context).execute();
        }
    }

}

interface LoadFavouriteMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<Movie> movies);
}
