package techgravy.popularmovies.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import techgravy.popularmovies.models.MovieCreditsResponseModel;
import techgravy.popularmovies.models.MovieDetailModel;
import techgravy.popularmovies.models.MovieResponseModel;
import techgravy.popularmovies.models.MovieVideoResponseModel;

/**
 * Created by aditlal on 26/09/15.
 */
public interface GetPopularMovieApi {

    @GET("/discover/movie?sort_by=popularity.desc")
    void getMoviesByPopularity(@Query("page") int page, @Query("api_key") String api_key, Callback<MovieResponseModel> cb);

    @GET("/movie/top_rated")
    void getMoviesByRating(@Query("page") int page, @Query("api_key") String api_key, Callback<MovieResponseModel> cb);

    @GET("/movie/{id}/videos")
    void getVideoForId(@Path("id") String movieId, @Query("api_key") String api_key, Callback<MovieVideoResponseModel> cb);

    @GET("/movie/{id}")
    void getMovieDetails(@Path("id") String movieId, @Query("api_key") String api_key, Callback<MovieDetailModel> cb);

    @GET("/movie/{id}/credits")
    void getMovieCredits(@Path("id") String movieId, @Query("api_key") String api_key, Callback<MovieCreditsResponseModel> cb);

}
