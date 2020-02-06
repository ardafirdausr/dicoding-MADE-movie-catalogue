package com.ardafirdausr.movie_catalogue.fragment.movie;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ardafirdausr.movie_catalogue.BuildConfig;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiClient;
import com.ardafirdausr.movie_catalogue.api.movie.MovieApiInterface;
import com.ardafirdausr.movie_catalogue.api.movie.response.Movie;
import com.ardafirdausr.movie_catalogue.api.movie.response.MovieList;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> movies;
    private MutableLiveData<Boolean> isFetchingData;
    private MutableLiveData<Boolean> isFetchingSuccess;
    private MutableLiveData<String> message;

    public MoviesViewModel(){
        movies = new MutableLiveData<>(new ArrayList<Movie>());
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
        if(movies.getValue().isEmpty()){
            // change this string to resource string
            message.postValue("Loading...");
            isFetchingData.postValue(true);

            String apiKey = BuildConfig.MOVIE_DB_API_KEY;
            String language = getCurrentLanguage();
            int page = 1;

            MovieApiInterface movieApi = MovieApiClient.getClient().create(MovieApiInterface.class);
            final Call<MovieList> movieListRequest = movieApi.getNowPlayingMovies(apiKey, language, page);
            movieListRequest.enqueue(new Callback<MovieList>() {

                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    isFetchingData.postValue(false);
                    if (response.body() != null) {
                        isFetchingSuccess.postValue(true);
                        movies.postValue((ArrayList) response.body().getMovies());
                        message.postValue("");
                    } else {
                        isFetchingSuccess.postValue(false);
                        // change this string to resource string
                        message.postValue("Movies are not available");
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    isFetchingData.postValue(false);
                    isFetchingSuccess.postValue(false);
                    // change this string to resource string
                    message.postValue("Failed load data");
                }

            });
        }
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movies;
    }

    public void setMovies(MutableLiveData<ArrayList<Movie>> movies) {
        this.movies = movies;
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
