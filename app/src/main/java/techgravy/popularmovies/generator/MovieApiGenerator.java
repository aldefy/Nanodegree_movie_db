package techgravy.popularmovies.generator;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import techgravy.popularmovies.MainApplication;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieApiGenerator {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    MainApplication application;

    // No need to instantiate this class.
    private MovieApiGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}