package org.app.anand.moviemagzine2.Auth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.app.anand.moviemagzine2.*;
import org.app.anand.moviemagzine2.UserProfile.Tab3;
import org.app.anand.moviemagzine2.UserProfile.UserProfile;
import org.app.anand.moviemagzine2.preferences.Preferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class cloudDataSync {
    String flow;
    String firebaseURL = "";
    private static FirebaseAuth mAuth;
    Context context;
    String fbBucketLink="gs://moviemeridian-376c7.appspot.com";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(fbBucketLink);

    public cloudDataSync() {
    }

    public cloudDataSync(Intent intent, Context c) {
        String uname, pass;

        try {
            if (c != null) {
                context = c;
                //firebaseURL = context.getResources().getString(R.string.firebaseLink);
                flow = intent.getStringExtra("Flow");
                if (flow != null) {
                    if (flow.length() > 0) {
                        switch (flow) {
                            case "UserLogin":
                                uname = intent.getStringExtra("Username");
                                pass = intent.getStringExtra("Password");
                                getmAuth();
                                userLogin(uname, pass);
                                break;
                            case "UserImage":
                                uname = intent.getStringExtra("Username");
                                pass = intent.getStringExtra("Password");
                                getmAuth();
                                uploadImage(uname, pass,intent.getStringExtra("Mode"),Uri.parse(intent.getStringExtra("uri")));
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Cloud sync:", "Exception in cloud sync");
            e.printStackTrace();
        }
    }

    public static FirebaseAuth getmAuth() {
        if(mAuth==null)
            mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    public void userLogin(final String user, String password) {
        if (user != null && password != null) {
            if (user.length() > 0 && password.length() > 0) {
                if (!user.contains(" ") && !user.contains(",") && !user.contains(";") && !user.contains("$") && !user.contains("#") && !user.contains("[") && !user.contains("]") && !user.contains("{") && !user.contains("}") && !user.contains("@") && !user.contains("%") && !user.contains("^")) {
                    mAuth.signInWithEmailAndPassword(user+"@moviemeridian.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                                Toast.makeText(context,"Sign-in failed!",Toast.LENGTH_LONG).show();
                            else {


                                Intent i = new Intent(context.getApplicationContext(), UserProfile.class);
                                context.startActivity(i);
                                Toast.makeText(context, "Sign-in success!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else
                    Toast.makeText(context, "Error : Username cannot contain: comma,space,;,$,#,[,],{,},@,%,^", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(context, "Error : Username/password too short, please try again", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Error : Username/password not available", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(String user, String password, final String Mode, final Uri uri){
        if (user != null && password != null) {
            if (user.length() > 0 && password.length() > 0) {
                if (!user.contains(" ") && !user.contains(",") && !user.contains(";") && !user.contains("$") && !user.contains("#") && !user.contains("[") && !user.contains("]") && !user.contains("{") && !user.contains("}") && !user.contains("@") && !user.contains("%") && !user.contains("^")) {
                    mAuth.signInWithEmailAndPassword(user+"@moviemeridian.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                                Toast.makeText(context,"Server connection failed!",Toast.LENGTH_LONG).show();
                            else {
                                if(Mode.equals("Browse")){
                                    try{
                                        task.getResult().getUser().getToken(true).getResult().getToken();
                                        StorageReference filePath = storageRef.child("Photos").child(uri.getLastPathSegment());//child("Photos").
                                        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_LONG).show();

                                                Uri downloadImg = taskSnapshot.getDownloadUrl();
                                                //Picasso.with(context).load(downloadImg).fit().centerCrop().into(Tab3.FbImg);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("FailUpload",e.getMessage());
                                            }
                                        });
                                    }catch(Exception e){

                                    }
                                }
                            }
                        }
                    });
                } else
                    Toast.makeText(context, "Error : Username cannot contain: comma,space,;,$,#,[,],{,},@,%,^", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(context, "Error : Username/password too short, please try again", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Error : Username/password not available", Toast.LENGTH_LONG).show();
        }
    }


}