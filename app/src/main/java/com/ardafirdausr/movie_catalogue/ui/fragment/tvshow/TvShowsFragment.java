package com.ardafirdausr.movie_catalogue.ui.fragment.tvshow;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardafirdausr.movie_catalogue.R;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Resource;
import com.ardafirdausr.movie_catalogue.ui.adapter.TvShowAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment
        implements LifecycleOwner, View.OnClickListener,
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private RecyclerView rvTvShows;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;
    private TvShowsViewModel tvShowsViewModel;
    private SearchView svTvShow;
    private TvShowAdapter tvShowAdapter;
    private String searchQuery;

    public TvShowsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
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
        svTvShow = (SearchView) searchItem.getActionView();
        svTvShow.setOnQueryTextListener(this);
        searchItem.setOnActionExpandListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void bindView(View view){
        rvTvShows = view.findViewById(R.id.rv_tv_show_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
        searchQuery = "";
    }

    private void initAdapter(){
        tvShowAdapter = new TvShowAdapter();
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
                new Observer<Resource.State>() {
                    @Override
                    public void onChanged(Resource.State fetchingStatus) {
                        if(fetchingStatus == Resource.State.LOADING){
                            showLoadingState();
                        } else if(fetchingStatus == Resource.State.FAILED){
                            showRetryButton();
                        } else if(fetchingStatus == Resource.State.SUCCESS){
                            if(svTvShow != null) svTvShow.clearFocus();
                            showMoviesList();
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
        tvShowAdapter.setTvShows(tvShows);
        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, TvShow tvShow) {
                TvShowsFragmentDirections.ActionNavigationTvShowsToTvShowDetailActivity toTvShowDetailActivity
                        = TvShowsFragmentDirections.actionNavigationTvShowsToTvShowDetailActivity(tvShow.getId());
                toTvShowDetailActivity.setTvShowId(tvShow.getId());
                Navigation.findNavController(view)
                        .navigate(toTvShowDetailActivity);

            }
        });
        if(searchQuery.length() > 0) tvShowAdapter.getFilter().filter(searchQuery);
        rvTvShows.setAdapter(tvShowAdapter);
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        if(!query.trim().equals("") && query.length() > 0){
            searchQuery = query;
            tvShowsViewModel.searchTvShow(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() > 2) tvShowAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        tvShowAdapter.getFilter().filter("");
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        tvShowAdapter.getFilter().filter("");
        return true;
    }
}
