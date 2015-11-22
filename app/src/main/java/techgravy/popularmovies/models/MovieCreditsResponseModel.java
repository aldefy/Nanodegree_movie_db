package techgravy.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;

/**
 * Created by aditlal on 27/09/15.
 */
public class MovieCreditsResponseModel implements Rush {
    @SerializedName("id")
    private String mId;

    @SerializedName("cast")
    private List<MovieCreditsCastModel> cast;

    @SerializedName("crew")
    private List<MovieCreditsCrewModel> crew;

    public MovieCreditsResponseModel() {
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
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
        return "ClassPojo [mId = " + mId + ", cast = " + cast + ", crew = " + crew + "]";
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
}
