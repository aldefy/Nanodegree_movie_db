package techgravy.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aditlal on 27/09/15.
 */
public class MovieCreditsResponseModel {
    private String id;

    @SerializedName("cast")
    private List<MovieCreditsCastModel> cast;

    @SerializedName("crew")
    private List<MovieCreditsCrewModel> crew;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieCreditsCastModel> getCast() {
        return cast;
    }

    public void setCast(List<MovieCreditsCastModel> cast) {
        this.cast = cast;
    }

    public List<MovieCreditsCrewModel> getCrew() {
        return crew;
    }

    public void setCrew(List<MovieCreditsCrewModel> crew) {
        this.crew = crew;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", cast = " + cast + ", crew = " + crew + "]";
    }
}
