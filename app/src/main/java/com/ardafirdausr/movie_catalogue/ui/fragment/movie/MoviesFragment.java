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
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Resource;
import com.ardafirdausr.movie_catalogue.ui.adapter.MovieAdapter;

import java.util.List;

public class MoviesFragment extends Fragment
        implements LifecycleOwner, View.OnClickListener,
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private RecyclerView rvMovies;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;
    private MoviesViewModel moviesViewModel;
    private SearchView svMovie;
    private MovieAdapter movieAdapter;
    private String searchQuery;

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
        initAdapter();
        initViewModel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        svMovie = (SearchView) searchItem.getActionView();
        svMovie.setOnQueryTextListener(this);
        searchItem.setOnActionExpandListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(true);
    }

    private void bindView(View view){
        rvMovies = view.findViewById(R.id.rv_movie_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
        searchQuery = "";
    }

    private void initAdapter(){
        movieAdapter = new MovieAdapter();
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
                .observe(getViewLifecycleOwner(), new Observer<Resource.State>() {
                    @Override
                    public void onChanged(Resource.State fetchingDataStatus) {
                        if(fetchingDataStatus == Resource.State.LOADING) {
                            showLoadingState();
                        }
                        else if(fetchingDataStatus == Resource.State.FAILED){
                            showRetryButton();
                        }
                        else if(fetchingDataStatus == Resource.State.SUCCESS){
                            if(svMovie != null) svMovie.clearFocus();
                            showMoviesList();
                        }
                    }
                });
    }

    private void renderMovieList(List<Movie> movies){
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
        if(searchQuery.length() > 0) movieAdapter.getFilter().filter(searchQuery);
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
        if(query.length() > 0) moviesViewModel.searchMovie(query);
        searchQuery = query;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() > 2) movieAdapter.getFilter().filter(newText);
        return true;
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        movieAdapter.getFilter().filter("");
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        movieAdapter.getFilter().filter("");
        return true;
    }
}
