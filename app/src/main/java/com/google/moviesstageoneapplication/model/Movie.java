package com.google.moviesstageoneapplication.model;

public class Movie {

    private String id;
    private String Title;
    private String posterPath;
    private String overview;
    private String releaseDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String voterAverage;

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

    public String getVoterAverage() {
        return voterAverage;
    }

    public void setVoterAverage(String voterAverage) {
        this.voterAverage = voterAverage;
    }
}
