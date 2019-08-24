package com.google.moviesstageoneapplication.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class favorites {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String movieId;
    private String Title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String voterAverage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Ignore
    public  favorites(String movieId, String Title, String posterPath,String overview,String releaseDate,String voterAverage){
        this.movieId = movieId;
        this.Title = Title;
        this.posterPath =posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voterAverage=voterAverage;
    }

    public String getVoterAverage() {
        return voterAverage;
    }

    public void setVoterAverage(String voterAverage) {
        this.voterAverage = voterAverage;
    }

    public  favorites(int id, String movieId, String Title, String posterPath, String overview, String releaseDate, String voterAverage){
        this.id= id;
        this.movieId = movieId;
        this.Title = Title;
        this.posterPath =posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voterAverage=voterAverage;
    }
}
