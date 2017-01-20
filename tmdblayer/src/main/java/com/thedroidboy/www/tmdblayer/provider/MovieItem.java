package com.thedroidboy.www.tmdblayer.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yshah on 1/20/2017.
 */

public class MovieItem implements Parcelable {
    public final int id;
    public final String movieTitle;
    public final String overview;
    public final String moviePoster;
    public final String release;
    public final String rate;

    public MovieItem(int id, String title, String details, String moviePoster, String release, String rate) {
        this.id = id;
        this.movieTitle = title;
        this.overview = details;
        this.moviePoster = moviePoster;
        this.release = release;
        this.rate = rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.movieTitle);
        dest.writeString(this.overview);
        dest.writeString(this.moviePoster);
        dest.writeString(this.release);
        dest.writeString(this.rate);
    }

    protected MovieItem(Parcel in) {
        this.id = in.readInt();
        this.movieTitle = in.readString();
        this.overview = in.readString();
        this.moviePoster = in.readString();
        this.release = in.readString();
        this.rate = in.readString();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
