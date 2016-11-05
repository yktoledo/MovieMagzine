package org.app.anand.moviemagzine2;

import org.app.anand.moviemagzine2.Model.Movie;
import org.app.anand.moviemagzine2.Model.Movies;
import org.app.anand.moviemagzine2.Model.Review;

import java.util.Map;


import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;


import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by User on 10/18/2016.
 */

public interface MoviesAPI {
    /*
    @GET("/search/movie?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US&query=will")
    public void getFeed(Callback<Movies> response);

    @GET("/movie/popular?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    public void getPopular(Callback<Movies> response);

    @GET("/movie/top_rated?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    public void getTopRated(Callback<Movies> response);

    @GET("/movie/now_playing?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    public void getNowPlay(Callback<Movies> response);

    @GET("/movie/upcoming?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    public void getUpcoming(Callback<Movies> response);

    @GET("/search/movie?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US&query=will")
    //Call<List<Movie>> getSearch(Callback<Movie> response, @QueryMap Map<String, String> options);
    public void getSearch(Callback<Result> response);
*/
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("search/movie")
    Call<Movies> getS(@QueryMap Map<String, String> options);
    @GET("movie/top_rated?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    Call<Movies> getTop();
    @GET("movie/top_rated")
    Call<Movies> getTopFrag(@QueryMap Map<String, String> options);
    @GET("movie/popular?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    Call<Movies> getPop();
    @GET("movie/popular")//?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US
    Call<Movies> getPopFrag(@QueryMap Map<String, String> options);
    @GET("movie/now_playing?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    Call<Movies> getNow();
    @GET("movie/now_playing")
    Call<Movies> getNowFrag(@QueryMap Map<String, String> options);
    @GET("movie/upcoming?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    Call<Movies> getUpcoming();
    @GET("movie/upcoming")
    Call<Movies> getUpcomingFrag(@QueryMap Map<String, String> options);
    @GET("movie/{id}?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    Call<Movie>getMovieDetails(@Path("id") int movieId);
    @GET("movie/{movie_id}/reviews?api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US")
    Call<Review>getMovieReviews(@Path("movie_id") int movieId);

    //------------------------------------
    class Factory{
        private static MoviesAPI service;

        public static MoviesAPI getInstance(){
            if (service == null){
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();

                service = retrofit.create(MoviesAPI.class);
                return service;
            }else {
                return service;
            }

        }
    }
    //------------------------------

}
