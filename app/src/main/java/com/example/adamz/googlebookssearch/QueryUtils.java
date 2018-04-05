package com.example.adamz.googlebookssearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by adamz on 26.3.2018.
 */

public class QueryUtils {

    private static final String URL_PATH = "https://www.googleapis.com/books/v1/";
    private static final String URL_QUERY = "volumes?q=";
    private static final String MAX_RESULTS_QUERY = "&maxResults=";

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    private static ArrayList<Book> extractBooks(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);

            JSONArray itemsArray = root.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);

                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                ArrayList<String> authors = new ArrayList<>();
                if (volumeInfo.has("authors")) {
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authors.add(authorsArray.getString(j));
                    }
                }

                String publishedDate = volumeInfo.getString("publishedDate");
                String url = volumeInfo.getString("canonicalVolumeLink");

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnailUrl = imageLinks.getString("smallThumbnail"); //osetrit

                books.add(new Book(title, authors, publishedDate, thumbnailUrl, url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException", e);
        }

        return books;
    }

    private static URL createUrl(String query, int maxResults) {
        URL url = null;

        if (query == null) {
            return url;
        }

        if (maxResults < 1) {
            maxResults = 10;
        }

        String urlString = URL_PATH + URL_QUERY + query + MAX_RESULTS_QUERY + maxResults;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error response code" + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem reading from stream ", e);
        }
        return output.toString();
    }

    public static ArrayList<Book> fetchBookData(String query, int maxResults) {
        URL url = createUrl(query, maxResults);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while making http request", e);
        }

        ArrayList<Book> books = extractBooks(jsonResponse);

        return books;
    }

}
