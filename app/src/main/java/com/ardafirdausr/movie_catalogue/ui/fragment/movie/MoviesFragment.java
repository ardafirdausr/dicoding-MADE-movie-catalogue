package com.ardafirdausr.movie_catalogue.ui.fragment.movie;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.ardafirdausr.movie_catalogue.ui.adapter.MovieAdapter;

import java.util.List;

public class MoviesFragment extends Fragment
        implements LifecycleOwner, View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView rvMovies;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;
    private MoviesViewModel moviesViewModel;
    private SearchView svMovie;

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
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        svMovie = (SearchView) searchItem.getActionView();
        svMovie.setOnQueryTextListener(this);
    }

    private void bindView(View view){
        rvMovies = view.findViewById(R.id.rv_movie_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
    }

    private void initViewModel(){
        Application application = null;
        if(getActivity() != null) application = getActivity().getApplication();
        if(application != null){
            moviesViewModel = new ViewModelProvider(
                    this,
                    new MoviesViewModel.Factory(application))
                    .get(MoviesViewModel.class);
            registerObserver();
        }
    }

    private void registerObserver(){
        observeMovies();
        observeFetchingDataStatus();
        observeMessage();
    }

    private void observeMovies(){
        moviesViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                renderMovieList(movies);
            }
        });
    }

    private void observeMessage(){
        moviesViewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                tvState.setText(message);
            }
        });
    }

    private void observeFetchingDataStatus(){
        moviesViewModel.getFetchingDataStatus()
                .observe(getViewLifecycleOwner(), new Observer<MoviesViewModel.FetchingStatus>() {
                    @Override
                    public void onChanged(MoviesViewModel.FetchingStatus fetchingDataStatus) {
                        if(fetchingDataStatus == MoviesViewModel.FetchingStatus.LOADING) {
                            showLoadingState();
                        }
                        else if(fetchingDataStatus == MoviesViewModel.FetchingStatus.FAILED){
                            showRetryButton();
                        }
                        else if(fetchingDataStatus == MoviesViewModel.FetchingStatus.SUCCESS){
                            showMoviesList();
                        }
                    }
                });
    }

    private void renderMovieList(List<Movie> movies){
        MovieAdapter movieAdapter = new MovieAdapter();
        movieAdapter.setMovie(movies);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, Movie movie) {
                MoviesFragmentDirections.ActionMoviesFragmentToMovieDetailActivity toMovieDetailActivity
                    = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailActivity(movie.getId());
                toMovieDetailActivity.setMovieId(movie.getId());
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

    private void showLoadingState(){
        hideAllViews();
        tvState.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void showMoviesList(){
        hideAllViews();
        rvMovies.setVisibility(View.VISIBLE);
    }

    private void showRetryButton(){
        hideAllViews();
        tvState.setVisibility(View.VISIBLE);
        btRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_retry){
            moviesViewModel.fetchMovies();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
