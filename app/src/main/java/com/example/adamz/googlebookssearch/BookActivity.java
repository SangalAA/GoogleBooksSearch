package com.example.adamz.googlebookssearch;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    private String searchQuery;
    private ArrayList<Book> books = new ArrayList<>();
    private BookAdapter mAdapter;
    private boolean isConnected = true;
    private LinearLayout mProgressBarWrapper;
    private Button searchButton;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(this);

        mProgressBarWrapper = (LinearLayout) findViewById(R.id.progress_bar_wrapper);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        ListView bookListView = (ListView) findViewById(R.id.book_list_view);

        bookListView.setAdapter(mAdapter);

        emptyStateTextView = (TextView) findViewById(R.id.empty_state_text_view);
        bookListView.setEmptyView(emptyStateTextView);

        if (savedInstanceState != null) {
            this.books = savedInstanceState.getParcelableArrayList("books");
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }
            EditText searchEditText = (EditText) findViewById(R.id.search_query);
            searchEditText.setText(savedInstanceState.getString("searchQuery"));
            hideKeyboard(this);
        }

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = mAdapter.getItem(position);
                String url = book.getUrl();
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchQuery", searchQuery);
        outState.putParcelableArrayList("books", books);
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        getSearchQuery();
    }

    private void getSearchQuery() {
        EditText searchEditText = (EditText) findViewById(R.id.search_query);

        searchQuery = searchEditText.getText().toString();

        if (isConnected) {
            LoaderManager mLoaderManager = getLoaderManager();
            if (mLoaderManager.getLoader(1) == null) {
                mLoaderManager.initLoader(BOOK_LOADER_ID, null, this);
            } else {
                mLoaderManager.restartLoader(BOOK_LOADER_ID, null, this);
            }

        } else {
            mAdapter.clear();
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        hideKeyboard(this);
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), 0);
        }
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        mProgressBarWrapper.setVisibility(View.VISIBLE);
        return new BookLoader(this, searchQuery);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        mAdapter.clear();

        mProgressBarWrapper.setVisibility(View.GONE);

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            this.books = books;
        } else {
            emptyStateTextView.setText(R.string.no_books_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();
    }
}
