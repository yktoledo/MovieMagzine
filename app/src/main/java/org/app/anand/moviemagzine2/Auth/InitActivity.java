package org.app.anand.moviemagzine2.Auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.app.anand.moviemagzine2.R;

public class InitActivity extends AppCompatActivity {
    public static SharedPreferences spAuthentication,userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        spAuthentication = getApplicationContext().getSharedPreferences("org.app.anand.moviemagzine", Context.MODE_PRIVATE);
        String startActivity = spAuthentication.getString("org.app.anand.moviemagzine.authentication.registered", "No");
        if(startActivity.equals("No")){
            Log.d("Init", "Redirecting to Register");
            Intent Login = new Intent(this, SignInActivity.class);
            startActivity(Login);
            InitActivity.this.finish();
        } else if(startActivity.equals("Yes")) {
            Log.d("Init","Redirecting to Login");
            Intent Home = new Intent(this, SignInActivity.class);
            startActivity(Home);
            InitActivity.this.finish();
        }
    }
}
