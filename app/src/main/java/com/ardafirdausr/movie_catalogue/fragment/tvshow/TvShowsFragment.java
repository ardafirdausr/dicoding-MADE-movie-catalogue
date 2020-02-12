package com.ardafirdausr.movie_catalogue.fragment.tvshow;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.api.movie.response.TvShow;
import com.ardafirdausr.movie_catalogue.adapter.TvShowAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rvTvShows;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;
    private TvShowsViewModel tvShowsViewModel;

    public TvShowsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initViewModel();
        registerObserver();
        tvShowsViewModel.initFetchMovies();
    }

    private void bindView(View view){
        rvTvShows = view.findViewById(R.id.rv_tv_show_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
    }

    private void initViewModel(){
        this.tvShowsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(TvShowsViewModel.class);
    }

    private void registerObserver(){
        observeMovies();
        observeIsFetchData();
        observeIsFetchSuccess();
    }

    private void observeMovies(){
        tvShowsViewModel.getTvShows().observe(this, new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                renderTvShowList(tvShows);
            }
        });
    }

    private void observeIsFetchData(){
        tvShowsViewModel.getIsFetchingData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFetchingData) {
                if(isFetchingData) showLoadingState(tvShowsViewModel.getMessage().getValue());
            }
        });
    }

    private void observeIsFetchSuccess(){
        tvShowsViewModel.getIsFetchingSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFetchSuccess) {
                if(isFetchSuccess) showMoviesList();
                else showRetryButton(tvShowsViewModel.getMessage().getValue());
            }
        });
    }

    private void renderTvShowList(List<TvShow> tvShows){
        TvShowAdapter movieAdapter = new TvShowAdapter();
        movieAdapter.setTvShows(tvShows);
        movieAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, TvShow tvShow) {
                TvShowsFragmentDirections.ActionNavigationTvShowsToTvShowDetailActivity toTvShowDetailActivity
                        = TvShowsFragmentDirections.actionNavigationTvShowsToTvShowDetailActivity(tvShow);
                toTvShowDetailActivity.setTvShow(tvShow);
                Navigation.findNavController(view)
                        .navigate(toTvShowDetailActivity);

            }
        });
        rvTvShows.setAdapter(movieAdapter);
        rvTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void hideAllViews(){
        tvState.setVisibility(View.INVISIBLE);
        rvTvShows.setVisibility(View.INVISIBLE);
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
        rvTvShows.setVisibility(View.VISIBLE);
    }

    private void showRetryButton(String message){
        hideAllViews();
        tvState.setText(message);
        tvState.setVisibility(View.VISIBLE);
        btRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_retry) tvShowsViewModel.initFetchMovies();
    }

}
