package org.app.anand.moviemagzine2.UserProfile;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.app.anand.moviemagzine2.Adapter.ProfileAdapter;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.Model.Movie;
import org.app.anand.moviemagzine2.MoviesAPI;
import org.app.anand.moviemagzine2.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Tab1 extends Fragment {

    private String MyPREFERENCES = "key";
    TextView txt1;

    private OnFragmentInteractionListener mListener;
    private RecyclerView rvProfile_frag;


    public Tab1() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        rvProfile_frag = (RecyclerView) v.findViewById(R.id.rvProfile_frag_id);

        SharedPreferences prefs = getContext().getSharedPreferences(MyPREFERENCES, getContext().MODE_PRIVATE);
        Set configTemp = prefs.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), new HashSet<String>());
        List<String> configList = new ArrayList<String>(configTemp);
        //configList
        getData(configList);
        txt1 = (TextView) v.findViewById(R.id.txtTab_id);
       txt1.setText(""+configList.size());


        return v;
    }
    private void getData(List<String> list){
        final List<Movie> total=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            MoviesAPI.Factory.getInstance().getMovieDetails(Integer.parseInt(list.get(i))).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Movie body = response.body();
                    if(body!=null) {
                        total.add(body);
                        ProfileAdapter adapter = new ProfileAdapter(total, getContext());
                        rvProfile_frag.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        rvProfile_frag.setAdapter(adapter);
                    }

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }



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
}
