package com.google.moviesstageoneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.moviesstageoneapplication.model.Movie;
import com.google.moviesstageoneapplication.view.MoviesAdapter;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = null;

    private ImageView posterImg;

    private TextView title;
    private TextView releaseDate;
    private TextView userRating;
    private TextView overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        posterImg = findViewById(R.id.poster_img);

        title = findViewById(R.id.title);
        releaseDate = findViewById(R.id.release_date);
        userRating = findViewById(R.id.user_rating);
        overview = findViewById(R.id.overview);

        Intent intent = getIntent();

        int position = intent.getIntExtra(EXTRA_POSITION,-1);

        if(position == -1){
            return;
        }

        Movie movieObj = MainActivity.movieList.get(position);
        title.setText(movieObj.getTitle());
        releaseDate.setText(movieObj.getReleaseDate());
        userRating.setText(movieObj.getVoterAverage());
        overview.setText(movieObj.getOverview());

        Picasso.with(this)
                .load(movieObj.getPosterPath())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(posterImg);

    }
}
