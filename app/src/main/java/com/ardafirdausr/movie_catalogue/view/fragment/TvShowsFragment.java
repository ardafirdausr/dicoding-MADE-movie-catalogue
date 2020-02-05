package com.ardafirdausr.movie_catalogue.view.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.ardafirdausr.movie_catalogue.api.movie.response.TvShow;
import com.ardafirdausr.movie_catalogue.api.movie.response.TvShowList;
import com.ardafirdausr.movie_catalogue.view.adapter.TvShowAdapter;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment implements View.OnClickListener{

    private List<TvShow> tvShows;
    private RecyclerView rvTvShows;
    private TextView tvState;
    private ProgressBar pbLoading;
    private Button btRetry;

    public TvShowsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTvShows = view.findViewById(R.id.rv_tv_show_list);
        tvState = view.findViewById(R.id.tv_state);
        pbLoading = view.findViewById(R.id.pb_loading);
        btRetry = view.findViewById(R.id.bt_retry);
        btRetry.setOnClickListener(this);
        fetchNowPlayingMovies();
    }

    private String getCurrentLanguage(){
        String countryCode = Locale.getDefault().getCountry();
        String languageCode = Locale.getDefault().getLanguage();
        languageCode = languageCode.equals("in") ? "id" : languageCode;
        return languageCode + "-" + countryCode;
    }

    private void fetchNowPlayingMovies(){
        renderLoadingState();
        String apiKey = BuildConfig.MOVIE_DB_API_KEY;
        String language = getCurrentLanguage();
        int page = 1;
        MovieApiInterface movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        final Call<TvShowList> tvShowListRequest = movieApi.getTvOnTheAir(apiKey, language, page);
        tvShowListRequest.enqueue(new Callback<TvShowList>() {
            @Override
            public void onResponse(Call<TvShowList> call, Response<TvShowList> response) {
                if(response.isSuccessful()){
                    tvShows = response.body().getTvShows();
                    if(tvShows.size() > 0){
                        renderPopulatedTvShowListState();
                    }
                    else {
                        renderEmptyTvShowListState();
                    }
                }
                else {
                    renderFailedFetch();
                }
            }

            @Override
            public void onFailure(Call<TvShowList> call, Throwable t) {
                renderFailedFetch();
            }
        });
    }

    private void showTvShowList(){
        TvShowAdapter tvShowAdapater = new TvShowAdapter();
        tvShowAdapater.setTvShows(tvShows);
        tvShowAdapater.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onClick(View view, TvShow tvShow) {
                TvShowsFragmentDirections.ActionNavigationTvShowsToTvShowDetailActivity toTvShowDetailActivity
                        = TvShowsFragmentDirections.actionNavigationTvShowsToTvShowDetailActivity(tvShow);
                toTvShowDetailActivity.setTvShow(tvShow);
                Navigation.findNavController(view)
                        .navigate(toTvShowDetailActivity);

            }
        });
        rvTvShows.setAdapter(tvShowAdapater);
        rvTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void hideAllViews(){
        tvState.setVisibility(View.INVISIBLE);
        rvTvShows.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        btRetry.setVisibility(View.INVISIBLE);
    }

    private void renderLoadingState(){
        hideAllViews();
        tvState.setText(R.string.loading);
        tvState.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void renderEmptyTvShowListState(){
        hideAllViews();
        tvState.setText(R.string.no_data_displayed);
        tvState.setVisibility(View.VISIBLE);
    }

    private void renderPopulatedTvShowListState(){
        hideAllViews();
        showTvShowList();
        rvTvShows.setVisibility(View.VISIBLE);
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
