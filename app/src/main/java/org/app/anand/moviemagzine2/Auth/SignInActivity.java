package org.app.anand.moviemagzine2.Auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.app.anand.moviemagzine2.*;
import org.app.anand.moviemagzine2.MainActivity;
import org.app.anand.moviemagzine2.UserProfile.UserProfile;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SignInActivity extends AppCompatActivity {

    EditText uname,pass;
    Button signon;
    private FirebaseAuth mAuth;
    TextView txt_signup;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_signup = (TextView) findViewById(R.id.txt_singUp_id);
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                SignInActivity.this.finish();
            }
        });
        mAuth = cloudDataSync.getmAuth();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                    Toast.makeText(SignInActivity.this,"User sign-in success!",Toast.LENGTH_LONG).show();
            }
        };
        uname = (EditText) findViewById(R.id.signInEmail);
        pass = (EditText) findViewById(R.id.signInPassword);
        signon = (Button) findViewById(R.id.email_signIn_button);

        signon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser(){

        String user = uname.getText().toString();
        String password = pass.getText().toString();
        Intent loginIntent = new Intent();
        loginIntent.putExtra("Username",user);
        loginIntent.putExtra("Password",password);
        loginIntent.putExtra("Flow","UserLogin");
        Log.d("signIn","Starting logging");
        new cloudDataSync(loginIntent,SignInActivity.this);
        SignInActivity.this.finish();
    }


}

/*if (user != null && password != null) {
            if (user.length() > 0 && pass.length() > 0) {
                if (!user.contains(" ") && !user.contains(",") && !user.contains(";") && !user.contains("$") && !user.contains("#") && !user.contains("[") && !user.contains("]") && !user.contains("{") && !user.contains("}") && !user.contains("@") && !user.contains("%") && !user.contains("^")) {
                        mAuth.signInWithEmailAndPassword(user+"@moviemeridian.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful())
                                    Toast.makeText(SignInActivity.this,"Sign-in failed!",Toast.LENGTH_LONG).show();
                                else {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    Toast.makeText(SignInActivity.this, "Sign-in success!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                } else
                    Toast.makeText(SignInActivity.this, "Error : Username cannot contain: comma,space,;,$,#,[,],{,},@,%,^", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(SignInActivity.this, "Error : Username/password too short, please try again", Toast.LENGTH_LONG).show();
        }*/