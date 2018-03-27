package com.example.adamz.googlebookssearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements View.OnClickListener {

    private String searchQuery;
    private List<Book> books = new ArrayList<>();
    private BookAdapter mAdapter;

    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(this);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        ListView bookListView = (ListView) findViewById(R.id.book_list_view);

        bookListView.setAdapter(mAdapter);

    }

    private class BookAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> result = QueryUtils.fetchBookData(searchQuery, 30);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            mAdapter.clear();

            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }
        }
    }


    private void getSearchQuery() {
        EditText searchEditText = (EditText) findViewById(R.id.search_query);

        searchQuery = searchEditText.getText().toString();

        BookAsyncTask task = new BookAsyncTask();
        task.execute();

    }

    @Override
    public void onClick(View v) {
        getSearchQuery();
    }
}
