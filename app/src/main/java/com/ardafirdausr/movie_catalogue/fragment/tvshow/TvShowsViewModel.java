package com.ardafirdausr.movie_catalogue.fragment.tvshow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ardafirdausr.movie_catalogue.BuildConfig;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.api.movie.response.TvShow;
import com.ardafirdausr.movie_catalogue.api.movie.response.TvShowList;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TvShow>> tvShows;
    private MutableLiveData<Boolean> isFetchingData;
    private MutableLiveData<Boolean> isFetchingSuccess;
    private MutableLiveData<String> message;

    public TvShowsViewModel(){
        tvShows = new MutableLiveData<>(new ArrayList<TvShow>());
        isFetchingData = new MutableLiveData<>(false);
        isFetchingSuccess = new MutableLiveData<>(false);
        message = new MutableLiveData<>("");
    }

    private String getCurrentLanguage(){
        String countryCode = Locale.getDefault().getCountry();
        String languageCode = Locale.getDefault().getLanguage();
        languageCode = languageCode.equals("in") ? "id" : languageCode;
        return languageCode + "-" + countryCode;
    }

    public void initFetchMovies(){
        if(tvShows.getValue().isEmpty()){
            message.setValue("Loading...");
            isFetchingData.postValue(true);

            String apiKey = BuildConfig.MOVIE_DB_API_KEY;
            String language = getCurrentLanguage();
            int page = 1;

            MovieApiInterface movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
            final Call<TvShowList> tvShowListRequest = movieApi.getTvOnTheAir(apiKey, language, page);
            tvShowListRequest.enqueue(new Callback<TvShowList>() {

                @Override
                public void onResponse(Call<TvShowList> call, Response<TvShowList> response) {
                    isFetchingData.postValue(false);
                    if (response.body() != null) {
                        isFetchingSuccess.postValue(true);
                        getTvShows().postValue((ArrayList<TvShow>) response.body().getTvShows());
                        message.setValue("");
                    } else {
                        isFetchingSuccess.postValue(false);
                        message.setValue("Tv shows are not available");
                    }
                }

                @Override
                public void onFailure(Call<TvShowList> call, Throwable t) {
                    isFetchingData.postValue(false);
                    isFetchingSuccess.postValue(false);
                    message.setValue("Failed load data");
                }

            });
        }
    }

    public MutableLiveData<ArrayList<TvShow>> getTvShows() {
        return tvShows;
    }

    public void setTvShows(MutableLiveData<ArrayList<TvShow>> tvShows) {
        this.tvShows = tvShows;
    }

    public MutableLiveData<Boolean> getIsFetchingData() {
        return isFetchingData;
    }

    public void setIsFetchingData(MutableLiveData<Boolean> isFetchingData) {
        this.isFetchingData = isFetchingData;
    }

    public MutableLiveData<Boolean> getIsFetchingSuccess() {
        return isFetchingSuccess;
    }

    public void setIsFetchingSuccess(MutableLiveData<Boolean> isFetchingSuccess) {
        this.isFetchingSuccess = isFetchingSuccess;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void setMessage(MutableLiveData<String> message) {
        this.message = message;
    }
}
