package com.example.adamz.googlebookssearch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by adamz on 26.3.2018.
 */

public class Book implements Parcelable {
    private String mTitle;
    private ArrayList<String> mAuthors;
    private String mPublishedDate;
    private String mThumbnailUrl;
    private String mUrl;

    public Book(String mTitle, ArrayList<String> mAuthors, String mPublishedDate, String mThumbnailUrl, String mUrl) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mPublishedDate = mPublishedDate;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    protected Book(Parcel in) {
        mTitle = in.readString();
        if (in.readByte() == 0x01) {
            mAuthors = new ArrayList<String>();
            in.readList(mAuthors, String.class.getClassLoader());
        } else {
            mAuthors = null;
        }
        mPublishedDate = in.readString();
        mThumbnailUrl = in.readString();
        mUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        if (mAuthors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mAuthors);
        }
        dest.writeString(mPublishedDate);
        dest.writeString(mThumbnailUrl);
        dest.writeString(mUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
