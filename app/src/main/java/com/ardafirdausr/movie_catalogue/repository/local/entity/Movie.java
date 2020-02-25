package com.ardafirdausr.movie_catalogue.repository.local.entity;


import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "movies")
public class Movie implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "vote")
    private double vote;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "coverUrl")
    private String coverUrl;

    @ColumnInfo(name = "isFavourite")
    private boolean isFavourite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    //TODO: seperate column name to variable
    @NonNull
    public static Movie fromContentValues(@Nullable ContentValues values) {
        final Movie movie = new Movie();
        if (values != null) {
            movie.id = values.getAsLong("id");
            movie.title = values.getAsString("title");
            movie.description = values.getAsString("description");
            movie.vote = values.getAsDouble("vote");
            movie.releaseDate = values.getAsString("releaseDate");
            movie.imageUrl = values.getAsString("imageUrl");
            movie.coverUrl = values.getAsString("coverUrl");
            movie.isFavourite = values.getAsBoolean("isFavourite");
        }
        return movie;
    }
}
