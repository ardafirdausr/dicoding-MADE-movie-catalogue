package com.ardafirdausr.movie_catalogue.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.BuildConfig;
import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.ardafirdausr.movie_catalogue.api.movie.response.MovieList;
import com.ardafirdausr.movie_catalogue.view.adapter.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements View.OnClickListener {

    private MovieApiInterface movieApi;
    private MovieAdapter movieAdapter;
    private List<Movie> movies;
    private ListView lvMovies;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        initialize();
        bindView();
        fetchNowPlayingMovies();
    }

    private void bindView(){
        lvMovies = findViewById(R.id.lv_movie_list);
        tvState = findViewById(R.id.tv_state);
        pbLoading = findViewById(R.id.pb_loading);
        btRetry = findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
    }

    private void initialize(){
        movies = new ArrayList<>();
    }

    private void fetchNowPlayingMovies(){
        renderLoadingState();
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        final Call<MovieList> movieListRequest = movieApi.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, 1);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        movieListRequest.enqueue(new Callback<MovieList>() {
                            @Override
                            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                                if(response.isSuccessful()){
                                    movies = response.body().getMovies();
                                    if(movies.size() > 0){
                                        renderPopulatedMovieListState();
                                    }
                                    else {
                                        renderEmptyMovieListState();
                                    }
                                }
                                else {
                                    renderFailedFetch();
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieList> call, Throwable t) {
                                renderFailedFetch();
                            }
                        });
                    }
                },
                1200);
    }

    private void setMovieAdapater(){
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setMovies(movies);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onClick(Movie movie) {
                navigateToMovieDetail(movie);
            }
        });
        lvMovies.setAdapter(movieAdapter);
    }

    public void navigateToMovieDetail(Movie movie){
        Intent navigateToMovieDetailIntent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
        navigateToMovieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(navigateToMovieDetailIntent);
    }

    private void hideAllViews(){
        tvState.setVisibility(View.INVISIBLE);
        lvMovies.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        btRetry.setVisibility(View.INVISIBLE);
    }

    private void renderLoadingState(){
        hideAllViews();
        tvState.setText(R.string.loading);
        tvState.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void renderEmptyMovieListState(){
        hideAllViews();
        tvState.setText(R.string.no_data_displayed);
        tvState.setVisibility(View.VISIBLE);
    }

    private void renderPopulatedMovieListState(){
        hideAllViews();
        setMovieAdapater();
        lvMovies.setVisibility(View.VISIBLE);
    }

    private void renderFailedFetch(){
        hideAllViews();
        tvState.setText(R.string.fetch_data_failed);
        tvState.setVisibility(View.VISIBLE);
        btRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_retry:
                fetchNowPlayingMovies();
                break;
        }
    }
}
