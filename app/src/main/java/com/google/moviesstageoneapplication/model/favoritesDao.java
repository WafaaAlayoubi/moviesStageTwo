package com.google.moviesstageoneapplication.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface favoritesDao {

    @Query("SELECT * FROM favorites ORDER BY id")
    LiveData<List<favorites>> loadAllFavorites();

    @Insert
    void insertFavorite(favorites favorites);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(favorites favorites);

    @Delete
    void deleteFavorite(favorites favorites);



}
