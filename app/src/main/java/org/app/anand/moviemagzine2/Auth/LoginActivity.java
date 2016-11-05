package org.app.anand.moviemagzine2.Auth;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.app.anand.moviemagzine2.*;

/**
 * A registration screen that offers registration via usernmae/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button mEmailSignInButton,mLoginButton;
    public static Context context;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        context = this;
        mAuth = cloudDataSync.getmAuth();
        /*InitActivity.spAuthentication = getApplicationContext().getSharedPreferences("org.app.anand.moviemagzine", Context.MODE_PRIVATE);
        String start = InitActivity.spAuthentication.getString("org.app.anand.moviemagzine.authentication.registered","No");
        Log.d("Login","Start:"+start);


        if(start.equals("Yes")) {
            Log.d("Init","Redirecting to Main");
            Intent Home = new Intent(this, org.app.anand.moviemagzine2.MainActivity.class);
            startActivity(Home);
            LoginActivity.this.finish();
        }*/
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }

        };
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPassword);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginButton = (Button) findViewById(R.id.email_log_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isOnline()) {
                        Login();
                    } else
                        Toast.makeText(LoginActivity.this, "Error : You are offline, please try again to register with data connection ON", Toast.LENGTH_LONG).show();
                }catch(Exception e){Toast.makeText(LoginActivity.this,"Exception:"+e.getMessage(),Toast.LENGTH_LONG).show();}
            }
        });

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                } catch (Exception e) {
                }
            }
        });
    }

    private void Login() {
        final String username = mEmailView.getText().toString();
        if (username != null) {
            if (username.length() > 4) {
                if (!username.contains(" ") && !username.contains(",") && !username.contains(";") && !username.contains("$") && !username.contains("#") && !username.contains("[") && !username.contains("]") && !username.contains("{") && !username.contains("}") && !username.contains("@") && !username.contains("%") && !username.contains("^")) {
                    final String pass = mPasswordView.getText().toString();
                    String confirm = mConfirmPasswordView.getText().toString();
                    if (pass != null && confirm != null) {
                        if (pass.equals(confirm) && pass.length() > 7 && confirm.length() > 7) {

                            mAuth.createUserWithEmailAndPassword(username+"@moviemeridian.com", pass)
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this,"User Created Successfully!",Toast.LENGTH_LONG).show();
                                                InitActivity.spAuthentication = getApplicationContext().getSharedPreferences("org.app.anand.moviemagzine", Context.MODE_PRIVATE);
                                                InitActivity.spAuthentication.edit().putString("org.app.anand.moviemagzine.authentication.registered", "Yes").commit();
                                                InitActivity.spAuthentication.edit().putString("org.app.anand.moviemagzine.authentication.username", username).commit();
                                                InitActivity.spAuthentication.edit().putString("org.app.anand.moviemagzine.authentication.password", pass).commit();
                                                InitActivity.spAuthentication.edit().putString("org.app.anand.moviemagzine.authentication.UID",task.getResult().getUser().getUid()).commit();

                                                Intent Home = new Intent(LoginActivity.context, org.app.anand.moviemagzine2.MainActivity.class);
                                                LoginActivity.this.finish();
                                                startActivity(Home);

                                                AuthCredential authCredential = EmailAuthProvider.getCredential(username+"@moviemeridian.com",pass);
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, "Error : Password is not confirmed or password is too short (minimum 8 characters), please try again", Toast.LENGTH_LONG).show();
                            mEmailView.setText("");
                            mPasswordView.setText("");
                            mConfirmPasswordView.setText("");
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error : Username cannot contain: comma,space,;,$,#,[,],{,},@,%,^", Toast.LENGTH_LONG).show();
                    mEmailView.setText("");
                    mPasswordView.setText("");
                    mConfirmPasswordView.setText("");
                }
            } else
                Toast.makeText(LoginActivity.this, "Error : Username too short (minimum 5 characters), please try again", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else
            return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /*@Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/
}

