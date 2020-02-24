package com.ardafirdausr.movie_catalogue.ui.fragment.movie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ardafirdausr.movie_catalogue.repository.MovieRepository;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Resource;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    private MoviesViewModel(@NonNull Application application) {
        super(application);
        movieRepository = MovieRepository.getInstance(application);
    }

    public void fetchMovies(){
        movieRepository.fetchNowPlayingMovies();
    }

    public void searchMovie(String movieTitle){
        movieRepository.searchMovie(movieTitle);
    }

    public LiveData<List<Movie>> getMovies(){
        return movieRepository.getMovies();
    }

    public LiveData<Resource.State> getFetchingDataStatus() {
        return movieRepository.getMoviesFetchState();
    }

    public LiveData<String> getMessage() {
        return movieRepository.getMoviesFetchStatusMessage();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MoviesViewModel(application);
        }
    }

}
