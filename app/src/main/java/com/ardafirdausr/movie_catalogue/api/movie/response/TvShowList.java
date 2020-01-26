package com.ardafirdausr.movie_catalogue.api.movie.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowList {

    @SerializedName("page")
    int page;

    @SerializedName("total_pages")
    String totalPages;

    @SerializedName("results")
    List<TvShow> tvShow;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public List<TvShow> getTvShows() {
        return tvShow;
    }

    public void setTvShows(List<TvShow> tvShow) {
        this.tvShow = tvShow;
    }

}
