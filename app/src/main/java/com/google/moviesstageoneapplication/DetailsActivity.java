package com.google.moviesstageoneapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.moviesstageoneapplication.model.Movie;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = null;

    private ImageView posterImg;

    private TextView title;
    private TextView releaseDate;
    private TextView userRating;
    private TextView overview;



    int position ;



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
        position = intent.getIntExtra(EXTRA_POSITION,-1);

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


    public void trailer(View v)
    {
        Intent intent = new Intent(getBaseContext(), VideosActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }

    public void reviews(View v)
    {
        Intent intent = new Intent(getBaseContext(), ReviewsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }


}
