package techgravy.popularmovies;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;
import techgravy.popularmovies.models.MovieCreditsCastModel;
import techgravy.popularmovies.models.MovieCreditsCrewModel;
import techgravy.popularmovies.models.MovieCreditsResponseModel;
import techgravy.popularmovies.models.MovieDetailModel;
import techgravy.popularmovies.models.MovieGenres;
import techgravy.popularmovies.models.MovieProductionCompanies;
import techgravy.popularmovies.models.MovieProductionCountries;
import techgravy.popularmovies.models.MovieResponseModel;
import techgravy.popularmovies.models.MovieResultsModel;
import techgravy.popularmovies.models.MovieReviewModel;
import techgravy.popularmovies.models.MovieSpokenLanguage;
import techgravy.popularmovies.models.MovieVideoResponseModel;
import techgravy.popularmovies.models.MovieVideoResultModel;
import techgravy.popularmovies.utils.LogLevel;
import techgravy.popularmovies.utils.Logger;

/**
 * Created by aditlal on 26/09/15.
 */
public class MainApplication extends Application {

    public static MainApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Logger
                .init()               // default PRETTYLOGGER or use just init()
                .setMethodCount(4)            // default 2
                .hideThreadInfo()             // default shown
                .setLogLevel(LogLevel.FULL);  // default LogLevel.FULL
        List<Class<? extends Rush>> classes = new ArrayList<>();
        // Add classes
        classes.add(MovieCreditsCrewModel.class);
        classes.add(MovieCreditsCastModel.class);
        classes.add(MovieCreditsResponseModel.class);
        classes.add(MovieDetailModel.class);
        classes.add(MovieGenres.class);
        classes.add(MovieProductionCompanies.class);
        classes.add(MovieProductionCountries.class);
        classes.add(MovieResponseModel.class);
        classes.add(MovieResultsModel.class);
        classes.add(MovieReviewModel.class);
        classes.add(MovieResultsModel.class);
        classes.add(MovieSpokenLanguage.class);
        classes.add(MovieVideoResponseModel.class);
        classes.add(MovieVideoResultModel.class);



        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext(),classes);
        RushCore.initialize(config);



    }

    public static MainApplication getApplication() {
        return application;
    }


}
