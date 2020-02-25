package com.ardafirdausr.favourite.entity;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Movie implements Parcelable {

    private long id;
    private String title;
    private String description;
    private double vote;
    private String releaseDate;
    private String imageUrl;
    private String coverUrl;
    private boolean isFavourite;

    public Movie(){}

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        vote = in.readDouble();
        releaseDate = in.readString();
        imageUrl = in.readString();
        coverUrl = in.readString();
        isFavourite = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeByte((byte) (isFavourite ? 1 : 0));
    }

    //TODO: seperate column name to variable
    @NonNull
    public static ContentValues toContentValues(@Nullable Movie movie) {
        final ContentValues contentValues = new ContentValues();
        if (movie != null) {
            contentValues.put("id", movie.getId());
            contentValues.put("title", movie.getTitle());
            contentValues.put("description", movie.getDescription());
            contentValues.put("vote", movie.getVote());
            contentValues.put("releaseDate", movie.getReleaseDate());
            contentValues.put("imageUrl", movie.getImageUrl());
            contentValues.put("coverUrl", movie.getCoverUrl());
            contentValues.put("isFavourite", movie.getIsFavourite());
        }
        return contentValues;
    }
}
