package techgravy.popularmovies.fragment;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.text.style.BulletSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by aditlal on 08/12/15.
 */
public class MovieDetailsFragment extends Fragment {


    String imageUrl, title, videoId1;
    MovieResultsModel model;
    boolean mFavourite = false;
    int circleColor;
    MovieDetailModel detailModel;
    GetPopularMovieApi getPopularMovieApi;
    @Bind(R.id.itemImage)
    ImageView itemImage;
    @Bind(R.id.likeFab)
    FloatingActionButton likeFab;
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
    @Bind(R.id.scrollTrailer)
    HorizontalScrollView scrollTrailer;
    @Bind(R.id.trailerCount)
    TextView trailerCount;
    @Bind(R.id.trailerCard)
    CardView trailerCard;
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
    @Bind(R.id.reviewTitle)
    TextView reviewTitle;
    @Bind(R.id.movie_detail_review_container)
    LinearLayout movieDetailReviewContainer;
    @Bind(R.id.reviewCard)
    CardView reviewCard;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @Bind(R.id.movieSelectionText)
    TextView movieSelectionText;


    private String apiKey;
    private String imdb, genre, runningTime, released, productionCompanies, productionCountries, tagline, overview, rating;
    private ArrayList<MovieVideoResultModel> videoList;
    private ArrayList<MovieReviewResults> reviewsList;
    private MovieVideoResultModel mMainTrailer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
        handleArgs();
    }

    public static MovieDetailsFragment newInstance(Bundle bundle) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            nestedScrollView.setVisibility(View.VISIBLE);
            movieSelectionText.setVisibility(View.GONE);
            initHeader();
            videoList = new ArrayList<>();
            reviewsList = new ArrayList<>();
            apiKey = BuildConfig.API_KEY;
            getPopularMovieApi = MovieApiGenerator.createService(GetPopularMovieApi.class);
            queryFavMovieInDb();
            getMovieDetails();
        }


        return rootView;
    }

    private void handleArgs() {

        if (getArguments() != null) {
            imageUrl = getArguments().getString("imageUrl");
            model = getArguments().getParcelable("movieObject");
            Logger.d("IMAGE", imageUrl);
        }
    }


    private void initHeader() {
        Picasso.with(getActivity()).load(imageUrl).transform(PaletteTransformation.instance()).into(itemImage,
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
                                Window window = getActivity().getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(vibrantDark.getRgb());
                            }
                            if (vibrant == null) {
                                topBarLayout.setBackgroundColor(vibrantDark.getRgb());
                            }
                            //collapsingToolbar.setContentScrim(new ColorDrawable(vibrantDark.getRgb()));
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
        //  setupToolBar();
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

  /*  private void setupToolBar() {
        collapsingToolbar.setTitle(title);
    }
    */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        trailerCount.setText(resultList.size() + " trailers");

        if (resultList != null && !resultList.isEmpty()) {
            mMainTrailer = resultList.get(0);
            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.openYoutubeVideo(getActivity(), mMainTrailer.getKey());
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
                        IntentUtils.openYoutubeVideo(getActivity(), key);
                    }
                });
                String url = "http://img.youtube.com/vi/" + key + "/0.jpg";
                Picasso.with(getActivity()).load(url).placeholder(R.drawable.ic_placeholder_movie)
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

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

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


}
