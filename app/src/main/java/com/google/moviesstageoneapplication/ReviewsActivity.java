package com.google.moviesstageoneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.moviesstageoneapplication.model.Movie;
import com.google.moviesstageoneapplication.model.reviewsListItem;
import com.google.moviesstageoneapplication.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    private static final String EXTRA_POSITION = null ;

    final   ArrayList<reviewsListItem> Items=new  ArrayList<reviewsListItem> ();

    int position ;

    private String jasonReviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        setTitle("Reviews");

        Intent intent = getIntent();
        position = intent.getIntExtra(EXTRA_POSITION,-1);

        Movie movieObj = MainActivity.movieList.get(position);

        makeVideoSearchQuery("reviews",movieObj.getId());
    }

    private void showListReviews(){
        final MyCustomAdapter myadpter= new MyCustomAdapter(Items);

        ListView ls=(ListView)findViewById(R.id.listView);
        ls.setAdapter(myadpter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            }
        });
    }

    private void makeVideoSearchQuery(String movieQuery,String id) {

        URL movieSearchUrl = NetworkUtils.buildUrlVideosAndReviews(movieQuery,id);
        new ReviewQueryTask().execute(movieSearchUrl);
    }



    public class ReviewQueryTask extends AsyncTask<URL, Void, String> {


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
            String content = null;
            //mLoadingIndicator.setVisibility(View.GONE);
            if (movieSearchResults != null && !movieSearchResults.equals("")) {

                jasonReviewText = movieSearchResults;
                JSONObject reader = null;
                JSONArray results = null;
                try {
                    reader = new JSONObject(jasonReviewText);
                    results  = reader.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject resultsReader = null;
                        String author = null;

                        resultsReader= results.optJSONObject(i);
                        author = resultsReader.optString("author", "fall_back_string");
                        content = resultsReader.optString("content", "fall_back_string");

                        Items.add(new reviewsListItem(author,content));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showListReviews();

            } else {


            }
        }
    }

    class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<reviewsListItem> Items=new ArrayList<reviewsListItem>();
        MyCustomAdapter(ArrayList<reviewsListItem> Items ) {
            this.Items=Items;

        }


        @Override
        public int getCount() {
            return Items.size();
        }

        @Override
        public Integer getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return  position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater linflater =getLayoutInflater();
            View view1=linflater.inflate(R.layout.list, null);

            TextView content = view1.findViewById(R.id.content_tv);
            content.setText(Items.get(i).getContent());

            TextView author = view1.findViewById(R.id.author_tv);
            author.setText(Items.get(i).getAuthor());

            return view1;

        }

    }
}
