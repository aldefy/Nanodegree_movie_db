package techgravy.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aditlal on 26/09/15.
 */

public class MovieVideoResponseModel {
    private String id;

    @SerializedName("results")
    private List<MovieVideoResultModel> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieVideoResultModel> getResults() {
        return results;
    }

    public void setResults(List<MovieVideoResultModel> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", results = " + results + "]";
    }
}

