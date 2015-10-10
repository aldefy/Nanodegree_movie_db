package techgravy.popularmovies.adapter.holder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import techgravy.popularmovies.R;
import techgravy.popularmovies.activity.MovieDetailActivity;
import techgravy.popularmovies.models.MovieResultsModel;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView moviePhoto;
    public TextView movieTitle;
    ActivityOptions options = null;

    public MovieViewHolder(View itemView) {
        super(itemView);

        movieTitle = (TextView) itemView.findViewById(R.id.itemLabel);
        moviePhoto = (ImageView) itemView.findViewById(R.id.itemImage);
        moviePhoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
        MovieResultsModel model = (MovieResultsModel) view.getTag();
        String imgUrl = "http://image.tmdb.org/t/p/" + "w342" + model.getBackdrop_path();
        intent.putExtra("imageUrl", imgUrl);
        intent.putExtra("movieObject", model);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), view, "toolbarImage");
            view.getContext().startActivity(intent, options.toBundle());
        } else {
            view.getContext().startActivity(intent);
        }

    }
}