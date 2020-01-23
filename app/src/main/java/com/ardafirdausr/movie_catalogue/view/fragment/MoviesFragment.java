package com.ardafirdausr.movie_catalogue.view.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.BuildConfig;
import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.ardafirdausr.movie_catalogue.api.movie.response.MovieList;
import com.ardafirdausr.movie_catalogue.view.activity.MovieDetailActivity;
import com.ardafirdausr.movie_catalogue.view.adapter.MovieAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements View.OnClickListener{

    private List<Movie> movies;
    private RecyclerView rvMovies;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovies = view.findViewById(R.id.rv_movie_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
        fetchNowPlayingMovies();
    }

    private void fetchNowPlayingMovies(){
        renderLoadingState();
        MovieApiInterface movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
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

    private void showMovieList(){
        MovieAdapter movieAdapter = new MovieAdapter();
        movieAdapter.setMovies(movies);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onClick(Movie movie) {
                navigateToMovieDetail(movie);
            }
        });
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void navigateToMovieDetail(Movie movie){
        Intent navigateToMovieDetailIntent = new Intent(getContext(), MovieDetailActivity.class);
        navigateToMovieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(navigateToMovieDetailIntent);
    }

    private void hideAllViews(){
        tvState.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.INVISIBLE);
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
        showMovieList();
        rvMovies.setVisibility(View.VISIBLE);
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
