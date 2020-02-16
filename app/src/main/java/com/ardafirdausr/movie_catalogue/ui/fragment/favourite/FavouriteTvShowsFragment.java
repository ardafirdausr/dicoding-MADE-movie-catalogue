package com.ardafirdausr.movie_catalogue.ui.fragment.favourite;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.ui.activity.TvShowDetail.TvShowDetailActivity;
import com.ardafirdausr.movie_catalogue.ui.adapter.TvShowAdapter;

import java.util.List;

public class FavouriteTvShowsFragment extends Fragment {

    private FavouriteTvShowsViewModel favouriteTvShowsViewModel;
    private RecyclerView rvFavouriteTvShows;

    public static FavouriteTvShowsFragment newInstance() {
        return new FavouriteTvShowsFragment();
    }

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
                        renderTvShows(tvShows);
                    }
                });
    }

    private void renderTvShows(final List<TvShow> tvShows){
        TvShowAdapter tvShowAdapter = new TvShowAdapter();
        tvShowAdapter.setTvShows(tvShows);
        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, TvShow tvShow) {
                Intent toTvShowDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);
                toTvShowDetailActivity.putExtra("tvShowId", tvShow.getId());
                startActivity(toTvShowDetailActivity);
//                FavouriteTvShowsFragmentDirections.ActionFavouriteTvShowsFragmentToTvShowDetailActivity
//                        toTvShowDetailActivity = FavouriteTvShowsFragmentDirections
//                            .actionFavouriteTvShowsFragmentToTvShowDetailActivity(tvShow.getId());
//                toTvShowDetailActivity.setTvShowId(tvShow.getId());
//                Navigation.findNavController(getView())
//                        .navigate(toTvShowDetailActivity);
            }
        });
        rvFavouriteTvShows.setAdapter(tvShowAdapter);
        rvFavouriteTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
