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

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<FetchingStatus> fetchingDataStatus;
    private MutableLiveData<String> message;

    private MoviesViewModel(@NonNull Application application) {
        super(application);
        fetchingDataStatus = new MutableLiveData<>(FetchingStatus.SUCCESS);
        message = new MutableLiveData<>();
        movieRepository = MovieRepository.getInstance(application);
    }

    public void fetchMovies(){
        fetchingDataStatus.setValue(FetchingStatus.LOADING);
        message.setValue("Loading...");
        movieRepository.fetchNowPlayingMovies(new MovieRepository.OnFetchCallback(){
            @Override
            public void onSuccess() {
                fetchingDataStatus.setValue(FetchingStatus.SUCCESS);
                message.setValue("Success");
            }

            @Override
            public void onFailed(String errorMessage) {
                fetchingDataStatus.setValue(FetchingStatus.FAILED);
                message.setValue(errorMessage);
            }
        });
    }

    public LiveData<List<Movie>> getMovies(){
        if(movieRepository.getMovieCount() < 1) fetchMovies();
        return movieRepository.getMovies();
    }

    public LiveData<FetchingStatus> getFetchingDataStatus() {
        return fetchingDataStatus;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public enum FetchingStatus {
        LOADING,
        SUCCESS,
        FAILED
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
