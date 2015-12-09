package techgravy.popularmovies.interfaces;

import android.view.View;

import techgravy.popularmovies.models.MovieResultsModel;

/**
 * Created by aditlal on 08/12/15.
 */
public interface MovieDetailsFragmentInterface {
    void loadDetailsFragment(View v,String imageUrl, MovieResultsModel model);
}
