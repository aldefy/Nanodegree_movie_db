package techgravy.popularmovies.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import techgravy.popularmovies.R;
import techgravy.popularmovies.adapter.DashboardRecyclerAdapter;
import techgravy.popularmovies.api.GetPopularMovieApi;
import techgravy.popularmovies.generator.MovieApiGenerator;
import techgravy.popularmovies.models.MovieResponseModel;
import techgravy.popularmovies.models.MovieResultsModel;
import techgravy.popularmovies.utils.EndlessRecyclerOnScrollListener;
import techgravy.popularmovies.utils.GridSpacesItemDecoration;
import techgravy.popularmovies.utils.IntentUtils;
import techgravy.popularmovies.utils.Logger;

/**
 * Created by aditlal on 26/09/15.
 */
public class DashboardMovieGridFragment extends Fragment {

    @InjectView(R.id.recyclerGridView)
    RecyclerView recyclerGridView;
    private GridLayoutManager staggeredGridLayoutManager;
    GetPopularMovieApi getPopularMovieApi;
    private List<MovieResultsModel> moviesList;
    private DashboardRecyclerAdapter rcAdapter;
    int page = 1;
    int sortOrder;
    ProgressDialog loadingDialog;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_grid, container, false);
        ButterKnife.inject(this, rootView);
        getPopularMovieApi = MovieApiGenerator.createService(GetPopularMovieApi.class);
        moviesList = new ArrayList<>();
        setupViews();
        return rootView;
    }


    private void setupViews() {
        recyclerGridView.setHasFixedSize(false);
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("Fetching movies");
        staggeredGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerGridView.setLayoutManager(staggeredGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.rv_grid_spacing);
        recyclerGridView.addItemDecoration(new GridSpacesItemDecoration(spacingInPixels));
        rcAdapter = new DashboardRecyclerAdapter(getActivity(), moviesList);
        recyclerGridView.setAdapter(rcAdapter);
        sortOrder = 01;

        fetchMovies(page);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Logger.d("Current page is ", current_page + "");
                fetchMovies(current_page);
            }
        };
        recyclerGridView.addOnScrollListener(endlessRecyclerOnScrollListener);


    }

    private void fetchMovies(int page) {
        loadingDialog.show();
        Logger.d("Movies page", "loading " + page);
        if (sortOrder == IntentUtils.POPULAR_SORT)
            getPopularMovieApi.getMoviesByPopularity(page, "b6a0f31f0237fcf5b0ee5670bf2ef99b", new Callback<MovieResponseModel>() {
                @Override
                public void success(MovieResponseModel movieResponseModel, Response response) {
                    Logger.d("Sucess", movieResponseModel.toString());
                    loadingDialog.cancel();
                    moviesList.addAll(movieResponseModel.getResults());
                    rcAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    loadingDialog.cancel();
                    Logger.e("Error", error.getMessage());
                }
            });
        else
            getPopularMovieApi.getMoviesByRating(page, "b6a0f31f0237fcf5b0ee5670bf2ef99b", new Callback<MovieResponseModel>() {
                @Override
                public void success(MovieResponseModel movieResponseModel, Response response) {
                    Logger.d("Sucess", movieResponseModel.toString());
                    moviesList.addAll(movieResponseModel.getResults());
                    loadingDialog.cancel();
                    rcAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Logger.e("Error", error.getMessage());
                    loadingDialog.cancel();
                }
            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void sortMovies(int sortOrder) {
        this.sortOrder = sortOrder;
        moviesList.clear();
        rcAdapter.notifyDataSetChanged();
        endlessRecyclerOnScrollListener.reset(0, true);
        page = 1;
        fetchMovies(page);

    }
}
