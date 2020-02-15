package com.ardafirdausr.movie_catalogue.repository.remote.movie.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowListResponse {

    @SerializedName("results")
    private List<TvShowResponse> tvShows;

    public List<TvShowResponse> getTvShows() {
        return this.tvShows;
    }

}
