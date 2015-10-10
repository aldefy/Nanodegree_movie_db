package techgravy.popularmovies;

import android.app.Application;

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
                .setMethodCount(3)            // default 2
                .hideThreadInfo()             // default shown
                .setLogLevel(LogLevel.FULL);  // default LogLevel.FULL
    }

    public static MainApplication getApplication() {
        return application;
    }
}
