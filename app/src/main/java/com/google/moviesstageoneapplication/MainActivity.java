package com.google.moviesstageoneapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.moviesstageoneapplication.model.Movie;
import com.google.moviesstageoneapplication.utilities.MyDividerItemDecoration;
import com.google.moviesstageoneapplication.utilities.NetworkUtils;
import com.google.moviesstageoneapplication.utilities.RecyclerTouchListener;
import com.google.moviesstageoneapplication.view.MoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    public static List<Movie> movieList = new ArrayList<Movie>();
    private MoviesAdapter mAdapter;
    private String jasonMovieText;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean check = checkConnection();

            mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
            mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
            recyclerView = findViewById(R.id.rv_movies);
            mSpinner = findViewById(R.id.spinner);
            mSpinner.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sortType, android.R.layout.simple_spinner_item);

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter1);

            setTitle("popular movies");

        if(check) {

            makeMovieSearchQuery("popular");

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, final int position) {

                    Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.EXTRA_POSITION, position);
                    startActivity(intent);

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }else {
            showErrorMessage();
        }
    }

    private boolean checkConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
       return isConnected;
    }



    private void showRecycleView() {

        mErrorMessageDisplay.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        recyclerView.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    private void getResults(JSONArray results)  {

        if(results.length() != 0) {

            for (int i = 0; i < results.length(); i++) {

                JSONObject resultsReader = null;
                String title = null;
                String image = null;
                String releaseDate = null;
                String voteAverage = null;
                String overview = null;
                resultsReader= results.optJSONObject(i);
                title = resultsReader.optString("title", "fall_back_string");
                image = resultsReader.optString("poster_path", "fall_back_string");
                releaseDate = resultsReader.optString("release_date", "fall_back_string");
                voteAverage = resultsReader.optString("vote_average", "fall_back_string");
                overview = resultsReader.optString("overview", "fall_back_string");

                String imgPath = makeMovieImageSearchQuery(image);

                Movie entry1=new Movie();
                entry1.setTitle(title);
                entry1.setPosterPath(imgPath);
                entry1.setReleaseDate(releaseDate);
                entry1.setVoterAverage(voteAverage);
                entry1.setOverview(overview);
                movieList.add(entry1);

            }
        }

    }

    private void makeMovieSearchQuery(String movieQuery) {

        showRecycleView();

        //this is the url
        URL movieSearchUrl = NetworkUtils.buildUrl(movieQuery,false);
        new MovieQueryTask().execute(movieSearchUrl);
    }
    private String makeMovieImageSearchQuery(String imgId) {

        //this is the url
        URL movieSearchUrl = NetworkUtils.buildUrl(imgId,true);
        return movieSearchUrl.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        movieList.clear();
        setTitle(text+" movies");
        makeMovieSearchQuery(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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

            mLoadingIndicator.setVisibility(View.GONE);
            if (movieSearchResults != null && !movieSearchResults.equals("")) {

                jasonMovieText = movieSearchResults;
                JSONObject reader = null;
                JSONArray results = null;
                try {
                    reader = new JSONObject(jasonMovieText);
                    results  = reader.getJSONArray("results");
                    getResults(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int posterWidth = 500;

                mAdapter = new MoviesAdapter(MainActivity.this, movieList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this.getApplicationContext(), calculateBestSpanCount(posterWidth));
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.addItemDecoration(new MyDividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL, 16));
                recyclerView.setAdapter(mAdapter);
                showRecycleView();

            } else {

                showErrorMessage();
            }
        }
    }
    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }


}
