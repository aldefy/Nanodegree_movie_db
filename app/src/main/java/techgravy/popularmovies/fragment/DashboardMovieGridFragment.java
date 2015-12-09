package techgravy.popularmovies.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindBool;
import butterknife.BindString;
import butterknife.ButterKnife;
import co.uk.rushorm.core.RushSearch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import techgravy.popularmovies.BuildConfig;
import techgravy.popularmovies.R;
import techgravy.popularmovies.activity.MainActivity;
import techgravy.popularmovies.adapter.DashboardRecyclerAdapter;
import techgravy.popularmovies.api.GetPopularMovieApi;
import techgravy.popularmovies.generator.MovieApiGenerator;
import techgravy.popularmovies.interfaces.ItemClickListener;
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
public class DashboardMovieGridFragment extends Fragment  {

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
    @BindString(R.string.favouriteMovies)
    String fMovieTag;
    @BindBool(R.bool.portrait)
    boolean isPortrait;

    ProgressDialog loadingDialog;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    ActivityOptions options = null;
    private ItemClickListener itemClickListener;
    MovieResultsModel model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // the callback interface. If not, it throws an exception
        try {
            itemClickListener = (ItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().toString()
                    + " must implement ItemClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_grid, container, false);
        ButterKnife.bind(this, rootView);
        getPopularMovieApi = MovieApiGenerator.createService(GetPopularMovieApi.class);
        moviesList = new ArrayList<>();
        apiKey = BuildConfig.API_KEY;
        if (savedInstanceState != null) {
            moviesList = (ArrayList<MovieResultsModel>) savedInstanceState.get(IntentUtils.MOVIE_LIST_SAVE_INSTANCE);
        }
        setupViews();
        return rootView;
    }


    private void setupViews() {
        recyclerGridView.setHasFixedSize(false);
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("Fetching movies");
        staggeredGridLayoutManager = new GridLayoutManager(getActivity(), (int) getResources().getInteger(R.integer.grid_size));
        recyclerGridView.setLayoutManager(staggeredGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.rv_grid_spacing);
        recyclerGridView.addItemDecoration(new GridSpacesItemDecoration(spacingInPixels));
        rcAdapter = new DashboardRecyclerAdapter(getActivity(), moviesList,itemClickListener);
        recyclerGridView.setAdapter(rcAdapter);
        if (getMyListData() != null && getMyListData().size() > 0) {
            // moviesList.addAll(getMyListData());
            rcAdapter.notifyDataSetChanged();
        } else {
            sortOrder = 01;
            fetchMovies(page);
        }
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Logger.d("Current page is ", current_page + "");
                fetchMovies(current_page);
            }
        };
        recyclerGridView.addOnScrollListener(endlessRecyclerOnScrollListener);

        /*recyclerGridView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(getActivity(), new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        model = rcAdapter.getItemAtPos(position);
                        Logger.d("View click", view.getId()+"");

                    }
                })
        );*/
    }


    private void fetchMovies(int page) {
        Logger.d("Movies savedInstanceState view ", "Called fetchMovies");

        if (!ConnectivityUtils.isConnected(getActivity()) && sortOrder != IntentUtils.FAVORITES) {

           /* ViewGroup viewGroup = findSuitableParent(getActivity().findViewById(android.R.id.content));
            Snackbar.make(viewGroup, "Please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                            getActivity().finish();
                        }
                    }).show();*/
            ((MainActivity) getActivity()).setTitle(fMovieTag);
            getFavMovies();
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
            getFavMovies();
        }

    }

    private void getFavMovies() {
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

    // Fires when a configuration change occurs and fragment needs to save state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(IntentUtils.MOVIE_LIST_SAVE_INSTANCE,
                (ArrayList<? extends Parcelable>) moviesList);
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

    public void setMyListData(ArrayList<MovieResultsModel> data) {
        this.moviesList = data;
    }

    public ArrayList<MovieResultsModel> getMyListData() {
        return this.moviesList;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("Onresume " + sortOrder);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;

        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup) view;
            }

            if (view instanceof FrameLayout) {
                // android.R.id.content
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                }

                fallback = (ViewGroup) view;
            }

            if (view != null) {
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }


}
