package techgravy.popularmovies.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.uk.rushorm.core.RushSearch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import techgravy.popularmovies.BuildConfig;
import techgravy.popularmovies.R;
import techgravy.popularmovies.adapter.DashboardRecyclerAdapter;
import techgravy.popularmovies.api.GetPopularMovieApi;
import techgravy.popularmovies.generator.MovieApiGenerator;
import techgravy.popularmovies.models.MovieResponseModel;
import techgravy.popularmovies.models.MovieResultsModel;
import techgravy.popularmovies.utils.ConnectivityUtils;
import techgravy.popularmovies.utils.EndlessRecyclerOnScrollListener;
import techgravy.popularmovies.utils.GridSpacesItemDecoration;
import techgravy.popularmovies.utils.IntentUtils;
import techgravy.popularmovies.utils.Logger;

/**
 * Created by aditlal on 26/09/15.
 */
public class DashboardMovieGridFragment extends Fragment {

    @Bind(R.id.recyclerGridView)
    RecyclerView recyclerGridView;
    @Bind(R.id.root)
    LinearLayout root;
    private GridLayoutManager staggeredGridLayoutManager;
    GetPopularMovieApi getPopularMovieApi;
    private ArrayList<MovieResultsModel> moviesList;
    private DashboardRecyclerAdapter rcAdapter;
    int page = 1;
    int sortOrder;
    String apiKey;
    ProgressDialog loadingDialog;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_grid, container, false);
        ButterKnife.bind(this, rootView);
        getPopularMovieApi = MovieApiGenerator.createService(GetPopularMovieApi.class);
        moviesList = new ArrayList<>();
        apiKey = BuildConfig.API_KEY;
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
        Logger.d("Movies savedInstanceState view ", "Called fetchMovies");

        if (!ConnectivityUtils.isConnected(getActivity()) && sortOrder != IntentUtils.FAVORITES) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Network communication issue")
                    .setMessage("Please check your internet connection")
                    .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        if (page == 1)
            loadingDialog.show();
        Logger.d("Movies page", "loading " + page);
        if (sortOrder == IntentUtils.POPULAR_SORT)
            getPopularMovieApi.getMoviesByPopularity(page, apiKey, new Callback<MovieResponseModel>() {
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
        else if (sortOrder == IntentUtils.HIGHEST_RATED_SORT)
            getPopularMovieApi.getMoviesByRating(page, apiKey, new Callback<MovieResponseModel>() {
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
        else {
            try {
                List<MovieResultsModel> list = new RushSearch()
                        .find(MovieResultsModel.class);
                moviesList.clear();
                rcAdapter.notifyDataSetChanged();
                if (list.size() != 0) {

                    Logger.d("SortOrder", sortOrder + "" + list.size() + " " + list.get(0).getOriginal_title());
                    moviesList.addAll(list);
                    rcAdapter.notifyDataSetChanged();
                }
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void sortMovies(int sortOrder) {
        this.sortOrder = sortOrder;
        Logger.d("SortOrder", sortOrder + "");

        moviesList.clear();
        rcAdapter.notifyDataSetChanged();
        endlessRecyclerOnScrollListener.reset(0, true);
        page = 1;
        fetchMovies(page);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("Onresume "+ sortOrder);
    }
}
