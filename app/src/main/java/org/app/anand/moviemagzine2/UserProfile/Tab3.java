package org.app.anand.moviemagzine2.UserProfile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.app.anand.moviemagzine2.Adapter.MoviesAdapter;
import org.app.anand.moviemagzine2.Adapter.UpcomingAdapter;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button uploadBtn,captureBtn;
    public static ImageView FbImg;
    ProgressDialog progressBar;
    StorageReference backendStorage;
    private static final int CAMERA_CODE = 1;
    private static final int BROWSE_CODE = 256;
    Uri uriSavedImage;
    String fbBucketLink="gs://moviemeridian-376c7.appspot.com";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(fbBucketLink);
    RecyclerView recyclerView;
    public Tab3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab3.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab3, container, false);

        backendStorage = FirebaseStorage.getInstance().getReference();
        uploadBtn = (Button) v.findViewById(R.id.buttonUpload);
        captureBtn = (Button) v.findViewById(R.id.buttonCapture);
        FbImg = (ImageView) v.findViewById(R.id.imageView);
        progressBar = new ProgressDialog(getActivity());

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              browseImage();
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.memories);

        SharedPreferences dataPreferences = getActivity().getApplicationContext().getSharedPreferences("UserMemories", Context.MODE_PRIVATE);
        Set userPreferencesList = dataPreferences.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(), new HashSet<String>());
        List<String> configList = new ArrayList<>(userPreferencesList);


        Set<String> configListSet = new HashSet<String>(configList);
        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("UserMemories", getActivity().getApplicationContext().MODE_PRIVATE).edit();
        editor.putStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(), configListSet).commit();

        UpcomingAdapter adapter = new UpcomingAdapter(getContext(),configList,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void captureImage(){
        Calendar calendar = Calendar.getInstance();
        long stamp = calendar.getTimeInMillis();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        uriSavedImage=Uri.fromFile(new File("/sdcard/demo/flashImage_"+stamp+".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            startActivityForResult(intent,CAMERA_CODE);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)){
                Toast.makeText(getActivity(), "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CAMERA},CAMERA_CODE);
            }
        }
    }

    private void browseImage(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            File f = new File(getActivity().getApplicationContext().getFilesDir().getPath()+"/sdcard/");
            Uri uri = Uri.parse("file://" + f.getAbsolutePath());
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setDataAndType(uri,"image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

            startActivityForResult(intent, BROWSE_CODE);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity(), "Storage access permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},BROWSE_CODE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            progressBar.setMessage("Uploading Image ...");
            progressBar.show();
        }
        if(requestCode==CAMERA_CODE && resultCode==RESULT_OK){
            try {
                Uri uri = uriSavedImage;

                StorageReference filePath = storageRef.child("Photos").child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.dismiss();
                        Toast.makeText(getActivity(), "Uploaded successfully", Toast.LENGTH_LONG).show();

                        Uri downloadImg = taskSnapshot.getDownloadUrl();
                        Picasso.with(getActivity()).load(downloadImg).fit().centerCrop().into(FbImg);
                    }
                });
            }catch(Exception e){
                progressBar.dismiss();
            }
        }
        else if(requestCode==BROWSE_CODE && resultCode==RESULT_OK) {
            try {
                uriSavedImage = data.getData();
                StorageReference filePath = storageRef.child(uriSavedImage.getLastPathSegment());//child("Photos")
                filePath.putFile(uriSavedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.dismiss();
                        Toast.makeText(getActivity(), "Uploaded successfully", Toast.LENGTH_LONG).show();

                        Uri downloadImg = taskSnapshot.getDownloadUrl();
                        if(cloudDataSync.getmAuth().getCurrentUser() != null) {
                            SharedPreferences dataPreferences = getActivity().getApplicationContext().getSharedPreferences("UserMemories", Context.MODE_PRIVATE);
                            Set userPreferencesList = dataPreferences.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(), new HashSet<String>());
                            List<String> configList = new ArrayList<>(userPreferencesList);

                            configList.add(downloadImg.toString());

                            Set<String> configListSet = new HashSet<String>(configList);
                            SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("UserMemories", getActivity().getApplicationContext().MODE_PRIVATE).edit();
                            editor.putStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(), configListSet).commit();

                            UpcomingAdapter adapter = new UpcomingAdapter(getContext(),configList,1);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(adapter);
                        }
                        Picasso.with(getActivity()).load(downloadImg).fit().centerCrop().into(FbImg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FailUpload",e.getMessage());
                        progressBar.dismiss();
                    }
                });
            } catch (Exception e) {
                progressBar.dismiss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    Calendar calendar = Calendar.getInstance();
                    long stamp = calendar.getTimeInMillis();
                    uriSavedImage=Uri.fromFile(new File("/sdcard/demo/flashImage_"+stamp+".jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    startActivityForResult(intent,CAMERA_CODE);
                }
                break;
            case BROWSE_CODE:
                File f = new File(getActivity().getApplicationContext().getFilesDir().getPath()+"/sdcard/");
                Uri uri = Uri.parse("file://" + f.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setDataAndType(uri,"image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                uriSavedImage=Uri.fromFile(new File(intent.getData().getPath()));
                startActivityForResult(intent, BROWSE_CODE);
                break;
        }
    }
}
