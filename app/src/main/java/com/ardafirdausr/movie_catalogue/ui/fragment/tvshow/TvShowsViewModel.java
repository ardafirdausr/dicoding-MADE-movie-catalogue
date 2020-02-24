package com.ardafirdausr.movie_catalogue.ui.fragment.tvshow;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ardafirdausr.movie_catalogue.repository.TvShowRepository;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.Resource;

import java.util.List;

public class TvShowsViewModel extends AndroidViewModel {

    private TvShowRepository tvShowRepository;

    private TvShowsViewModel(@NonNull Application application) {
        super(application);
        tvShowRepository = TvShowRepository.getInstance(application);
    }

    public void fetchingTvShows(){
        tvShowRepository.fetchTvOnTheAir();
    }

    public LiveData<List<TvShow>> getTvShows() {
        return tvShowRepository.getTvShows();
    }

    public void searchTvShow(String tvShowTitle){
        tvShowRepository.searchTvShow(tvShowTitle);
    }

    public LiveData<Resource.State> getFetchingDataStatus() { return tvShowRepository.getTvShowsFetchState(); }

    public LiveData<String> getMessage() {
        return tvShowRepository.getTvShowsFetchStatusMessage();
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
