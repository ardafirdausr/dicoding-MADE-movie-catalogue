package com.ardafirdausr.movie_catalogue.ui.fragment.favourite;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
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
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.ui.activity.tvShowDetail.TvShowDetailActivity;
import com.ardafirdausr.movie_catalogue.ui.adapter.TvShowAdapter;

import java.util.List;

public class FavouriteTvShowsFragment extends Fragment {

    private RecyclerView rvFavouriteTvShows;
    private TextView tvState;
    private FavouriteTvShowsViewModel favouriteTvShowsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initViewModel();
    }

    private void bindView(View view){
        tvState = view.findViewById(R.id.tv_state);
        rvFavouriteTvShows = view.findViewById(R.id.rv_tv_show_list);
    }

    private void initViewModel(){
        Application application = null;
        if(getActivity() != null) application = getActivity().getApplication();
        if(application != null){
            favouriteTvShowsViewModel = new ViewModelProvider(
                    this,
                    new FavouriteTvShowsViewModel.Factory(application))
                    .get(FavouriteTvShowsViewModel.class);
            registerObserver();
        }
    }

    private void registerObserver(){
        favouriteTvShowsViewModel.getFavouriteTvShows().observe(
                getViewLifecycleOwner(),
                new Observer<List<TvShow>>(){
                    @Override
                    public void onChanged(List<TvShow> tvShows) {
                        if(tvShows.isEmpty()) showNoFavouriteTvShowFound();
                        else renderTvShows(tvShows);
                    }
                });
    }

    private void showNoFavouriteTvShowFound(){
        tvState.setVisibility(View.VISIBLE);
        rvFavouriteTvShows.setVisibility(View.INVISIBLE);
    }

    private void renderTvShows(final List<TvShow> tvShows){
        tvState.setVisibility(View.INVISIBLE);
        rvFavouriteTvShows.setVisibility(View.VISIBLE);
        TvShowAdapter tvShowAdapter = new TvShowAdapter();
        tvShowAdapter.setTvShows(tvShows);
        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, TvShow tvShow) {
                Intent toTvShowDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);
                toTvShowDetailActivity.putExtra("tvShowId", tvShow.getId());
                startActivity(toTvShowDetailActivity);
            }
        });
        rvFavouriteTvShows.setAdapter(tvShowAdapter);
        rvFavouriteTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
