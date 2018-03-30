package com.example.adamz.googlebookssearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adamz on 26.3.2018.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getName();

    public BookAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
    }

    private static String formatAuthors(List<String> authors) {
        StringBuilder output = new StringBuilder();
        if (authors.isEmpty()) {
            return output.toString();
        }
        for (int i = 0; i < authors.size(); i++) {
            if (i == authors.size() - 1) {
                output.append(authors.get(i));
            } else {
                output.append(authors.get(i));
                output.append(", ");
            }
        }
        return output.toString();
    }

    private static String formatPublishedDate(String publishedDate) {
        if (publishedDate.length() <= 4) {
            return publishedDate;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");

        String yearString = "";
        Date date = null;
        try {
            date = dateFormat.parse(publishedDate);
            yearString = year.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date", e);
        } catch (NullPointerException npe) {
            Log.e(LOG_TAG, "SimpleDateFormat tried to format null", npe);
        }

        return yearString;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View bookListView = convertView;

        if (bookListView == null) {
            bookListView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        // ImageView
        ImageView thumbnailImageView = (ImageView) bookListView.findViewById(R.id.book_thumbnail);
        // Using Glide library to lazy load thumbnail images
        Glide.with(getContext()).load(currentBook.getThumbnailUrl()).into(thumbnailImageView);

        TextView titleTextView = (TextView) bookListView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentBook.getTitle());
        TextView authorsTextView = (TextView) bookListView.findViewById(R.id.authors_text_view);

        ArrayList<String> authors = currentBook.getAuthors();
        if (authors.isEmpty()) {
            authorsTextView.setText(R.string.no_authors);
        } else {
            authorsTextView.setText(formatAuthors(authors));
        }

        TextView dateTextView = (TextView) bookListView.findViewById(R.id.date_text_view);
        dateTextView.setText(formatPublishedDate(currentBook.getPublishedDate()));

        return bookListView;
    }

}
