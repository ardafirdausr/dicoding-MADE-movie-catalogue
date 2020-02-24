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
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Resource;
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
    private Resource<List<Movie>> movies;
    private boolean isListRefreshed;
    private List<Long> refreshedMovieIds;

    private MovieRepository(Application application){
        movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
        movieDao = MovieCatalogueDatabase.getInstance(application).getMovieDao();
        movies = new Resource<>();
        isListRefreshed = false;
        refreshedMovieIds = new ArrayList<>();
    }

    public static synchronized MovieRepository getInstance(Application application){
        if(movieRepositoryInstance == null){
            movieRepositoryInstance = new MovieRepository(application);
        }
        return movieRepositoryInstance;
    }

    public LiveData<List<Movie>> getMovies(){
        if(!isListRefreshed) fetchNowPlayingMovies();
        movies.setData(movieDao.getMovies());
        return movies.getData();
    }

    public LiveData<Resource.State> getMoviesFetchState(){
        return movies.getState();
    }

    public LiveData<String> getMoviesFetchStatusMessage(){
        return movies.getMessage();
    }

    public LiveData<List<Movie>> getFavouriteMovies(){
        return movieDao.getFavouriteMovies();
    }

    public LiveData<Movie> getMovie(long movieId){
        if(!refreshedMovieIds.contains(movieId)){ fetchMovie(movieId); }
        return movieDao.getMovie(movieId);
    }

    public void addMovieToFavourite(long movieId){
        new AddMovieToFavouriteAsyncTask(movieDao).execute(movieId);
    }

    public void removeMovieFromFavourite(long movieId){
        new RemoveMovieFromFavouriteAsyncTask(movieDao).execute(movieId);
    }

    public void fetchMovie(final long movieId){
//        movies.setState(Resource.State.LOADING);
//        movies.setMessage("Loading...");
        movieApi.getMovie(movieId, Util.getApiKey(), Util.getCurrentLanguage())
                .enqueue(new Callback<MovieResponse>() {

                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.body() != null) {
//                            movies.setState(Resource.State.SUCCESS);
//                            movies.setMessage("Success");

                            refreshedMovieIds.add(movieId);
                            MovieResponse movieResponse = response.body();
                            Movie movie = transformMovieResponseToMovieEntity(movieResponse);
                            Movie presistedMovie = movieDao.getMovie(movieId).getValue();
                            if(presistedMovie != null){
                                movie.setFavourite(presistedMovie.getIsFavourite());
                                new UpdateMovieAsyncTask(movieDao).execute(movie);
                            }
                            new InsertMovieAsyncTask(movieDao).execute(movie);
                        }
                        else {
//                            movies.setState(Resource.State.FAILED);
//                            movies.setMessage("Failed to load data");
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
//                        movies.setState(Resource.State.FAILED);
//                        movies.setMessage("Failed to load data");
                    }
                });
    }

    public void fetchNowPlayingMovies(){
        movies.setState(Resource.State.LOADING);
        movies.setMessage("Loading...");
        movieApi.getNowPlayingMovies(Util.getApiKey(), Util.getCurrentLanguage(), 1)
            .enqueue(new Callback<MovieListResponse>() {

                @EverythingIsNonNull
                @Override
                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                    if (response.body() != null) {
                        isListRefreshed = true;
                        movies.setState(Resource.State.SUCCESS);
                        movies.setMessage("Success");

                        List<MovieResponse> moviesResponse = response.body().getMovies();
                        List<Movie> movies = transformMoviesResponseToMovieEntities(moviesResponse);
                        new InsertMoviesAsyncTask(movieDao).execute(movies);
                    }
                    else {
                        movies.setState(Resource.State.FAILED);
                        movies.setMessage("Failed to load data");
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<MovieListResponse> call, Throwable t) {
                    movies.setState(Resource.State.FAILED);
                    movies.setMessage("Failed to load data");
                }
            });
    }

    public void searchMovie(String movieTitle){
        movies.setState(Resource.State.LOADING);
        movies.setMessage("Loading...");
        movieApi.searchMovie(Util.getApiKey(), Util.getCurrentLanguage(), movieTitle)
                .enqueue(new Callback<MovieListResponse>() {

                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                        if (response.body() != null) {
                            movies.setState(Resource.State.SUCCESS);
                            movies.setMessage("Success");

                            List<MovieResponse> moviesResponse = response.body().getMovies();
                            List<Movie> movies = transformMoviesResponseToMovieEntities(moviesResponse);
                            new InsertMoviesAsyncTask(movieDao).execute(movies);
                        }
                        else {
                            movies.setState(Resource.State.SUCCESS);
                            movies.setMessage("");
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<MovieListResponse> call, Throwable t) {
                        movies.setState(Resource.State.SUCCESS);
                        movies.setMessage("");
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

    private static class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void>{

        private MovieDao movieDao;

        private InsertMovieAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground (Movie... movies) {
            movieDao.addMovie(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void>{

        private MovieDao movieDao;

        private UpdateMovieAsyncTask (MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            Movie movie = movies[0];
            movieDao.updateMovie(movie);
            return null;
        }
    }

    private static class AddMovieToFavouriteAsyncTask extends AsyncTask<Long, Void, Void>{

        private MovieDao movieDao;

        private AddMovieToFavouriteAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Long... movieIds) {
            long movieId = movieIds[0];
            movieDao.addMovieToFavourite(movieId);
            return null;
        }
    }

    private static class RemoveMovieFromFavouriteAsyncTask extends AsyncTask<Long, Void, Void>{

        private MovieDao movieDao;

        private RemoveMovieFromFavouriteAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Long... movieIds) {
            long movieId = movieIds[0];
            movieDao.removeMovieFromFavourite(movieId);
            return null;
        }
    }

}
