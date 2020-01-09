package com.ardafirdausr.movie_catalogue.api.movie;

import com.ardafirdausr.movie_catalogue.api.movie.response.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiInterface {

    @GET("movie/now_playing")
    Call<MovieList> getNowPlayingMovies(@Query("api_key") String apiKey, @Query("page") int page);

}
