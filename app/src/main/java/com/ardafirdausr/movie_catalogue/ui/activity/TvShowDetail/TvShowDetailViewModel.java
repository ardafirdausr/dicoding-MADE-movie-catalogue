package com.ardafirdausr.movie_catalogue.ui.activity.TvShowDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ardafirdausr.movie_catalogue.repository.TvShowRepository;
import com.ardafirdausr.movie_catalogue.repository.local.entity.TvShow;

public class TvShowDetailViewModel extends AndroidViewModel {

    private LiveData<TvShow> tvShow;
    private TvShowRepository tvShowRepository;

    private TvShowDetailViewModel(@NonNull Application application) {
        super(application);
        tvShow = new MutableLiveData<TvShow>();
        tvShowRepository = TvShowRepository.getInstance(application);
    }

    public LiveData<TvShow> getTvShow(long tvShowId){
        tvShow = tvShowRepository.getTvShow(tvShowId);
        return tvShow;
    }

    public void addTvShowToFavourite(long tvShowId){
        tvShowRepository.addTvShowToFavourite(tvShowId);
    }

    public void removeTvShowFromFavourite(long tvShowId){
        tvShowRepository.removeTvShowFromFavourite(tvShowId);
    }

    public static class Factory implements ViewModelProvider.Factory {

        private Application application;

        public Factory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TvShowDetailViewModel(application);
        }
    }
}
