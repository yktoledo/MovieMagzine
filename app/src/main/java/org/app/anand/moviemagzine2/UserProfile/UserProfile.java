package org.app.anand.moviemagzine2.UserProfile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.app.anand.moviemagzine2.Auth.InitActivity;
import org.app.anand.moviemagzine2.Auth.LoginActivity;
import org.app.anand.moviemagzine2.Auth.SignInActivity;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.MainActivity;
import org.app.anand.moviemagzine2.R;
import org.app.anand.moviemagzine2.Search;
import org.app.anand.moviemagzine2.preferences.Preferences;

public class UserProfile extends AppCompatActivity{
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static SharedPreferences spAuthentication;
    Button btn1, btn2, btn3;
    Tab1 tab1;
    Tab2 tab2;
    Tab3 tab3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if(cloudDataSync.getmAuth().getCurrentUser()!=null)
            this.setTitle("Profile "+cloudDataSync.getmAuth().getCurrentUser().getEmail());
        tab1 = new Tab1();
        tab2 = new Tab2();
        tab3 = new Tab3();
        spAuthentication = getApplicationContext().getSharedPreferences("org.app.anand.moviemagzine", Context.MODE_PRIVATE);
        String startActivity = spAuthentication.getString("org.app.anand.moviemagzine.authentication.registered", "No");
        if(startActivity.equals("No")){
            Log.d("Init", "Redirecting to Register");
            Intent Login = new Intent(this, SignInActivity.class);
            startActivity(Login);
            UserProfile.this.finish();
        } else if(startActivity.equals("Yes")) {
            /*mAuthListener = new FirebaseAuth.AuthStateListener(){

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d("Profile:", "onAuthStateChanged:signed_in:" + user.getUid());

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id,tab1).commit();
                    } else {
                        // User is signed out
                        Log.d(":Profile:", "onAuthStateChanged:signed_out");
                        Intent profile = new Intent(UserProfile.this, MainActivity.class);
                        startActivity(profile);
                        Toast.makeText(UserProfile.this,"Not logged-in, redirected to home.",Toast.LENGTH_LONG).show();

                    }
                }
            };*/
            if(cloudDataSync.getmAuth().getCurrentUser()!=null){
                Log.d("Profile:", "onAuthStateChanged:signed_in:" + cloudDataSync.getmAuth().getCurrentUser().getUid());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id,tab1).commit();
            }else {
                Log.d(":Profile:", "onAuthStateChanged:signed_out");
                UserProfile.this.finish();
                Intent profile = new Intent(UserProfile.this, SignInActivity.class);
                startActivity(profile);
                Toast.makeText(UserProfile.this,"Not logged-in, redirected to sign-in.",Toast.LENGTH_LONG).show();
            }



        }

        btn1= (Button) findViewById(R.id.button);
        btn2= (Button) findViewById(R.id.button2);
        btn3= (Button) findViewById(R.id.button3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show1();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show2();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show3();
            }
        });



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
                    UserProfile.this.finish();
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

                }else if (id == R.id.nav_home) {
                    // Handle the Home action
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    UserProfile.this.finish();
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
                        UserProfile.this.finish();
                        startActivity(i);
                        Toast.makeText(UserProfile.this,"User Logged Out",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UserProfile.this,"Logout not applicable.",Toast.LENGTH_LONG).show();
                    }

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //-----------------------navigation---------------
    }



    public void show1(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id,tab1).commit();
    }
    public void show2(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id,tab2).commit();
    }
    public void show3(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id,tab3).commit();
    }




    //--------------------------------------------------------------


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

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return false;
//    }

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
        MenuItemCompat.setOnActionExpandListener(searchItem, new UserProfile.SearchViewExpandListener(this));
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
                //Toast.makeText(MainActivity.this, "You clicked",Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }



    //--------------------------------------------------------------
}
