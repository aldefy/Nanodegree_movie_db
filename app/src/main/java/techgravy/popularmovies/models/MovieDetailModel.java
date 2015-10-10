package techgravy.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aditlal on 26/09/15.
 */

public class MovieDetailModel {
    private String budget;

    private String vote_average;

    private String backdrop_path;

    @SerializedName("genres")
    private List<MovieGenres> genres;

    private String status;

    private String runtime;

    @SerializedName("spoken_languages")
    private List<MovieSpokenLanguage> spoken_languages;

    private String adult;

    private String homepage;

    private String id;
    @SerializedName("production_companies")
    private List<MovieProductionCompanies> production_companies;

    private String title;

    private String original_language;

    private String overview;

    private List<MovieProductionCountries> production_countries;


    private String imdb_id;

    private String release_date;

    private String original_title;

    private String vote_count;

    private String poster_path;

    private String video;

    private String tagline;

    private String revenue;

    private String popularity;

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public List<MovieGenres> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieGenres> genres) {
        this.genres = genres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public List<MovieSpokenLanguage> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<MovieSpokenLanguage> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieProductionCountries> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<MovieProductionCountries> production_countries) {
        this.production_countries = production_countries;
    }

    public List<MovieProductionCompanies> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<MovieProductionCompanies> production_companies) {
        this.production_companies = production_companies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }



    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "ClassPojo [budget = " + budget + ", vote_average = " + vote_average + ", backdrop_path = " + backdrop_path + ", genres = " + genres + ", status = " + status + ", runtime = " + runtime + ", spoken_languages = " + spoken_languages + ", adult = " + adult + ", homepage = " + homepage + ", id = " + id + ", production_countries = " + production_countries + ", title = " + title + ", original_language = " + original_language + ", overview = " + overview + ", production_companies = " + production_companies + "" + ", imdb_id = " + imdb_id + ", release_date = " + release_date + ", original_title = " + original_title + ", vote_count = " + vote_count + ", poster_path = " + poster_path + ", video = " + video + ", tagline = " + tagline + ", revenue = " + revenue + ", popularity = " + popularity + "]";
    }
}

