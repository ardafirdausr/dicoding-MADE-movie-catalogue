package com.ardafirdausr.movie_catalogue.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ardafirdausr.movie_catalogue.repository.local.MovieCatalogueDatabase;
import com.ardafirdausr.movie_catalogue.repository.local.dao.TvShowDao;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Resource;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Util;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.TvShowListResponse;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.TvShowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class TvShowRepository {

    private static TvShowRepository tvShowRepositoryInstance;
    private MovieApiInterface movieApi;
    private TvShowDao tvShowDao;
    private Resource<List<TvShow>> tvShows;
    private boolean isListRefreshed;
    private List<Long> refreshedTvShowIds;

    private TvShowRepository(Application application){
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        tvShowDao = MovieCatalogueDatabase.getInstance(application).getTvShowDao();
        tvShows = new Resource<>();
        isListRefreshed = false;
        refreshedTvShowIds = new ArrayList<>();
    }

    public static synchronized TvShowRepository getInstance(Application application){
        if(tvShowRepositoryInstance == null){
            tvShowRepositoryInstance = new TvShowRepository(application);
        }
        return tvShowRepositoryInstance;
    }

    public LiveData<List<TvShow>> getTvShows(){
        if(!isListRefreshed) fetchTvOnTheAir();
        tvShows.setData(tvShowDao.getTvShows());
        return tvShowDao.getTvShows();
    }

    public void searchTvShow(String tvShowTitle){
        tvShows.setState(Resource.State.LOADING);
        tvShows.setMessage("Loading...");
        movieApi.searchTvShow(Util.getApiKey(), Util.getCurrentLanguage(), tvShowTitle)
            .enqueue(new Callback<TvShowListResponse>() {
                @Override
                public void onResponse(Call<TvShowListResponse> call, Response<TvShowListResponse> response) {
                    if(response.body() != null){
                        tvShows.setState(Resource.State.SUCCESS);
                        tvShows.setMessage("Success");

                        List<TvShowResponse> tvShowResponses = response.body().getTvShows();
                        List<TvShow> tvShows = transformTvShowsResponseToTvShowEntities(tvShowResponses);
                        new InsertTvShowsAsyncTask(tvShowDao).execute(tvShows);

                    } else {
                        tvShows.setState(Resource.State.SUCCESS);
                        tvShows.setMessage("");
                    }
                }

                @Override
                public void onFailure(Call<TvShowListResponse> call, Throwable t) {
                    tvShows.setState(Resource.State.SUCCESS);
                    tvShows.setMessage("");
                }
            });
    }

    public LiveData<List<TvShow>> getFavouriteTvShows(){
        return tvShowDao.getFavouriteTvShows();
    }

    public LiveData<Resource.State> getTvShowsFetchState(){
        return tvShows.getState();
    }

    public LiveData<String> getTvShowsFetchStatusMessage(){
        return tvShows.getMessage();
    }

    public LiveData<TvShow> getTvShow(long tvShowId){
        if(!refreshedTvShowIds.contains(tvShowId)) fetchTvShow(tvShowId);
        return tvShowDao.getTvShow(tvShowId);
    }

    public void addTvShowToFavourite(long tvShowId){
        new AddTvShowToFavouriteAsyncTask(tvShowDao).execute(tvShowId);
    }

    public void removeTvShowFromFavourite(long tvShowId){
        new RemoveTvShowFromFavouriteAsyncTask(tvShowDao).execute(tvShowId);
    }


    public void fetchTvOnTheAir(){
        tvShows.setState(Resource.State.LOADING);
        tvShows.setMessage("Loading...");
        movieApi.getTvOnTheAir(Util.getApiKey(), Util.getCurrentLanguage(), 1)
                .enqueue(new Callback<TvShowListResponse>() {

                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<TvShowListResponse> call, Response<TvShowListResponse> response) {
                        if(response.body() != null){
                            tvShows.setState(Resource.State.SUCCESS);
                            tvShows.setMessage("Success");
                            isListRefreshed = true;
                            List<TvShowResponse> tvShowsResponse = response.body().getTvShows();
                            List<TvShow> tvShows = transformTvShowsResponseToTvShowEntities(tvShowsResponse);
                            new InsertTvShowsAsyncTask(tvShowDao).execute(tvShows);
                        }
                        else {
                            tvShows.setState(Resource.State.FAILED);
                            tvShows.setMessage("Failed to load data");
                        }
                    }

                    @Override
                    public void onFailure(Call<TvShowListResponse> call, Throwable t) {
                        tvShows.setState(Resource.State.FAILED);
                        tvShows.setMessage("Failed to load data");
                    }
                });

    }

    public void fetchTvShow(final long tvShowId){
        tvShows.setState(Resource.State.LOADING);
        tvShows.setMessage("Loading...");
        movieApi.getTvShow(tvShowId, Util.getApiKey(), Util.getCurrentLanguage())
                .enqueue(new Callback<TvShowResponse>() {
                    @Override
                    public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                        if(response.body() != null){
                            tvShows.setState(Resource.State.SUCCESS);
                            tvShows.setMessage("Success");
                            refreshedTvShowIds.add(tvShowId);
                            TvShowResponse tvShowResponse = response.body();
                            TvShow tvShow = transformTvShowResponseToTvShowEntity(tvShowResponse);
                            new InsertTvShowAsyncTask(tvShowDao).execute(tvShow);
                        } else {
                            tvShows.setState(Resource.State.FAILED);
                            tvShows.setMessage("Failed to load data");
                        }
                    }

                    @Override
                    public void onFailure(Call<TvShowResponse> call, Throwable t) {
                        tvShows.setState(Resource.State.FAILED);
                        tvShows.setMessage("Failed to load data");
                    }
                });
    }

    private List<TvShow> transformTvShowsResponseToTvShowEntities(List<TvShowResponse> tvShowsResponse){
        List<TvShow> tvShows = new ArrayList<>();
        for(TvShowResponse tvShowResponse: tvShowsResponse){
            tvShows.add(transformTvShowResponseToTvShowEntity(tvShowResponse));
        }
        return tvShows;
    }

    private TvShow transformTvShowResponseToTvShowEntity(TvShowResponse tvShowResponse){
        TvShow tvShow = new TvShow();
        tvShow.setId(tvShowResponse.getId());
        tvShow.setTitle(tvShowResponse.getTitle());
        tvShow.setDescription(tvShowResponse.getDescription());
        tvShow.setFirstAirDate(tvShowResponse.getFirstAirDate());
        tvShow.setVote(tvShowResponse.getVote());
        tvShow.setImageUrl(tvShowResponse.getImageUrl());
        tvShow.setCoverUrl(tvShowResponse.getCoverUrl());
        return tvShow;
    }

    private static class InsertTvShowsAsyncTask extends AsyncTask<List<TvShow>, Void, Void> {

        private TvShowDao tvShowDao;

        private InsertTvShowsAsyncTask(TvShowDao tvShowDao){
            this.tvShowDao = tvShowDao;
        }

        @Override
        protected Void doInBackground (List<TvShow>... tvShows) {
            tvShowDao.addTvShows(tvShows[0]);
            return null;
        }
    }

    private static class InsertTvShowAsyncTask extends AsyncTask<TvShow, Void, Void> {

        private TvShowDao tvShowDao;

        private InsertTvShowAsyncTask(TvShowDao tvShowDao){
            this.tvShowDao = tvShowDao;
        }

        @Override
        protected Void doInBackground (TvShow... tvShows) {
            tvShowDao.addTvShow(tvShows[0]);
            return null;
        }
    }

    private static class AddTvShowToFavouriteAsyncTask extends AsyncTask<Long, Void, Void>{

        private TvShowDao tvShowDao;

        private AddTvShowToFavouriteAsyncTask(TvShowDao tvShowDao){
            this.tvShowDao = tvShowDao;
        }

        @Override
        protected Void doInBackground(Long... tvShowIds) {
            long tvShowId = tvShowIds[0];
            tvShowDao.addTvShowToFavourite(tvShowId);
            return null;
        }
    }

    private static class RemoveTvShowFromFavouriteAsyncTask extends AsyncTask<Long, Void, Void>{

        private TvShowDao tvShowDao;

        private RemoveTvShowFromFavouriteAsyncTask(TvShowDao tvShowDao){
            this.tvShowDao = tvShowDao;
        }

        @Override
        protected Void doInBackground(Long... tvShowIds) {
            long tvShowId = tvShowIds[0];
            tvShowDao.removeTvShowFromFavourite(tvShowId);
            return null;
        }
    }

}
