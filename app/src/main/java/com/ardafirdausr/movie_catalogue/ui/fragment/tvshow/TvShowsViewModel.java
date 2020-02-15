package com.ardafirdausr.movie_catalogue.ui.fragment.tvshow;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ardafirdausr.movie_catalogue.repository.TvShowRepository;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;

import java.util.List;

public class TvShowsViewModel extends AndroidViewModel {

    private TvShowRepository tvShowRepository;
    private LiveData<List<TvShow>> tvShows;
    private MutableLiveData<FetchingStatus> fetchingDataStatus;
    private MutableLiveData<String> message;

    private TvShowsViewModel(@NonNull Application application) {
        super(application);
        tvShows = new MutableLiveData<>();
        fetchingDataStatus = new MutableLiveData<>(FetchingStatus.LOADING);
        message = new MutableLiveData<>("Loading...");
        tvShowRepository = TvShowRepository.getInstance(application);
    }

    public void fetchingTvShows(){
        fetchingDataStatus.setValue(FetchingStatus.LOADING);
        message.setValue("Loading...");
        tvShowRepository.fetchTvOnTheAir(new TvShowRepository.OnFetchCallback(){
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

    public LiveData<List<TvShow>> getTvShows() {
        tvShows = tvShowRepository.getTvShows();
        return tvShows;
    }

    public LiveData<FetchingStatus> getFetchingDataStatus() { return fetchingDataStatus; }

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

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new TvShowsViewModel(application);
        }
    }

}
