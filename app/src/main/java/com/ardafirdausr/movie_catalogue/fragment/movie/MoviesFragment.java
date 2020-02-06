package com.ardafirdausr.movie_catalogue.fragment.movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.adapter.MovieAdapter;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rvMovies;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;
    private MoviesViewModel moviesViewModel;

    public MoviesFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initViewModel();
        registerObserver();
        moviesViewModel.initFetchMovies();
    }

    private void bindView(View view){
        rvMovies = view.findViewById(R.id.rv_movie_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
    }

    private void initViewModel(){
        this.moviesViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(MoviesViewModel.class);
    }

    private void registerObserver(){
        observeMovies();
        observeIsFetchData();
        observeIsFetchSuccess();
    }

    private void observeMovies(){
        moviesViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                renderMovieList(movies);
            }
        });
    }

    private void observeIsFetchData(){
        moviesViewModel.getIsFetchingData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFetchingData) {
                if(isFetchingData) showLoadingState(moviesViewModel.getMessage().getValue());
            }
        });
    }

    private void observeIsFetchSuccess(){
        moviesViewModel.getIsFetchingSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFetchSuccess) {
                if(isFetchSuccess) showMoviesList();
                else showRetryButton(moviesViewModel.getMessage().getValue());
            }
        });
    }

    private void renderMovieList(List<Movie> movies){
        MovieAdapter movieAdapter = new MovieAdapter();
        movieAdapter.setMovies(movies);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, Movie movie) {
                MoviesFragmentDirections.ActionMoviesFragmentToMovieDetailActivity toMovieDetailActivity
                        = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailActivity(movie);
                toMovieDetailActivity.setMovie(movie);
                Navigation.findNavController(view)
                        .navigate(toMovieDetailActivity);

            }
        });
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void hideAllViews(){
        tvState.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        btRetry.setVisibility(View.INVISIBLE);
    }

    private void showLoadingState(String message){
        hideAllViews();
        tvState.setText(message);
        tvState.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void showMoviesList(){
        hideAllViews();
        rvMovies.setVisibility(View.VISIBLE);
    }

    private void showRetryButton(String message){
        hideAllViews();
        tvState.setText(message);
        tvState.setVisibility(View.VISIBLE);
        btRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_retry:
                moviesViewModel.initFetchMovies();
                break;
        }
    }
}
