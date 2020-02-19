package com.ardafirdausr.movie_catalogue.repository.remote.movie.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListResponse {

    @SerializedName("results")
    private List<MovieResponse> movies;

    public List<MovieResponse> getMovies() {
        return this.movies;
    }


}
