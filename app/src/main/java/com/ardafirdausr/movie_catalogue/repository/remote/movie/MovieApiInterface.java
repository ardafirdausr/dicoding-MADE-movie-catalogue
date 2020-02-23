package com.ardafirdausr.movie_catalogue.repository.remote.movie;

import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.MovieListResponse;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.MovieResponse;
import com.ardafirdausr.movie_catalogue.repository.remote.movie.response.TvShowListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {

    @GET("movie/now_playing")
    Call<MovieListResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

    @GET("discover/movie")
    Call<MovieListResponse> getMovieRelease(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("primary_release_date.gte") String primary_release_date_gte,
            @Query("primary_release_date.lte") String primary_release_date_lte);

    @GET("search/movie")
    Call<MovieListResponse> searchMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String movieTitle);

    @GET("movie/{movie_id}")
    Call<MovieResponse> getMovie(
            @Path("movie_id") long movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("tv/on_the_air")
    Call<TvShowListResponse> getTvOnTheAir(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

}
