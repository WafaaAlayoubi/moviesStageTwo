package com.google.moviesstageoneapplication.view;

/**
 * Created by ravi on 20/02/18.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.moviesstageoneapplication.MainActivity;
import com.google.moviesstageoneapplication.R;
import com.google.moviesstageoneapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mMovieName;
        public ImageView mMovieImg;



        public MyViewHolder(final View view) {
            super(view);

            mMovieName = view.findViewById(R.id.rv_name);
            mMovieImg = view.findViewById(R.id.rv_img);

        }
    }


    public MoviesAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType") View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_element, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.mMovieName.setText(movie.getTitle());
//        new DownloadImageTask(holder.mMovieImg)
//                .execute(movie.getPosterPath());
        Picasso.with(context)
                .load(movie.getPosterPath())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.mMovieImg);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}
