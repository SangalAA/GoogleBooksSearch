package com.example.adamz.googlebookssearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements View.OnClickListener {

    private String searchQuery;
    private List<Book> books = new ArrayList<>();
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

    private class BookAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            mProgressBarWrapper.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> result = QueryUtils.fetchBookData(searchQuery, 30);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            mAdapter.clear();

            mProgressBarWrapper.setVisibility(View.GONE);

            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            } else {
                emptyStateTextView.setText(R.string.no_books_found);
            }
        }
    }

    private void getSearchQuery() {
        EditText searchEditText = (EditText) findViewById(R.id.search_query);

        searchQuery = searchEditText.getText().toString();

        if (isConnected) {
            BookAsyncTask task = new BookAsyncTask();
            task.execute();
        } else {
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        hideKeyboard(this);
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        getSearchQuery();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), 0);
        }
    }


}
