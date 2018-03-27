package com.example.adamz.googlebookssearch;

import java.util.List;

/**
 * Created by adamz on 26.3.2018.
 */

public class Book {
    private String mTitle;
    private List<String> mAuthors;
    private String mPublisher;
    private String mPublishedDate;
    private String mThumbnailUrl;
    private String mUrl;

    public Book(String mTitle, List<String> mAuthors, String mPublisher, String mPublishedDate, String mThumbnailUrl, String mUrl) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mPublisher = mPublisher;
        this.mPublishedDate = mPublishedDate;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }

    public String getPublisher() {
        return mPublisher;
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
}
