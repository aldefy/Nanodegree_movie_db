package techgravy.popularmovies.adapter.holder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import techgravy.popularmovies.R;
import techgravy.popularmovies.activity.MovieDetailActivity;
import techgravy.popularmovies.interfaces.ItemClickListener;
import techgravy.popularmovies.models.MovieResultsModel;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.itemImage)
    public ImageView itemImage;
    @Bind(R.id.itemLabel)
    public TextView itemLabel;
    ActivityOptions options = null;
    ItemClickListener itemClickListener;

    public MovieViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemClickListener = itemClickListener;

        itemImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        MovieResultsModel model = (MovieResultsModel) view.getTag();

        if(itemClickListener!=null)
        {
            itemClickListener.movieClicked(model,view);
        }

    }


}