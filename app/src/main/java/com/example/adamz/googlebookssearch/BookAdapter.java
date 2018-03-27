package com.example.adamz.googlebookssearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adamz on 26.3.2018.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View bookListView = convertView;

        if (bookListView == null) {
            bookListView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView publisherTextView = (TextView) bookListView.findViewById(R.id.publisher_text_view);
        String publisher = currentBook.getPublisher();

        if (publisher.equals("")) {
            publisher = getContext().getResources().getString(R.string.no_publisher);
        }
        publisherTextView.setText(publisher);

        TextView titleTextView = (TextView) bookListView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentBook.getTitle());

        TextView authorsTextView = (TextView) bookListView.findViewById(R.id.authors_text_view);

        List<String> authors = currentBook.getAuthors();
        if (authors.isEmpty()) {
            authorsTextView.setText(R.string.no_authors);
        } else {
            authorsTextView.setText(formatAuthors(authors));
        }


        TextView dateTextView = (TextView) bookListView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentBook.getPublishedDate());


        return bookListView;
    }

    private static String formatAuthors(List<String> authors) {
        StringBuilder output = new StringBuilder();
        if (authors.isEmpty()) {
            return output.toString();
        }
        for (String author : authors) {
            output.append(author);
            output.append(",");
        }
        return output.toString();
    }
}
