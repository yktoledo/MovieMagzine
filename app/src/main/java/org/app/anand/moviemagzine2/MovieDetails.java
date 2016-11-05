package org.app.anand.moviemagzine2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.app.anand.moviemagzine2.Adapter.ReviewAdapter;
import org.app.anand.moviemagzine2.Alarm.AlarmReceiver;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.Model.Movie;
import org.app.anand.moviemagzine2.Model.Results;
import org.app.anand.moviemagzine2.Model.Review;
import org.app.anand.moviemagzine2.R;
import org.app.anand.moviemagzine2.UserProfile.Tab2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.edit;

public class MovieDetails extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    ImageView poster;
    TextView posterDetails;
    ArrayList<String> movieListDataHeader;
    ExpandableListView expListViewMovies;
    YouTubePlayerView trailerPlayer;
    CheckBox checkBox;
    private String MyPREFERENCES = "key";
    private Set<String> currentTasks;
    String trailerID,uri="";
    static Movie movie;
    Intent alertIntent;
    String YoutubeAPIKey="AIzaSyDx-MDXhjIe015fy0sECEksbz0DtHoXIAU";//AIzaSyADvSwy4Rcs4mAHTBd20Iox-QzzxVjrF2M

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent i = getIntent();
        final String movie_id = String.valueOf(i.getIntExtra("movieId", 0));

         trailerID = i.getStringExtra("ID");
        getData(i.getIntExtra("movieId", 0));
        getReviews(i.getIntExtra("movieId", 0));
        setTitle("MovieMeridian - Movie Details");






        Button postReview = (Button) findViewById(R.id.reviewPost);
        postReview.setVisibility(View.GONE);
        poster= (ImageView) findViewById(R.id.posterImg);
        posterDetails= (TextView) findViewById(R.id.movieDetails);
        trailerPlayer = (YouTubePlayerView) findViewById(R.id.trailerView);
        trailerPlayer.initialize(YoutubeAPIKey, this);
        expListViewMovies = (ExpandableListView) findViewById(R.id.userReviews);
        movieListDataHeader = new ArrayList<>();
        movieListDataHeader.add("List of Reviews");


        checkBox = (CheckBox) findViewById(R.id.checkbox_id);
        if(cloudDataSync.getmAuth().getCurrentUser()==null)
            checkBox.setVisibility(View.GONE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && cloudDataSync.getmAuth().getCurrentUser()!=null){
                    updateConfig(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), movie_id,1);
                    Toast.makeText(MovieDetails.this, "Checked", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(MovieDetails.this, "Unchecked", Toast.LENGTH_SHORT).show();
                    updateConfig(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), movie_id,2);
                }
            }
        });
        if(cloudDataSync.getmAuth().getCurrentUser()!=null)
            checkIfFallow(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), movie_id);

    }

    private void getData(int id){
        Log.d("id dentro de getdata", ""+id);

        MoviesAPI.Factory.getInstance().getMovieDetails(id).enqueue(new Callback<Movie>() {
        @Override
        public void onResponse(Call<Movie> call, Response<Movie> response) {
            //posterDetails.setText(response.body().getTitle());
            if (response.body() != null) {
                movie = response.body();
                MovieDetails.this.setTitle("MovieMeridian - Details:"+response.body().getTitle());
                posterDetails.setText("Title: " + response.body().getTitle() + "\n"
                        + "Genre: " + response.body().getGenres().get(0).getName() + "\n"
                        + "Revenue: " + response.body().getRevenue() + "\n"
                        + "Release Date: " + response.body().getReleaseDate());
                Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500" + response.body().getPosterPath()).into(poster);
            }
        }

        @Override
        public void onFailure(Call<Movie> call, Throwable t) {

        }
    });
    }

    private void getReviews(int id){
        MoviesAPI.Factory.getInstance().getMovieReviews(id).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.body() != null) {
                    List<Results> myReviews=response.body().getResults();
                    HashMap<String, List<Results>> hmMovies = new HashMap<String, List<Results>>();
                    hmMovies.put(movieListDataHeader.get(0),myReviews);
                    ReviewAdapter listAdaptor = new ReviewAdapter(MovieDetails.this, movieListDataHeader, hmMovies);
                    expListViewMovies.setAdapter(listAdaptor);
                    expListViewMovies.expandGroup(0);
                } else
                    Toast.makeText(MovieDetails.this,"No reviews",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {

            }
        });
    }

//--------------------------------------


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateConfig(String userKey, String data, int op){
        Log.d("dias restante", "kkkkkk");
        SharedPreferences prefs = this.getSharedPreferences(MyPREFERENCES, this.MODE_PRIVATE);
        Set configTemp = prefs.getStringSet(userKey, new HashSet<String>());
        List<String> configList = new ArrayList<String>(configTemp);

        if(!configList.contains(data) && op==1){
            configList.add(data);
            //------------------
            String date[] = movie.getReleaseDate().split("-");
            Tab2 obj = new Tab2();
            int rd=obj.remainingDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            Log.d("dias restante", "kkkkkk"+rd);
            if(rd>0)
            setNotitication(data);
            //------------------
        }

        else if(configList.contains(data) && op==2){
            configList.remove(data);
            cancel();
        }

        Set<String> configListSet = new HashSet<String>(configList);
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE).edit();
        editor.putStringSet(userKey, configListSet).commit();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel((AlarmManager.OnAlarmListener) alertIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }
    public void setNotitication(String movieid) {
        String date[] = movie.getReleaseDate().split("-");
        Log.d("date", date[0]+" "+date[1]+" "+date[2] );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);



        alertIntent = new Intent(this, AlarmReceiver.class);
        alertIntent.putExtra("peliId", movieid);
        alertIntent.putExtra("title", movie.getTitle());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent
                .getBroadcast(this,1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT ));


    }

    private void checkIfFallow(String userKey, String data){
        SharedPreferences prefs = this.getSharedPreferences(MyPREFERENCES, this.MODE_PRIVATE);
        Set configTemp = prefs.getStringSet(userKey, new HashSet<String>());
        List<String> configList = new ArrayList<String>(configTemp);

           if (configList.contains(data)){
               checkBox.setChecked(true);
               Log.d("chk", "kkkkkkkkkkkkkkkk");
           }



    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(trailerID);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


//---------------------------------------
}
/*HashMap<String, List<Result>> hmMovies = new HashMap<String, List<Result>>();
                    hmMovies.put(movieListDataHeader.get(0),myMovies);
                    MovieAdapter listAdaptor = new MovieAdapter(MainActivity.this, movieListDataHeader, hmMovies);
                    expListViewMovies.setAdapter(listAdaptor);
                    MoviesAdapter adapter = new MoviesAdapter(MainActivity.this,myMovies);
                    movies.setAdapter(adapter);*/