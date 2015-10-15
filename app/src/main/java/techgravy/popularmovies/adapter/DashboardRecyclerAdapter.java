package techgravy.popularmovies.adapter;

import android.content.Context;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import techgravy.popularmovies.R;
import techgravy.popularmovies.adapter.holder.MovieViewHolder;
import techgravy.popularmovies.models.MovieResultsModel;
import techgravy.popularmovies.utils.Logger;
import techgravy.popularmovies.utils.PaletteTransformation;

/**
 * Created by aditlal DashboardRecyclerAdapter on 26/09/15.
 */
public class DashboardRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private List<MovieResultsModel> itemList;
    private Context context;

    public DashboardRecyclerAdapter(Context context, List<MovieResultsModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_dashboard, null);
        MovieViewHolder rcv = new MovieViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.movieTitle.setText(itemList.get(position).getTitle());
        String imgUrl = "http://image.tmdb.org/t/p/" + "w342" + itemList.get(position).getBackdrop_path();
        Logger.d("Adapter image", "url is " + imgUrl);
        holder.moviePhoto.setTag(itemList.get(position));
        Picasso.with(context).load(imgUrl).placeholder(R.drawable.ic_placeholder_movie).error(R.drawable.ic_placeholder_movie).transform(PaletteTransformation.instance()).into(holder.moviePhoto, new PaletteTransformation.PaletteCallback(holder.moviePhoto) {
            @Override
            protected void onSuccess(Palette palette) {
                Palette.Swatch vibrant = palette.getDarkVibrantSwatch();
                if (vibrant != null) {
                    // Update the title TextView with the proper text color
                    holder.movieTitle.setBackgroundColor(vibrant.getRgb());
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
