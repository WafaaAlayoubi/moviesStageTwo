package com.google.moviesstageoneapplication.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.moviesstageoneapplication.MainActivity;
import com.google.moviesstageoneapplication.database.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<favorites>> favorite;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance (this.getApplication ());
        favorite = database.favoritesDao ().loadAllFavorites ();
    }

    public LiveData<List<favorites>> getMovies() {
        return favorite;
    }
}
