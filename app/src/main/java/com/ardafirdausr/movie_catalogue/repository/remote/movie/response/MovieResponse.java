package com.ardafirdausr.movie_catalogue.repository.remote.movie.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieResponse implements Parcelable {

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

    @SerializedName("backdrop_path")
    private String coverUrl;

    private MovieResponse(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        vote = in.readDouble();
        releaseDate = in.readString();
        imageUrl = in.readString();
        coverUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(vote);
        dest.writeString(releaseDate);
        dest.writeString(imageUrl);
        dest.writeString(coverUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {
        @Override
        public MovieResponse createFromParcel(Parcel in) {
            return new MovieResponse(in);
        }

        @Override
        public MovieResponse[] newArray(int size) {
            return new MovieResponse[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getVote() {
        return vote;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + imageUrl;
    }

    public String getCoverUrl() {
        return "https://image.tmdb.org/t/p/w500" + coverUrl;
    }

}
