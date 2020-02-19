package com.ardafirdausr.movie_catalogue.ui.fragment.favourite;

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

public class FavouriteTvShowsViewModel extends AndroidViewModel {

    private TvShowRepository tvShowRepository;
    private LiveData<List<TvShow>> tvShows;

    private FavouriteTvShowsViewModel(@NonNull Application application) {
        super(application);
        tvShows = new MutableLiveData<>();
        tvShowRepository = TvShowRepository.getInstance(application);
    }

    public LiveData<List<TvShow>> getFavouriteTvShows() {
        tvShows = tvShowRepository.getFavouriteTvShows();
        return tvShows;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new FavouriteTvShowsViewModel(application);
        }
    }
}
