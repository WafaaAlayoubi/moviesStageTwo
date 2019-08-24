package com.google.moviesstageoneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.moviesstageoneapplication.database.AppDatabase;
import com.google.moviesstageoneapplication.model.AppExecutor;
import com.google.moviesstageoneapplication.model.MainViewModel;
import com.google.moviesstageoneapplication.model.Movie;
import com.google.moviesstageoneapplication.model.favorites;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = null;

    private ImageView posterImg;

    private TextView title;
    private TextView releaseDate;
    private TextView userRating;
    private TextView overview;

    int position ;

    private AppDatabase mDb;
    private Movie movieObj;
    private favorites favObj;

    private ToggleButton favoriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        position = intent.getIntExtra(EXTRA_POSITION,-1);

        mDb = AppDatabase.getInstance(getApplicationContext());

        favoriteBtn = findViewById(R.id.favorites_btn);

        favoriteBtn.setTextOn("Remove from favorite");
        favoriteBtn.setTextOff("Add to favorite");

        movieObj= MainActivity.movieList.get(position);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<favorites>>() {
            @Override
            public void onChanged(List<favorites> favorites) {
                for (int i =0 ;i<favorites.size();i++){
                    favObj = favorites.get(i);

                    if (favObj.getMovieId().equals(movieObj.getId())){
                        favoriteBtn.setChecked(true);
                        break;
                    }
                    else
                        favoriteBtn.setChecked(false);
                }

            }

        });






        favoriteBtn.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked && buttonView.isPressed()) {
                favoriteBtn.getTextOn ();
                final favorites favoritesEntry = new favorites(movieObj.getId(),movieObj.getTitle(),movieObj.getPosterPath(),movieObj.getOverview(),movieObj.getReleaseDate(),movieObj.getVoterAverage());

                AppExecutor.getInstance ().diskIO ().execute (() -> mDb.favoritesDao ().insertFavorite(favoritesEntry));

                finish();

            }
            else if(isChecked!=true && buttonView.isPressed()){
                favoriteBtn.getTextOff();
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.favoritesDao().deleteFavorite(favObj);
                        finish();
                }
                });
            }
        });


        posterImg = findViewById(R.id.poster_img);

        title = findViewById(R.id.title);
        releaseDate = findViewById(R.id.release_date);
        userRating = findViewById(R.id.user_rating);
        overview = findViewById(R.id.overview);


        if(position == -1){
            return;
        }

         movieObj = MainActivity.movieList.get(position);
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
