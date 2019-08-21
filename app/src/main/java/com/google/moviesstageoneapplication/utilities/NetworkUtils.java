package com.google.moviesstageoneapplication.utilities;

import android.net.Uri;

import com.google.moviesstageoneapplication.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie";

    final static String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;


    public static URL buildUrl(String query,boolean isImg) {

        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342/"+query;

        URL url = null;

        if(isImg) {
            Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                    .build();
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else {

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendPath(query)
                    .appendQueryParameter("api_key", API_KEY)
                    .build();
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }


        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
