package techgravy.popularmovies.interfaces;

import android.view.View;

import techgravy.popularmovies.models.MovieResultsModel;

/**
 * Created by aditlal on 09/12/15.
 */
public interface ItemClickListener {
    void movieClicked(MovieResultsModel model,  View view);
}
