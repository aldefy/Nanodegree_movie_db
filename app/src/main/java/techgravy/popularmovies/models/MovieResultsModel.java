package techgravy.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieResultsModel implements Parcelable, Rush {
    private String vote_average;

    private String backdrop_path;

    private String adult;
    @SerializedName("id")
    private String mId;

    private String title;

    private String original_language;

    private String overview;

    private String[] genre_ids;

    private String original_title;

    private String release_date;

    private String vote_count;

    private String poster_path;

    private String video;

    private String popularity;

    public MovieResultsModel() {
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

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Override
    public void save() {
        RushCore.getInstance().save(this);
    }

    @Override
    public void save(RushCallback callback) {
        RushCore.getInstance().save(this, callback);
    }

    @Override
    public void delete() {
        RushCore.getInstance().delete(this);
    }

    @Override
    public void delete(RushCallback callback) {
        RushCore.getInstance().delete(this, callback);
    }

    @Override
    public String getId() {
        return RushCore.getInstance().getId(this);
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

    public String[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(String[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
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

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "ClassPojo [vote_average = " + vote_average + ", backdrop_path = " + backdrop_path + ", adult = " + adult + ", mId = " + mId + ", title = " + title + ", original_language = " + original_language + ", overview = " + overview + ", genre_ids = " + genre_ids + ", original_title = " + original_title + ", release_date = " + release_date + ", vote_count = " + vote_count + ", poster_path = " + poster_path + ", video = " + video + ", popularity = " + popularity + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(vote_average);
        dest.writeString(mId);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeString(original_title);
    }

    // Creator
    public static final Parcelable.Creator<MovieResultsModel> CREATOR
            = new Parcelable.Creator<MovieResultsModel>() {
        public MovieResultsModel createFromParcel(Parcel in) {
            return new MovieResultsModel(in);
        }

        public MovieResultsModel[] newArray(int size) {
            return new MovieResultsModel[size];
        }
    };

    // "De-parcel object
    public MovieResultsModel(Parcel in) {
        title = in.readString();
        vote_average = in.readString();
        mId = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        original_title = in.readString();
    }


}
