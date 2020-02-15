package com.ardafirdausr.movie_catalogue.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.ardafirdausr.movie_catalogue.repository.local.MovieCatalogueDatabase;
import com.ardafirdausr.movie_catalogue.repository.local.dao.MovieDao;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Util;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.MovieListResponse;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MovieRepository {

    private static MovieRepository movieRepositoryInstance;
    private MovieApiInterface movieApi;
    private MovieDao movieDao;

    private MovieRepository(Application application){
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        movieDao = MovieCatalogueDatabase.getInstance(application).getMovieDao();
    }

    public static synchronized MovieRepository getInstance(Application application){
        if(movieRepositoryInstance == null){
            movieRepositoryInstance = new MovieRepository(application);
        }
        return movieRepositoryInstance;
    }

    public LiveData<List<Movie>> getMovies(){
        return movieDao.getMovies();
    }

    public LiveData<Movie> getMovie(long movieId){
        return movieDao.getMovie(movieId);
    }

    public void addToFavourite(long movieId){
        new AddToFavouriteAsyncTask(movieDao).execute(movieId);
    }

    public void removeFromFavourite(long movieId){
        new RemoveFromFavouriteAsyncTask(movieDao).execute(movieId);
    }

    public void fetchNowPlayingMovies(final OnFetchCallback onFetchCallback){
        movieApi.getNowPlayingMovies(Util.getApiKey(), Util.getCurrentLanguage(), 1)
            .enqueue(new Callback<MovieListResponse>() {

                @EverythingIsNonNull
                @Override
                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                    Log.d("WWWWW", response.body().toString());
                    if (response.body() != null) {
                        Log.d("WWWWW", response.body().toString());
                        onFetchCallback.onSuccess();
                        List<MovieResponse> moviesResponse = response.body().getMovies();
                        List<Movie> movies = transformMoviesResponseToMovieEntities(moviesResponse);
                        new InsertMoviesAsyncTask(movieDao).execute(movies);
                    }
                    else {
                        onFetchCallback.onFailed("Data not available");
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<MovieListResponse> call, Throwable t) {
                    onFetchCallback.onFailed("Failed to load data");
                }
            });
    }

    private List<Movie> transformMoviesResponseToMovieEntities(List<MovieResponse> moviesResponse){
        List<Movie> movies = new ArrayList<>();
        for(MovieResponse movieResponse: moviesResponse){
            movies.add(transformMovieResponseToMovieEntity(movieResponse));
        }
        return movies;
    }

    private Movie transformMovieResponseToMovieEntity(MovieResponse movieResponse){
        Movie movie = new Movie();
        movie.setId(movieResponse.getId());
        movie.setTitle(movieResponse.getTitle());
        movie.setDescription(movieResponse.getDescription());
        movie.setReleaseDate(movieResponse.getReleaseDate());
        movie.setVote(movieResponse.getVote());
        movie.setImageUrl(movieResponse.getImageUrl());
        movie.setCoverUrl(movieResponse.getCoverUrl());
        return movie;
    }

    private static class InsertMoviesAsyncTask extends AsyncTask<List<Movie>, Void, Void>{

        private MovieDao movieDao;

        private InsertMoviesAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground (List<Movie>... movies) {
            movieDao.addMovies(movies[0]);
            return null;
        }
    }

    private static class AddToFavouriteAsyncTask extends AsyncTask<Long, Void, Void>{

        private MovieDao movieDao;

        private AddToFavouriteAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Long... movieIds) {
            long movieId = movieIds[0];
            movieDao.addToFavourite(movieId);
            return null;
        }
    }

    private class RemoveFromFavouriteAsyncTask extends AsyncTask<Long, Void, Void>{

        private MovieDao movieDao;

        private RemoveFromFavouriteAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Long... movieIds) {
            long movieId = movieIds[0];
            movieDao.removeFromFavourite(movieId);
            return null;
        }
    }

    public interface OnFetchCallback {
        void onSuccess();
        void onFailed(String errorMessage);
    }

}
