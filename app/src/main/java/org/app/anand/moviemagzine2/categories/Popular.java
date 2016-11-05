package org.app.anand.moviemagzine2.categories;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.app.anand.moviemagzine2.Adapter.MoviesAdapter;
import org.app.anand.moviemagzine2.Model.Movies;
import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.MoviesAPI;
import org.app.anand.moviemagzine2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Popular extends Fragment {
    RecyclerView rvPopularFrag;
        int pages = 0;
        List<Result> total=new ArrayList<>();
        TextView txt;
        int i,count=0;

    ProgressDialog progressDialog;

    public Popular() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_popular, container, false);

        txt= (TextView) v.findViewById(R.id.popularTxtFrag_id);
        if(isOnline()) {
            rvPopularFrag = (RecyclerView) v.findViewById(R.id.rvPopular_frag_id);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            //requestSearchData();
            iterateResult();
        } else
            Toast.makeText(getContext(),"You are offline!",Toast.LENGTH_SHORT).show();
        return v;
    }


    protected boolean isOnline() {
        ConnectivityManager cm= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else
            return false;
    }
    private void requestSearchData() {
        // api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US&query=
        Map<String, String> opt = new HashMap<>();
        opt.put("api_key", "ce1672fa9b97cf806c105a0c7b5df8e0");
        opt.put("language", "en-US");

        MoviesAPI.Factory.getInstance().getPop().enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Movies temp = response.body();
                if(temp!=null) {
                    pages = temp.getTotalPages();
                    total = temp.getResults();
                    Log.d("Search", pages + ":" + total.size());
                    iterateResult();
                } else {
                    /*Thread t = new Thread();
                    t.start();
                    try {
                        t.sleep(5000);
                    } catch (Exception e) {

                    }
                    requestSearchData();//Toast.makeText(getContext(),"No result!",Toast.LENGTH_SHORT).show();*/
                    try {
                        Response<Movies> response2 = MoviesAPI.Factory.getInstance().getPop().execute();
                        Movies temp2 = response.body();
                        if(temp2!=null){
                            pages = temp2.getTotalPages();
                            total = temp2.getResults();
                            Log.d("Search", pages + ":" + total.size());
                            iterateResult();
                        }
                    }catch(Exception e){

                    }
                }
            }


            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
    }


    private void iterateResult() {
        Log.d("itr","Inside");
        Log.d("itr",""+pages);

        for (i = 1; i <= 7; i++) {
            Map<String, String> opt = new HashMap<>();
            opt.put("api_key", "ce1672fa9b97cf806c105a0c7b5df8e0");
            opt.put("language", "en-US");
            opt.put("page", String.valueOf(i));
            MoviesAPI.Factory.getInstance().getPopFrag(opt).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Movies body = response.body();
                   if(body!=null) {
                        List<Result> t = body.getResults();
                        Iterator itrMovie = t.iterator();
                        while (itrMovie.hasNext()) {
                            Result r = (Result) itrMovie.next();
                            total.add(r);
                            Log.d("Popular", i + ":" + pages);
                        }
                        if(i==8) {
                            Log.d("Popular","Inside IF");
                            txt.setText("Total: "+String.valueOf((deleteNull(total)).size()));
                            count++;
                            MoviesAdapter adapter = new MoviesAdapter(deleteNull(total), getContext());
                            rvPopularFrag.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            rvPopularFrag.setAdapter(adapter);
                            progressDialog.dismiss();
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

}
