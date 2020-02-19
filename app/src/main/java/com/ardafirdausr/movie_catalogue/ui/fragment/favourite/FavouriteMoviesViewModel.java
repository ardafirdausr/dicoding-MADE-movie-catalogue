package com.ardafirdausr.movie_catalogue.ui.fragment.favourite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ardafirdausr.movie_catalogue.repository.MovieRepository;
import com.ardafirdausr.movie_catalogue.repository.local.entity.Movie;

import java.util.List;

public class FavouriteMoviesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<Movie>> movies;

    private FavouriteMoviesViewModel(@NonNull Application application) {
        super(application);
        movies = new MutableLiveData<>();
        movieRepository = MovieRepository.getInstance(application);
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        movies = movieRepository.getFavouriteMovies();
        return movies;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FavouriteMoviesViewModel(application);
        }
    }
}
