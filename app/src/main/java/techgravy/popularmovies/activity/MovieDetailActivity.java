package techgravy.popularmovies.activity;

import android.content.Intent;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.style.BulletSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushSearch;
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
import techgravy.popularmovies.models.MovieReviewModel;
import techgravy.popularmovies.models.MovieReviewResults;
import techgravy.popularmovies.models.MovieVideoResponseModel;
import techgravy.popularmovies.models.MovieVideoResultModel;
import techgravy.popularmovies.utils.IntentUtils;
import techgravy.popularmovies.utils.Logger;
import techgravy.popularmovies.utils.PaletteTransformation;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieDetailActivity extends AppCompatActivity {


    String imageUrl, title, videoId1;
    MovieResultsModel model;
    boolean mFavourite = false;
    int circleColor;
    MovieDetailModel detailModel;
    GetPopularMovieApi getPopularMovieApi;
    @Bind(R.id.itemImage)
    ImageView itemImage;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.ratingTitle)
    TextView ratingTitle;
    @Bind(R.id.runningTimeTextView)
    TextView runningTimeTextView;
    @Bind(R.id.releasedTextView)
    TextView releasedTextView;
    @Bind(R.id.topBarLayout)
    RelativeLayout topBarLayout;
    @Bind(R.id.plotOverviewTextView)
    TextView plotOverviewTextView;
    @Bind(R.id.trailerTitle)
    TextView trailerTitle;
    @Bind(R.id.movie_detail_trailer_container)
    LinearLayout movieDetailTrailerContainer;
    @Bind(R.id.tagText)
    TextView tagText;
    @Bind(R.id.taglineText)
    TextView taglineText;
    @Bind(R.id.tagLineDivider)
    View tagLineDivider;
    @Bind(R.id.imdbText)
    TextView imdbText;
    @Bind(R.id.productionCompaniesText)
    TextView productionCompaniesText;
    @Bind(R.id.productionCountriesText)
    TextView productionCountriesText;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.trailerCount)
    TextView trailerCount;
    @Bind(R.id.likeFab)
    FloatingActionButton likeFab;
    @Bind(R.id.scrollTrailer)
    HorizontalScrollView scrollTrailer;
    @Bind(R.id.reviewTitle)
    TextView reviewTitle;
    @Bind(R.id.movie_detail_review_container)
    LinearLayout movieDetailReviewContainer;
    @Bind(R.id.trailerCard)
    CardView trailerCard;
    @Bind(R.id.reviewCard)
    CardView reviewCard;

    private String apiKey;
    private String imdb, genre, runningTime, released, productionCompanies, productionCountries, tagline, overview, rating;
    private ArrayList<MovieVideoResultModel> videoList;
    private ArrayList<MovieReviewResults> reviewsList;
    private MovieVideoResultModel mMainTrailer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleExtras();
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        videoList = new ArrayList<>();
        reviewsList = new ArrayList<>();
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imdb = savedInstanceState.getString("imdb");
            genre = savedInstanceState.getString("genreText");
            runningTime = savedInstanceState.getString("runningTime");
            released = savedInstanceState.getString("released");
            productionCompanies = savedInstanceState.getString("productionCompanies");
            productionCountries = savedInstanceState.getString("productionCountries");
            tagline = savedInstanceState.getString("tagline");
            overview = savedInstanceState.getString("overview");
            rating = savedInstanceState.getString("rating");
            imageUrl = savedInstanceState.getString("imageUrl");
            mFavourite = savedInstanceState.getBoolean("mFavourite");
            videoList = savedInstanceState.getParcelableArrayList("videoList");
            reviewsList = savedInstanceState.getParcelableArrayList("reviewsList");

            imdbText.setText(imdb);
            tagText.setText(genre);
            runningTimeTextView.setText(runningTime);
            releasedTextView.setText(released);
            productionCompaniesText.setText(productionCompanies);
            productionCountriesText.setText(productionCountries);
            taglineText.setText(tagline);
            plotOverviewTextView.setText(overview);
            ratingTitle.setText(rating);
            if (videoList.size() != 0) {
                addTrailerViews(videoList);
            } else {
                trailerCard.setVisibility(View.GONE);
            }
            if (reviewsList.size() != 0) {
                addReviewViews(reviewsList);
            } else {
                reviewCard.setVisibility(View.GONE);
            }

            initHeader();
            if (mFavourite) {
                likeFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_selected));
            } else {
                likeFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));

            }
        } else {
            apiKey = BuildConfig.API_KEY;
            getPopularMovieApi = MovieApiGenerator.createService(GetPopularMovieApi.class);
            queryFavMovieInDb();
            getMovieDetails();
            initHeader();
        }
    }

    private void queryFavMovieInDb() {
        /* Get all objects with id 1 or intField greater than 5 */
        List<MovieResultsModel> movieResultsModelList = new RushSearch()
                .whereEqual("mId", model.getmId())
                .find(MovieResultsModel.class);

        if (movieResultsModelList != null && movieResultsModelList.size() > 0) {
            mFavourite = true;
            likeFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_selected));
        } else {
            likeFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        }
    }


    private void getMovieDetails() {
        getPopularMovieApi.getMovieDetails(model.getmId(), apiKey, new Callback<MovieDetailModel>() {
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
                    Logger.e("Movie id ", model.getmId() + "");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.e("Movie Detail Error" + error.getMessage().toString());
            }
        });
        getMovieTrailersForId(model);
        getMovieCredits();

        getMovieReviews(model.getmId());
    }

    private void getMovieReviews(String id) {
        getPopularMovieApi.getMovieReviews(id, apiKey, new Callback<MovieReviewModel>() {
            @Override
            public void success(MovieReviewModel movieReviewModel, Response response) {
                Logger.d("Reviews", "Success " + movieReviewModel.getResults().size());
                if (movieReviewModel.getResults().size() != 0) {
                    reviewsList.addAll(movieReviewModel.getResults());
                    addReviewViews(reviewsList);
                } else {
                    reviewCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.e("MovieReviews Api Error", error);
            }
        });
    }


    private void getMovieTrailersForId(MovieResultsModel model) {

        getPopularMovieApi.getVideoForId(model.getmId(), apiKey, new Callback<MovieVideoResponseModel>() {
            @Override
            public void success(MovieVideoResponseModel movieResponseModel, Response response) {
                Logger.d("Trailer list size is : ", movieResponseModel.getResults().size() + "");

                if (movieResponseModel.getResults().size() != 0) {
                    videoList.addAll(movieResponseModel.getResults());
                    addTrailerViews(videoList);
                } else {
                    trailerCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.e("Video id Error" + error.getMessage());
            }
        });
    }


    private void addTrailerViews(List<MovieVideoResultModel> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(MovieDetailActivity.this);
        trailerCount.setText(resultList.size() + " trailers");

        if (resultList != null && !resultList.isEmpty()) {
            mMainTrailer = resultList.get(0);
            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openYoutubeVideo(MovieDetailActivity.this, mMainTrailer.getKey());
                }
            });
            for (MovieVideoResultModel trailer : resultList) {
                final String key = trailer.getKey();
                final View trailerView = inflater.inflate(R.layout.list_item_trailer, movieDetailTrailerContainer, false);
                ImageView trailerImage = ButterKnife.findById(trailerView, R.id.trailer_poster_image_view);
                ImageView playImage = ButterKnife.findById(trailerView, R.id.play_trailer_image_view);
                playImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.openYoutubeVideo(MovieDetailActivity.this, key);
                    }
                });
                String url = "http://img.youtube.com/vi/" + key + "/0.jpg";
                Picasso.with(MovieDetailActivity.this).load(url).placeholder(R.drawable.ic_placeholder_movie)
                        .error(R.drawable.ic_placeholder_movie).into(trailerImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Logger.d("Success trailer");
                    }

                    @Override
                    public void onError() {
                        Logger.e("failure trailer");
                    }
                });


                movieDetailTrailerContainer.addView(trailerView);
            }
        }

    }


    private void addReviewViews(List<MovieReviewResults> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(MovieDetailActivity.this);

        for (MovieReviewResults review : resultList) {
            final View reviewView = inflater.inflate(R.layout.list_item_review, movieDetailReviewContainer, false);
            TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.list_item_review_author_text_view);
            TextView reviewContent = ButterKnife.findById(reviewView, R.id.list_item_review_content_text_view);
            reviewAuthor.setText(review.getAuthor());
            reviewContent.setText(review.getContent());
            movieDetailReviewContainer.addView(reviewView);
        }
    }


    private void getMovieCredits() {
        getPopularMovieApi.getMovieCredits(model.getmId(), apiKey, new Callback<MovieCreditsResponseModel>() {
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


    private void initHeader() {


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
                            likeFab.setBackgroundTintList(ColorStateList.valueOf(mutedD.getRgb()));
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

        likeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavourite) {
                    likeFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                    List<MovieResultsModel> movieResultsModelList = new RushSearch()
                            .whereEqual("mId", model.getmId())
                            .find(MovieResultsModel.class);
                    movieResultsModelList.get(0).delete(new RushCallback() {
                        @Override
                        public void complete() {
                            mFavourite = false;
                            Logger.d("rushdb", model.getTitle() + " got deleted");

                        }
                    });
                } else {
                    likeFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_selected));
                    model.save(new RushCallback() {
                        @Override
                        public void complete() {
                            mFavourite = true;
                            Logger.d("rushdb", model.getId());
                        }
                    });

                }


            }
        });

    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if (id == R.id.action_share) {
            Logger.d("Trailer share", "http://www.youtube.com/watch?v=" + mMainTrailer.getKey());
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi , checkout this awesome trailer : " + "http://www.youtube.com/watch?v=" + mMainTrailer.getKey());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        imdb = imdbText.getText().toString();
        genre = tagText.getText().toString();
        runningTime = runningTimeTextView.getText().toString();
        released = releasedTextView.getText().toString();
        productionCompanies = productionCompaniesText.getText().toString();
        productionCountries = productionCountriesText.getText().toString();
        tagline = taglineText.getText().toString();
        overview = plotOverviewTextView.getText().toString();
        rating = ratingTitle.getText().toString();

        outState.putParcelableArrayList("videoList", videoList);
        outState.putParcelableArrayList("reviewsList", reviewsList);
        outState.putBoolean("mFavourite", mFavourite);
        outState.putString("imageUrl", imageUrl);
        outState.putString("imdb", imdb);
        outState.putString("genreText", genre);
        outState.putString("runningTime", runningTime);
        outState.putString("released", released);
        outState.putString("productionCompanies", productionCompanies);
        outState.putString("productionCountries", productionCountries);
        outState.putString("tagline", tagline);
        outState.putString("overview", overview);
        outState.putString("rating", rating);
        for (String key : outState.keySet()) {
            Logger.d("Bundle Debug", key + " = \"" + outState.get(key) + "\"");
        }
        super.onSaveInstanceState(outState);

    }

}
