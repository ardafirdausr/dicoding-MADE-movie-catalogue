package com.ardafirdausr.movie_catalogue.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ardafirdausr.movie_catalogue.repository.local.MovieCatalogueDatabase;
import com.ardafirdausr.movie_catalogue.repository.local.dao.TvShowDao;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiInterface;
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

    private TvShowRepository(Application application){
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        tvShowDao = MovieCatalogueDatabase.getInstance(application).getTvShowDao();
    }

    public static synchronized TvShowRepository getInstance(Application application){
        if(tvShowRepositoryInstance == null){
            tvShowRepositoryInstance = new TvShowRepository(application);
        }
        return tvShowRepositoryInstance;
    }

    public LiveData<List<TvShow>> getTvShows(){
        return tvShowDao.getTvShows();
    }

    public LiveData<List<TvShow>> getFavouriteTvShows(){
        return tvShowDao.getFavouriteTvShows();
    }

    public LiveData<TvShow> getTvShow(long tvShowId){ return tvShowDao.getTvShow(tvShowId); }

    public void addTvShowToFavourite(long tvShowId){
        new AddTvShowToFavouriteAsyncTask(tvShowDao).execute(tvShowId);
    }

    public void removeTvShowFromFavourite(long tvShowId){
        new RemoveTvShowFromFavouriteAsyncTask(tvShowDao).execute(tvShowId);
    }


    public void fetchTvOnTheAir(final OnFetchCallback onFetchCallback){
        movieApi.getTvOnTheAir(Util.getApiKey(), Util.getCurrentLanguage(), 1)
                .enqueue(new Callback<TvShowListResponse>() {

                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<TvShowListResponse> call, Response<TvShowListResponse> response) {
                        if(response.body() != null){
                            onFetchCallback.onSuccess();
                            List<TvShowResponse> tvShowsResponse = response.body().getTvShows();
                            List<TvShow> tvShows = transformTvShowsResponseToTvShowEntities(tvShowsResponse);
                            new InsertMoviesAsyncTask(tvShowDao).execute(tvShows);
                        }
                        else {
                            onFetchCallback.onFailed("Data not available");
                        }
                    }

                    @Override
                    public void onFailure(Call<TvShowListResponse> call, Throwable t) {
                        onFetchCallback.onFailed("Failed to load data");
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

    private static class InsertMoviesAsyncTask extends AsyncTask<List<TvShow>, Void, Void> {

        private TvShowDao tvShowDao;

        private InsertMoviesAsyncTask(TvShowDao tvShowDao){
            this.tvShowDao = tvShowDao;
        }

        @Override
        protected Void doInBackground (List<TvShow>... movies) {
            tvShowDao.addTvShows(movies[0]);
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

    public interface OnFetchCallback {
        void onSuccess();
        void onFailed(String errorMessage);
    }
}
