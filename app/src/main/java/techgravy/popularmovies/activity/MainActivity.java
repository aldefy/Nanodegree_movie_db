package techgravy.popularmovies.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.BindBool;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import techgravy.popularmovies.R;
import techgravy.popularmovies.fragment.DashboardMovieGridFragment;
import techgravy.popularmovies.fragment.MovieDetailsFragment;
import techgravy.popularmovies.interfaces.ItemClickListener;
import techgravy.popularmovies.models.MovieResultsModel;
import techgravy.popularmovies.utils.Logger;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    @Bind(R.id.dashboard_toolbar)
    Toolbar dashboardToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;
    @Bind(R.id.fragmentGridContainer)
    FrameLayout fragmentContainer;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @BindString(R.string.popularMovies)
    String pMovieTag;
    @BindString(R.string.highestRatedMovies)
    String hMovieTag;
    @BindString(R.string.favouriteMovies)
    String fMovieTag;
    @BindString(R.string.app_name)
    String appName;
    @BindBool(R.bool.portrait)
    boolean isPortrait;


    DashboardMovieGridFragment gridFragment;
    MovieDetailsFragment detailsFragment;
    ArrayAdapter<String> menuSortAdapter;
    MaterialDialog sortDialog;


    FragmentTransaction transaction;
    private ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setUpViews();
    }

    private void setUpViews() {


        if (isPortrait) {
            transaction = getSupportFragmentManager().beginTransaction();
            gridFragment = (DashboardMovieGridFragment) getSupportFragmentManager().findFragmentByTag("fragGrid");
            if (gridFragment == null)
                gridFragment = new DashboardMovieGridFragment();
            transaction.replace(R.id.fragmentGridContainer, gridFragment, "fragGrid");
            transaction.commit();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            transaction = getSupportFragmentManager().beginTransaction();
            gridFragment = (DashboardMovieGridFragment) getSupportFragmentManager().findFragmentByTag("fragGrid");
            if (gridFragment == null)
                gridFragment = new DashboardMovieGridFragment();
            transaction.replace(R.id.fragmentGridContainer, gridFragment, "fragGrid");
            transaction.commit();
            if (findViewById(R.id.fragmentDetailContainer) != null) {
                transaction = getSupportFragmentManager().beginTransaction();
                detailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentByTag("fragDetails");
                if (detailsFragment == null)
                    detailsFragment = new MovieDetailsFragment();
                transaction.replace(R.id.fragmentDetailContainer, detailsFragment, "fragDetails");
                transaction.commit();

            }
        }
    }


    private void setupToolbar() {
        setSupportActionBar(dashboardToolbar);
        getSupportActionBar().setTitle(appName);
    }

    @OnClick(R.id.fab)
    void fabOnClick() {
        menuSortAdapter = new ArrayAdapter<String>(this, R.layout.adapter_sort);
        menuSortAdapter.add(pMovieTag);
        menuSortAdapter.add(hMovieTag);
        menuSortAdapter.add(fMovieTag);
        View sortDialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_sort, null);
        ListView menuSortListView = (ListView) sortDialogView.findViewById(R.id.sortListView);
        menuSortListView.setAdapter(menuSortAdapter);
        menuSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle(pMovieTag);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(hMovieTag);
                        break;
                    case 2:
                        getSupportActionBar().setTitle(fMovieTag);
                        break;
                    default:
                        getSupportActionBar().setTitle(appName);
                        break;
                }
                /*searchView.setQuery("", false); //clear the text
                searchView.setIconified(true);//close the search editor and make search icon again*/


                gridFragment.sortMovies(position + 1);
                sortDialog.dismiss();

            }
        });

        sortDialog = new MaterialDialog(this)
                .setTitle("Sort By :")
                .setContentView(sortDialogView);
        sortDialog.setNegativeButton(
                "Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortDialog.dismiss();
                    }
                }
        );
        sortDialog.setCanceledOnTouchOutside(true);
        sortDialog.show();
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void loadDetailsFragment(View v, String imageUrl, MovieResultsModel model) {
        Logger.d("GridDetailsFragment", imageUrl + " " + model.getTitle());
        if (findViewById(R.id.fragmentDetailContainer) != null) {

            transaction = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putString("imageUrl", imageUrl);
            args.putParcelable("movieObject", model);
            detailsFragment = MovieDetailsFragment.newInstance(args);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                detailsFragment.setSharedElementReturnTransition(TransitionInflater.from(MainActivity.this).inflateTransition(R.transition.trans_move));
                detailsFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
                transaction.addSharedElement(v, "toolbarImage");
            }

            transaction.replace(R.id.fragmentDetailContainer, detailsFragment, "fragDetails");
            transaction.commit();
        }
    }

    @Override
    public void movieClicked(MovieResultsModel model, View view) {
        String imgUrl;

        if (isPortrait) {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            imgUrl = "http://image.tmdb.org/t/p/" + "w342" + model.getBackdrop_path();
            intent.putExtra("imageUrl", imgUrl);
            intent.putExtra("movieObject", model);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "toolbarImage");
                MainActivity.this.startActivity(intent, options.toBundle());
            } else {
                MainActivity.this.startActivity(intent);
            }

        } else {
            imgUrl = "http://image.tmdb.org/t/p/" + "w342" + model.getBackdrop_path();

            loadDetailsFragment(view, imgUrl, model);

        }

    }
}
