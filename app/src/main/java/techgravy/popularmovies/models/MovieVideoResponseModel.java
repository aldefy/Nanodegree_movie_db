package techgravy.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;

/**
 * Created by aditlal on 26/09/15.
 */

public class MovieVideoResponseModel implements Rush {
    @SerializedName("id")
    private String mId;

    public MovieVideoResponseModel() {
    }

    @SerializedName("results")
    private List<MovieVideoResultModel> results;

    public String getmId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public List<MovieVideoResultModel> getResults() {
        return results;
    }

    public void setResults(List<MovieVideoResultModel> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ClassPojo [mId = " + mId + ", results = " + results + "]";
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

