package com.ardafirdausr.movie_catalogue.api.movie.response;

import com.google.gson.annotations.SerializedName;

public class TvShow {


    @SerializedName("name")
    private String title;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("id")
    private Long id;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("overview")
    private String description;

    @SerializedName("poster_path")
    private String posterPath;

}
