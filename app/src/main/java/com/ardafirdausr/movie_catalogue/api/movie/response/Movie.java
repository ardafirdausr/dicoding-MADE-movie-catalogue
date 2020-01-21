package com.ardafirdausr.movie_catalogue.api.movie.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Parcelable {

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        vote = in.readDouble();
        releaseDate = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(vote);
        dest.writeString(releaseDate);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String description;

    @SerializedName("vote_average")
    private double vote;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String imageUrl;

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
        return "https://image.tmdb.org/t/p/w500" + imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
