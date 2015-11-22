package techgravy.popularmovies.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import techgravy.popularmovies.R;
import techgravy.popularmovies.fragment.DashboardMovieGridFragment;

public class MainActivity extends AppCompatActivity  {

    @Bind(R.id.dashboard_toolbar)
    Toolbar dashboardToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;
    DashboardMovieGridFragment gridFragment;

    ArrayAdapter<String> menuSortAdapter;
    MaterialDialog sortDialog;
    @Bind(R.id.fragmentContainer)
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

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setUpViews();


    }

    private void setUpViews() {
        gridFragment = new DashboardMovieGridFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, gridFragment);
        transaction.commit();
    }


    private void setupToolbar() {
        setSupportActionBar(dashboardToolbar);
        getSupportActionBar().setTitle(appName);
    }

    @OnClick(R.id.fab)
    void fabOnClick() {
        menuSortAdapter = new ArrayAdapter<String>(this,
                R.layout.adapter_sort
        );
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(MainActivity.this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement our filter logic
        gridFragment.filterItems(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }*/

}
