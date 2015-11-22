package techgravy.popularmovies;

import android.app.Application;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.RushCore;
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

        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        RushCore.initialize(config);


    }

    public static MainApplication getApplication() {
        return application;
    }


}
