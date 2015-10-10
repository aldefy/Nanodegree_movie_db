package techgravy.popularmovies.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.drakeet.materialdialog.MaterialDialog;
import techgravy.popularmovies.R;
import techgravy.popularmovies.fragment.DashboardMovieGridFragment;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.dashboard_toolbar)
    Toolbar dashboardToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.coordinator)
    CoordinatorLayout coordinator;
    DashboardMovieGridFragment gridFragment;

    ArrayAdapter<String> menuSortAdapter;
    MaterialDialog sortDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            menuSortAdapter = new ArrayAdapter<String>(this,
                    R.layout.adapter_sort
            );
            menuSortAdapter.add("Popular Movies");
            menuSortAdapter.add("Highest Rated Movies");
            View sortDialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_sort, null);
            ListView menuSortListView = (ListView) sortDialogView.findViewById(R.id.sortListView);
            menuSortListView.setAdapter(menuSortAdapter);
            menuSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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


            sortDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
