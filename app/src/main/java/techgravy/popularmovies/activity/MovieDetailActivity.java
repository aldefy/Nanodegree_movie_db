package techgravy.popularmovies.activity;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.style.BulletSpan;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import techgravy.popularmovies.BuildConfig;
import techgravy.popularmovies.R;
import techgravy.popularmovies.api.GetPopularMovieApi;
import techgravy.popularmovies.generator.MovieApiGenerator;
import techgravy.popularmovies.models.MovieCreditsResponseModel;
import techgravy.popularmovies.models.MovieDetailModel;
import techgravy.popularmovies.models.MovieGenres;
import techgravy.popularmovies.models.MovieProductionCompanies;
import techgravy.popularmovies.models.MovieProductionCountries;
import techgravy.popularmovies.models.MovieResultsModel;
import techgravy.popularmovies.models.MovieVideoResponseModel;
import techgravy.popularmovies.utils.IntentUtils;
import techgravy.popularmovies.utils.Logger;
import techgravy.popularmovies.utils.PaletteTransformation;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieDetailActivity extends AppCompatActivity {


    String imageUrl, title, videoId1;
    MovieResultsModel model;


    GetPopularMovieApi getPopularMovieApi;
    @InjectView(R.id.itemImage)
    ImageView itemImage;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.ratingTitle)
    TextView ratingTitle;
    @InjectView(R.id.topBarLayout)
    RelativeLayout topBarLayout;
    @InjectView(R.id.plotOverviewTextView)
    TextView plotOverviewTextView;
    @InjectView(R.id.taglineText)
    TextView taglineText;
    @InjectView(R.id.imdbText)
    TextView imdbText;
    @InjectView(R.id.productionCompaniesText)
    TextView productionCompaniesText;
    @InjectView(R.id.productionCountriesText)
    TextView productionCountriesText;
    @InjectView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @InjectView(R.id.locateFab)
    FloatingActionButton locateFab;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;
    MovieDetailModel detailModel;
    @InjectView(R.id.tagText)
    TextView tagText;

    int circleColor;
    @InjectView(R.id.infoTitle)
    TextView infoTitle;
    @InjectView(R.id.runningTimeTextView)
    TextView runningTimeTextView;
    @InjectView(R.id.tagLineDivider)
    View tagLineDivider;
    @InjectView(R.id.releasedTextView)
    TextView releasedTextView;
    private String apiKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleExtras();
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        apiKey = BuildConfig.API_KEY;
        getPopularMovieApi = MovieApiGenerator.createService(GetPopularMovieApi.class);
        getMovieDetails();
        init();


    }

    private void getMovieDetails() {
        getPopularMovieApi.getMovieDetails(model.getId(), apiKey, new Callback<MovieDetailModel>() {
            @Override
            public void success(MovieDetailModel movieDetailModel, Response response) {
                detailModel = movieDetailModel;
                Logger.d("Movie Detail ", detailModel.toString());
                try {
                    String imdbUrl = "IMDB : http://www.imdb.com/title/" + detailModel.getImdb_id();
                    imdbText.setText(imdbUrl);
                    Linkify.addLinks(imdbText, Linkify.WEB_URLS);
                    for (int i = 0; i < detailModel.getGenres().size(); i++) {
                        MovieGenres genres = detailModel.getGenres().get(i);
                        tagText.append(genres.getName() + "\n");
                    }
                    runningTimeTextView.setText("Runtime : " + detailModel.getRuntime() + " mins");
                    releasedTextView.setText("Released : " + detailModel.getRelease_date());
                    Spanny spanny;
                    for (MovieProductionCompanies companies : detailModel.getProduction_companies()) {
                        spanny = new Spanny(companies.getName() + "\n", new BulletSpan());
                        productionCompaniesText.append(spanny);
                    }
                    for (MovieProductionCountries countries : detailModel.getProduction_countries()) {
                        spanny = new Spanny(countries.getName() + "\n", new BulletSpan());
                        productionCountriesText.append(spanny);
                    }

                    if (!detailModel.getTagline().isEmpty())
                        taglineText.setText(detailModel.getTagline());
                    else {
                        tagLineDivider.setVisibility(View.GONE);
                        taglineText.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("Movie id ", model.getId() + "");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.e("Movie Detail Error" + error.getMessage().toString());
            }
        });
        getMovieVideoForId(model);
        getMovieCredits();
    }

    private void getMovieVideoForId(MovieResultsModel model) {
        getPopularMovieApi.getVideoForId(model.getId(), apiKey, new Callback<MovieVideoResponseModel>() {
            @Override
            public void success(MovieVideoResponseModel movieResponseModel, Response response) {
                videoId1 = movieResponseModel.getResults().get(0).getKey();
                Logger.d("Video id ", videoId1);
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.e("Video id Error" + error.getMessage().toString());
            }
        });
    }

    private void getMovieCredits() {
        getPopularMovieApi.getMovieCredits(model.getId(), apiKey, new Callback<MovieCreditsResponseModel>() {
            @Override
            public void success(MovieCreditsResponseModel movieCreditsResponseModel, Response response) {
                Logger.d("MovieCredits Response", movieCreditsResponseModel.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.e("MovieCredits Response", error.getMessage());
            }
        });
    }


    private void init() {

        Picasso.with(MovieDetailActivity.this).load(imageUrl).transform(PaletteTransformation.instance()).into(itemImage,
                new PaletteTransformation.PaletteCallback(itemImage) {
                    @Override
                    protected void onSuccess(Palette palette) {
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        if (vibrant != null) {
                            topBarLayout.setBackgroundColor(vibrant.getRgb());
                        } else {
                            Logger.d("Vibrant not available");
                        }
                        Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();
                        if (vibrantDark != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrantDark.getRgb());
                            }
                            if (vibrant == null) {
                                topBarLayout.setBackgroundColor(vibrantDark.getRgb());
                            }
                            collapsingToolbar.setContentScrim(new ColorDrawable(vibrantDark.getRgb()));
                        }

                        Palette.Swatch mutedD = palette.getDarkMutedSwatch();
                        if (mutedD != null) {
                            locateFab.setBackgroundTintList(ColorStateList.valueOf(mutedD.getRgb()));
                        }
                        Palette.Swatch muted = palette.getLightMutedSwatch();
                        if (muted != null) {
                            circleColor = muted.getRgb();
                        }
                    }

                    @Override
                    public void onError() {
                        Logger.e("ERROR", "palette error occured");
                    }
                });
        setupToolBar();
        plotOverviewTextView.setText(model.getOverview());
        ratingTitle.setText("Rating : " + model.getVote_average());

        locateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("Clicked FAB", videoId1 + "");
                IntentUtils.openYoutubeVideo(MovieDetailActivity.this, videoId1);
            }
        });

    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(title);
    }

    private void handleExtras() {
        Bundle b = getIntent().getExtras();

        imageUrl = b.getString("imageUrl");
        model = (MovieResultsModel) b.get("movieObject");
        title = model.getOriginal_title();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
