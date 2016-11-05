package org.app.anand.moviemagzine2.UserProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.app.anand.moviemagzine2.Adapter.MoviesAdapter;
import org.app.anand.moviemagzine2.Adapter.ProfileAdapter;
import org.app.anand.moviemagzine2.Auth.cloudDataSync;
import org.app.anand.moviemagzine2.Model.Movie;
import org.app.anand.moviemagzine2.Model.Movies;
import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.MoviesAPI;
import org.app.anand.moviemagzine2.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Tab2 extends Fragment {
    private RecyclerView rvProfile_upcoming_frag;
    private String MyPREFERENCES = "key";
int i;
    List<Result> total=new ArrayList<>();
    List<Result> total2=new ArrayList<>();
RecyclerView rvSubscription;
    int pages = 0;

    public static SharedPreferences dataPreferences;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
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
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        rvSubscription = (RecyclerView) v.findViewById(R.id.rvProfile_favorites_frag_id);
        rvProfile_upcoming_frag = (RecyclerView) v.findViewById(R.id.rvProfile_Upcoming_frag_id);
        SharedPreferences prefs = getContext().getSharedPreferences(MyPREFERENCES, getContext().MODE_PRIVATE);
        Set configTemp = prefs.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getUid().toString(), new HashSet<String>());
        List<String> configList = new ArrayList<String>(configTemp);
        //configList

        getData(configList);
        getSubscriptionData();


        return v;
    }
    private void getData(List<String> list){




        final List<Movie> total=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            MoviesAPI.Factory.getInstance().getMovieDetails(Integer.parseInt(list.get(i))).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Movie body = response.body();
                    Log.d("status", body.getStatus());
                    Log.d("statusID",body.getId().toString());
                    String date[] = body.getReleaseDate().split("-");
                    int remainingDays=remainingDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    Log.d("num",""+remainingDays);
                    if(body!=null && remainingDays>0) {

                        total.add(body);
                        /////-------------------
                        Collections.sort(total, new Comparator<Movie>() {
                            @Override
                            public int compare(Movie o1, Movie o2) {
                                return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                            }
                        });


                        ///---------------------
                        ProfileAdapter adapter = new ProfileAdapter(total, getContext());
                        rvProfile_upcoming_frag.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        rvProfile_upcoming_frag.setAdapter(adapter);
                    }else{

                    }

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }



    }
    private void getSubscriptionData() {
        total2.clear();
        dataPreferences = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        Set userPreferencesList = dataPreferences.getStringSet(cloudDataSync.getmAuth().getCurrentUser().getEmail(),  new HashSet<String>());
        final List<String> configList = new ArrayList<>(userPreferencesList);
Log.d("configlist", configList.toString());
        for (i = 1; i <= 1; i++) {
            Map<String, String> opt = new HashMap<>();
            opt.put("api_key", "ce1672fa9b97cf806c105a0c7b5df8e0");
            opt.put("language", "en-US");
            opt.put("page", String.valueOf(i));
            MoviesAPI.Factory.getInstance().getUpcomingFrag(opt).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Movies body = response.body();
                    if(body!=null) {
                        List<Result> t = body.getResults();
                        Iterator itrMovie = t.iterator();
                        String current ="";
                        while (itrMovie.hasNext()) {

                            Result r = (Result) itrMovie.next();
                            if(r.getGenreIds()!=null && r.getGenreIds().size()>0){


                            switch(r.getGenreIds().get(0)){
                                case 28:
                                    current="Action";
                                    break;
                                case 12:
                                    current="Adventure";
                                    break;
                                case 16:
                                    current="Animation;";
                                    break;
                                case 35:
                                    current="Comedy";
                                    break;
                                case 80:
                                    current="Crime";
                                    break;
                                case 99:
                                    current="Documentary";
                                    break;
                                case 18:
                                    current="Drama";
                                    break;
                                case 10751:
                                    current="Family";
                                    break;
                                case 14:
                                    current="Fantasy";
                                    break;
                                case 36:
                                    current="History";
                                    break;
                                case 27:
                                    current="Horror";
                                    break;
                                case 10202:
                                    current="Music";
                                    break;
                                case 9648:
                                    current="Mystery";
                                    break;
                                case 10749:
                                    current="Romance";
                                    break;
                                case 878:
                                    current="Science Fiction";
                                    break;
                                case 10770:
                                    current="TV Movie";
                                    break;
                                case 53:
                                    current="Thriller";
                                    break;
                                case 10752:
                                    current="War";
                                    break;
                                case 37:
                                    current="Western";
                                    break;
                             }
                            }
                            if (configList.contains(current))
                            total2.add(r);

                            Log.d("current", current);
                        }
                        if(i==2) {
                            Log.d("favorite","Inside IF");
                            //txt.setText(String.valueOf((deleteNull(total)).size()));

                            MoviesAdapter adapter = new MoviesAdapter(deleteNull(total2), getContext());
                            rvSubscription.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            rvSubscription.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {

                }
            });
        }


        //
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

    public int remainingDays(int y, int m, int d){
        try {
            String releaseDte = y+"-"+m+"-"+d;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar today = Calendar.getInstance();
            String now = sdf.format(today.getTime());
            Date d1 = sdf.parse(releaseDte);
            Date d2 = sdf.parse(now);
            if(d1.compareTo(d2) > 0){
                int result = (int) ((d1.getTime() - d2.getTime())/(1000*60*60*24));
                return result;
            } else
                return 0;
        }catch(ParseException pe){
            return  -1;
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
