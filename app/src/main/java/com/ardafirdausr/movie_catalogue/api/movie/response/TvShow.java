package com.ardafirdausr.movie_catalogue.api.movie.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TvShow  implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String title;

    @SerializedName("overview")
    private String description;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("vote_average")
    private Double vote;

    @SerializedName("poster_path")
    private String imageUrl;

    @SerializedName("backdrop_path")
    private String coverUrl;

    protected TvShow(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        title = in.readString();
        description = in.readString();
        firstAirDate = in.readString();
        if (in.readByte() == 0) {
            vote = null;
        } else {
            vote = in.readDouble();
        }
        imageUrl = in.readString();
        coverUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(firstAirDate);
        if (vote == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(vote);
        }
        dest.writeString(imageUrl);
        dest.writeString(coverUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public Double getVote() {
        return vote;
    }

    public void setVote(Double vote) {
        this.vote = vote;
    }

    public String getImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCoverUrl() {
        return "https://image.tmdb.org/t/p/w500" + coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
