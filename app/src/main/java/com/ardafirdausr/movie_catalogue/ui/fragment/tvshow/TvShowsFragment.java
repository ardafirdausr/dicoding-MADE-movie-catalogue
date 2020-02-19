package com.ardafirdausr.movie_catalogue.ui.fragment.tvshow;

import android.app.Application;
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
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.ui.adapter.TvShowAdapter;

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
    }

    private void bindView(View view){
        rvTvShows = view.findViewById(R.id.rv_tv_show_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
    }

    private void initViewModel(){
        Application application = null;
        if(getActivity() != null){
            application = getActivity().getApplication();
        }
        if(application != null){
            tvShowsViewModel = new ViewModelProvider(
                    this,
                    new TvShowsViewModel.Factory(application))
                    .get(TvShowsViewModel.class);
            registerObserver();
        }
    }

    private void registerObserver(){
        observeMovies();
        observeFetchingDataStatus();
        observeMessage();
    }

    private void observeMovies(){
        tvShowsViewModel.getTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                renderTvShowList(tvShows);
            }
        });
    }

    private void observeFetchingDataStatus(){
        tvShowsViewModel.getFetchingDataStatus().observe(
                getViewLifecycleOwner(),
                new Observer<TvShowsViewModel.FetchingStatus>() {
                    @Override
                    public void onChanged(TvShowsViewModel.FetchingStatus fetchingStatus) {
                        if(fetchingStatus == TvShowsViewModel.FetchingStatus.LOADING){
                            showLoadingState();
                        }
                        else if(fetchingStatus == TvShowsViewModel.FetchingStatus.SUCCESS){
                            showMoviesList();
                        }
                        else if(fetchingStatus == TvShowsViewModel.FetchingStatus.FAILED){
                            showRetryButton();
                        }
                    }
                }
        );
    }

    private void observeMessage(){
        tvShowsViewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                tvState.setText(message);
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
                        = TvShowsFragmentDirections.actionNavigationTvShowsToTvShowDetailActivity(tvShow.getId());
                toTvShowDetailActivity.setTvShowId(tvShow.getId());
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

    private void showLoadingState(){
        hideAllViews();
        tvState.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void showMoviesList(){
        hideAllViews();
        rvTvShows.setVisibility(View.VISIBLE);
    }

    private void showRetryButton(){
        hideAllViews();
        tvState.setVisibility(View.VISIBLE);
        btRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_retry){
            tvShowsViewModel.fetchingTvShows();
        }
    }

}
