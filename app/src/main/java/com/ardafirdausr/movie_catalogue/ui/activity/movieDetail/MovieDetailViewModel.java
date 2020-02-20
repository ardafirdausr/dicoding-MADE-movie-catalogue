package com.ardafirdausr.movie_catalogue.ui.activity.movieDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ardafirdausr.movie_catalogue.repository.MovieRepository;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;

public class MovieDetailViewModel extends AndroidViewModel {

    private LiveData<Movie> movie;
    private MovieRepository movieRepository;

    private MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movie = new MutableLiveData<Movie>();
        movieRepository = MovieRepository.getInstance(application);
    }

    public LiveData<Movie> getMovie(long movieId){
        movie = movieRepository.getMovie(movieId);
        return movie;
    }

    public void addMovieToFavourite(long movieId){
        movieRepository.addMovieToFavourite(movieId);
    }

    public void removeMovieFromFavourite(long movieId){
        movieRepository.removeMovieFromFavourite(movieId);
    }

    public static class Factory implements ViewModelProvider.Factory {

        private Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MovieDetailViewModel(application);
        }
    }
}
