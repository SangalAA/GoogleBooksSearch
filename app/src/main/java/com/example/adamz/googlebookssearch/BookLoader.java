package com.example.adamz.googlebookssearch;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private String mSearchQuery;

    public BookLoader(Context context, String searchQuery) {
        super(context);
        mSearchQuery = searchQuery;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        ArrayList<Book> result = QueryUtils.fetchBookData(mSearchQuery, 30);
        return result;
    }
}
