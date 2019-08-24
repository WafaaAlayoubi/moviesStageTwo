package com.google.moviesstageoneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.moviesstageoneapplication.model.Movie;
import com.google.moviesstageoneapplication.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideosActivity extends AppCompatActivity {

    private static final String EXTRA_POSITION = null ;
    List<String> videosName = new ArrayList<>() ;
    private String jasonVideoText;


    int position ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        setTitle("Trailer");

        Intent intent = getIntent();
        position = intent.getIntExtra(EXTRA_POSITION,-1);

        Movie movieObj = MainActivity.movieList.get(position);

        makeVideoSearchQuery("videos",movieObj.getId());
    }

    private void makeVideoSearchQuery(String movieQuery,String id) {

        URL movieSearchUrl = NetworkUtils.buildUrlVideosAndReviews(movieQuery,id);
        new VideoQueryTask().execute(movieSearchUrl);
    }

    private void showList(final String videoKey){

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, videosName);

        ListView listView = findViewById(R.id.videos_lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                watchYoutubeVideo(VideosActivity.this,videoKey);
            }
        });
    }

    public static void watchYoutubeVideo(Context context, String videoKey){

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoKey));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public class VideoQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieSearchResults;
        }

        @Override
        protected void onPostExecute(String movieSearchResults) {
            String videoKey = null;
            //mLoadingIndicator.setVisibility(View.GONE);
            if (movieSearchResults != null && !movieSearchResults.equals("")) {

                jasonVideoText = movieSearchResults;
                JSONObject reader = null;
                JSONArray results = null;
                try {
                    reader = new JSONObject(jasonVideoText);
                    results  = reader.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject resultsReader = null;
                        String title = null;

                        resultsReader= results.optJSONObject(i);
                        title = resultsReader.optString("name", "fall_back_string");
                        videoKey = resultsReader.optString("key", "fall_back_string");
                        videosName.add(title);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showList(videoKey);

            } else {


            }
        }
    }
}
