package com.example.adamz.googlebookssearch;

/**
 * Created by adamz on 26.3.2018.
 */

public class Book {
    private String mTitle;
    private String[] mAuthors;
    private String mPublisher;
    private String mPublishedDate;
    private String mUrl;
    private String mCoverImageUrl;
/*
    public Book(String mTitle, String[] mAuthors, String mPublisher, int mPublishedDate) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mPublisher = mPublisher;
        this.mPublishedDate = mPublishedDate;
    }
*/
    public Book(String mTitle, String mPublisher, String mPublishedDate) {
        this.mTitle = mTitle;
        this.mPublisher = mPublisher;
        this.mPublishedDate = mPublishedDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }
}
