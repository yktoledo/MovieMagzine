package org.app.anand.moviemagzine2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.app.anand.moviemagzine2.Adapter.MoviesAdapter;
import org.app.anand.moviemagzine2.Adapter.UpcomingAdapter;
import org.app.anand.moviemagzine2.Auth.InitActivity;
import org.app.anand.moviemagzine2.Auth.LoginActivity;
import org.app.anand.moviemagzine2.Auth.SignInActivity;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.Model.Movies;
import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.R;
import org.app.anand.moviemagzine2.UserProfile.UserProfile;
import org.app.anand.moviemagzine2.categories.Categories;
import org.app.anand.moviemagzine2.preferences.Preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{




    public static SharedPreferences spAuthentication;
    public static final String END_POINT="https://api.themoviedb.org/3";
    List<Result> myMovies,popMovies,topMovies,nowMovies, upComingMovies;
    ArrayList<String> movieListDataHeader;
    //ExpandableListView expListViewMovies;
    RecyclerView movies,popular,top,now, up;
    Button nowplayingbtn, popularbtn, topratedbtn, upComing;
    EditText editText;
    MenuItem action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        action = (MenuItem) findViewById(R.id.action_search);
        setTitle("MovieMeridian - Home");


        upComing = (Button) findViewById(R.id.upComing);
        upComing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Categories.class);
                i.putExtra("expression", "upcoming");
                startActivity(i);
            }
        });
        nowplayingbtn = (Button) findViewById(R.id.nowplayBtn);
        nowplayingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Categories.class);
                i.putExtra("expression", "nowplaying");
                startActivity(i);
            }
        });
        popularbtn = (Button) findViewById(R.id.popularBtn);
        popularbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Categories.class);
                i.putExtra("expression", "popular");
                startActivity(i);
            }
        });
        topratedbtn = (Button) findViewById(R.id.topratedBtn);
        topratedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Categories.class);
                i.putExtra("expression", "toprated");
                startActivity(i);
            }
        });

        movieListDataHeader = new ArrayList<>();
        movieListDataHeader.add("List of Movies");
        movies = (RecyclerView) findViewById(R.id.rvMovies);
        popular = (RecyclerView) findViewById(R.id.rvPopularMovies);
        top = (RecyclerView) findViewById(R.id.rvTopMovies);
        now = (RecyclerView) findViewById(R.id.rvNowPlay);
        up = (RecyclerView) findViewById(R.id.rvUpComingMovies);

        movies.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        if(isOnline()){
            getData();
        } else {
            Toast.makeText(this, "You are offline, no data available", Toast.LENGTH_LONG).show();
        }

        getData();
        //-----------------navigation-------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_preferences) {
                    // Handle the preference  action
                    Intent i = new Intent(getApplicationContext(), Preferences.class);
                    MainActivity.this.finish();
                    startActivity(i);
                } else if (id == R.id.nav_about) {


                    SharedPreferences spAuthentication = getApplicationContext().getSharedPreferences("org.app.anand.moviemagzine", Context.MODE_PRIVATE);
                    String startActivity = spAuthentication.getString("org.app.anand.moviemagzine.authentication.registered", "No");
                    if(startActivity.equals("No")){
                        Log.d("Init", "Redirecting to Register");
                        Intent Login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(Login);

                    } else if(startActivity.equals("Yes")) {
                        Intent i = new Intent(getApplicationContext(), UserProfile.class);
                        startActivity(i);
                    }

                } else if (id == R.id.nav_home) {
                    // Handle the Home action

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }else if (id == R.id.nav_login) {
                    // Handle the Home action
                    Intent i = new Intent(getApplicationContext(), InitActivity.class);
                    startActivity(i);
                }else if (id == R.id.nav_logout) {
                    FirebaseAuth mAuth = cloudDataSync.getmAuth();
                   if(mAuth!=null) {
                       mAuth.signOut();

                       Intent i = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(i);
                       Toast.makeText(MainActivity.this,"User Logged Out",Toast.LENGTH_LONG).show();
                   } else {
                       Toast.makeText(MainActivity.this,"Logout not applicable.",Toast.LENGTH_LONG).show();
                   }

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
 //-----------------------navigation---------------
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private class SearchViewExpandListener implements MenuItemCompat.OnActionExpandListener {

        private Context context;

        public SearchViewExpandListener (Context c) {
            context = c;
        }

        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
            return false;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(false);
            return false;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // See above
        MenuItemCompat.setOnActionExpandListener(searchItem, new SearchViewExpandListener(this));
        MenuItemCompat.setActionView(searchItem, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(MainActivity.this, "You searched " + s, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Search.class);
                i.putExtra("data",s);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You clicked",Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }
    //------------------navigation outside oncreate------------

    protected boolean isOnline() {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else
            return false;
    }
//--------------------------

//----------------------------
private void getData(){

    MoviesAPI.Factory.getInstance().getTop().enqueue(new Callback<Movies>() {
        @Override
        public void onResponse(Call<Movies> call, Response<Movies> response) {
           MoviesAdapter adapter = new MoviesAdapter(deleteNull(response.body().getResults()), getApplicationContext());
            top.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            top.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<Movies> call, Throwable t) {

        }
    });
    MoviesAPI.Factory.getInstance().getNow().enqueue(new Callback<Movies>() {
        @Override
        public void onResponse(Call<Movies> call, Response<Movies> response) {
            MoviesAdapter adapter = new MoviesAdapter(deleteNull(response.body().getResults()), getApplicationContext());
            now.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            now.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<Movies> call, Throwable t) {

        }
    });
    MoviesAPI.Factory.getInstance().getPop().enqueue(new Callback<Movies>() {
        @Override
        public void onResponse(Call<Movies> call, Response<Movies> response) {


            MoviesAdapter adapter = new MoviesAdapter(deleteNull(response.body().getResults()), getApplicationContext());
            popular.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            popular.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<Movies> call, Throwable t) {

        }
    });
    MoviesAPI.Factory.getInstance().getUpcoming().enqueue(new Callback<Movies>() {
        @Override
        public void onResponse(Call<Movies> call, Response<Movies> response) {
            UpcomingAdapter adapter = new UpcomingAdapter(deleteNull(response.body().getResults()), getApplicationContext());
            up.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            up.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<Movies> call, Throwable t) {

        }
    });
}


private List<Result> deleteNull(List<Result> list){

    Iterator<Result> m = list.iterator();
    List<Result> resultList=new ArrayList<>();
    while(m.hasNext()){
        Result temp = m.next();
        String poster = temp.getBackdropPath();
        if(poster!=null){
            resultList.add(temp);
        }
    }
    return resultList;
}

    //fin de la clase
    }



