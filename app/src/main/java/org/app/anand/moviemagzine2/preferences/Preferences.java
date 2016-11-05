package org.app.anand.moviemagzine2.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.app.anand.moviemagzine2.Auth.InitActivity;
import org.app.anand.moviemagzine2.Auth.LoginActivity;
import org.app.anand.moviemagzine2.Auth.SignInActivity;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.MainActivity;
import org.app.anand.moviemagzine2.R;
import org.app.anand.moviemagzine2.Search;
import org.app.anand.moviemagzine2.UserProfile.UserProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Preferences extends AppCompatActivity {
CheckBox action, adventure, animation, comedy, crime, documentary, drama, family,fantasy, history,
             horror, music, mystery, romance, scienceFinction, tvMovie, thriller,war, western;
    Button saveBtn, backup, restorebtn;
    public static SharedPreferences dataPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        action= (CheckBox) findViewById(R.id.action_id);
        adventure= (CheckBox) findViewById(R.id.adventure_id);
        animation= (CheckBox) findViewById(R.id.animation_id);
        comedy= (CheckBox) findViewById(R.id.comedy_id);
        crime= (CheckBox) findViewById(R.id.crime_id);
        documentary= (CheckBox) findViewById(R.id.documentary_id);
        drama= (CheckBox) findViewById(R.id.drama_id);
        family= (CheckBox) findViewById(R.id.family_id);
        fantasy= (CheckBox) findViewById(R.id.fantasy_id);
        history= (CheckBox) findViewById(R.id.history_id);
        horror= (CheckBox) findViewById(R.id.horror_id);
        music= (CheckBox) findViewById(R.id.music_id);
        mystery= (CheckBox) findViewById(R.id.mystery_id);
        romance= (CheckBox) findViewById(R.id.romance_id);
        scienceFinction= (CheckBox) findViewById(R.id.science_Fiction_id);
        tvMovie= (CheckBox) findViewById(R.id.tv_Movie_id);
        thriller= (CheckBox) findViewById(R.id.thriller_id);
        war= (CheckBox) findViewById(R.id.war_id);
        western= (CheckBox) findViewById(R.id.western_id);
    saveBtn = (Button) findViewById(R.id.saveSubscription_id);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
backup= (Button) findViewById(R.id.backup_id);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backupfunction();

            }
        });
        restorebtn= (Button) findViewById(R.id.restore_id);
        restorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreFuntion();
            }
        });
        if(cloudDataSync.getmAuth().getCurrentUser()!=null){
            setTitle("Preferences of "+cloudDataSync.getmAuth().getCurrentUser().getEmail());
            showData();

        }else {
            Log.d(":Profile:", "onAuthStateChanged:signed_out");
            Preferences.this.finish();
            Intent profile = new Intent(Preferences.this, SignInActivity.class);
            startActivity(profile);
            Toast.makeText(Preferences.this,"Not logged-in, redirected to sign-in.",Toast.LENGTH_LONG).show();
        }


        //-----------------navigation-------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    startActivity(i);
                }else if (id == R.id.nav_logout) {
                    FirebaseAuth mAuth = cloudDataSync.getmAuth();
                    if(mAuth!=null) {
                        mAuth.signOut();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        Toast.makeText(Preferences.this,"User Logged Out",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Preferences.this,"Logout not applicable.",Toast.LENGTH_LONG).show();
                    }

                }else if (id == R.id.nav_login) {
                    // Handle the Home action
                    Intent i = new Intent(getApplicationContext(), InitActivity.class);
                    startActivity(i);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //-----------------------navigation---------------
    }
public void showData(){
    dataPreferences = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    Set userPreferencesList = dataPreferences.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(),  new HashSet<String>());
    List<String> list = new ArrayList<>(userPreferencesList);
    if (list.contains(action.getText()))
        action.setChecked(true);
    if (list.contains(adventure.getText()))
        adventure.setChecked(true);
    if (list.contains(animation.getText()))
       animation.setChecked(true);
    if (list.contains(comedy.getText()))
        comedy.setChecked(true);
    if (list.contains(crime.getText()))
        crime.setChecked(true);
    if (list.contains(documentary.getText()))
        documentary.setChecked(true);
    if (list.contains(drama.getText()))
        drama.setChecked(true);
    if (list.contains(family.getText()))
        family.setChecked(true);
    if (list.contains(fantasy.getText()))
        fantasy.setChecked(true);
    if (list.contains(history.getText()))
        history.setChecked(true);
    if (list.contains(horror.getText()))
        horror.setChecked(true);
    if (list.contains(music.getText()))
        music.setChecked(true);
    if (list.contains(mystery.isChecked()))
        mystery.setChecked(true);
    if (list.contains(romance.isChecked()))
        romance.setChecked(true);
    if (list.contains(scienceFinction.getText()))
        scienceFinction.setChecked(true);
    if (list.contains(tvMovie.getText()))
        tvMovie.setChecked(true);
    if (list.contains(thriller.getText()))
        thriller.setChecked(true);
    if (list.contains(war.getText()))
        war.setChecked(true);
    if (list.contains(western.getText()))
        western.setChecked(true);
}
    private void saveData() {

            List<String> configList = new ArrayList<String>();





        if (action.isChecked())
            configList.add(action.getText().toString());
        if (adventure.isChecked())
            configList.add(adventure.getText().toString());
        if (animation.isChecked())
            configList.add(animation.getText().toString());
        if (comedy.isChecked())
            configList.add(comedy.getText().toString());
        if (crime.isChecked())
            configList.add(crime.getText().toString());
        if (documentary.isChecked())
            configList.add(documentary.getText().toString());
        if (drama.isChecked())
            configList.add(drama.getText().toString());
        if (family.isChecked())
            configList.add(family.getText().toString());
        if (fantasy.isChecked())
            configList.add(fantasy.getText().toString());
        if (history.isChecked())
            configList.add(history.getText().toString());
        if (horror.isChecked())
            configList.add(horror.getText().toString());
        if (music.isChecked())
            configList.add(music.getText().toString());
        if (mystery.isChecked())
            configList.add(mystery.getText().toString());
        if (romance.isChecked())
            configList.add(romance.getText().toString());
        if (scienceFinction.isChecked())
            configList.add(scienceFinction.getText().toString());
        if (tvMovie.isChecked())
            configList.add(tvMovie.getText().toString());
        if (thriller.isChecked())
            configList.add(thriller.getText().toString());
        if (war.isChecked())
            configList.add(war.getText().toString());
        if (western.isChecked())
            configList.add(western.getText().toString());

        Set<String> configListSet = new HashSet<String>(configList);
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("UserPreferences", getApplicationContext().MODE_PRIVATE).edit();
        editor.putStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(), configListSet).commit();
        Toast.makeText(this, "Preferences Saved", Toast.LENGTH_SHORT).show();
    }

public void backupfunction(){

    dataPreferences = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    Set userPreferencesList = dataPreferences.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(),  new HashSet<String>());
    List<String> preferencesList = new ArrayList<>(userPreferencesList);

    SharedPreferences prefs = getApplicationContext().getSharedPreferences("key", getApplicationContext().MODE_PRIVATE);


    Set configTemp = prefs.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), new HashSet<String>());
    List<String> favoritesList = new ArrayList<String>(configTemp);


    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    firebaseDatabase.getReference().child("backaupPreferences").child(cloudDataSync.getmAuth().getCurrentUser().getUid()).child("favorites").setValue(favoritesList);
    firebaseDatabase.getReference().child("backaupPreferences").child(cloudDataSync.getmAuth().getCurrentUser().getUid()).child("preferences").setValue(preferencesList);
    Toast.makeText(this, "Backup Done", Toast.LENGTH_SHORT).show();
}
    public void restoreFuntion() {



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference favo = firebaseDatabase.getReference().child("backaupPreferences").child(cloudDataSync.getmAuth().getCurrentUser().getUid()).child("favorites");
        favo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<List<String>> favoritesList = new GenericTypeIndicator<List<String>>(){};
                List t = dataSnapshot.getValue(favoritesList);
                Set<String> favoritesListSet = new HashSet<String>(t);
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("key", getApplicationContext().MODE_PRIVATE).edit();
                editor.putStringSet(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), favoritesListSet).commit();


                Toast.makeText(Preferences.this, "Restored", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       DatabaseReference preferencesRef = firebaseDatabase.getReference().child("backaupPreferences").child(cloudDataSync.getmAuth().getCurrentUser().getUid()).child("preferences");
        preferencesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> preferencesList = new GenericTypeIndicator<List<String>>(){};
                List t = dataSnapshot.getValue(preferencesList);
                Set<String> preferencesListSet = new HashSet<String>(t);
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("UserPreferences", getApplicationContext().MODE_PRIVATE).edit();
                editor.putStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(), preferencesListSet).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        MenuItemCompat.setOnActionExpandListener(searchItem, new Preferences.SearchViewExpandListener(this));
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

            }
        });
        return true;
    }
    //------------------navigation outside oncreate------------
}
