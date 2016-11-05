package org.app.anand.moviemagzine2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.app.anand.moviemagzine2.Adapter.SearchAdapter;
import org.app.anand.moviemagzine2.Model.Movies;
import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends AppCompatActivity {
    public static final String END_POINT="https://api.themoviedb.org/3";
    List<Result> mySearch;
    RecyclerView search;
    SearchAdapter adapter;
    int pages = 0;
    List<Result> total=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (RecyclerView) findViewById(R.id.rvSearch);
        Intent i = getIntent();
        String data = i.getStringExtra("data");
        setTitle("MovieMeridian - Search Result: " + data);

        requestSearchData(data);
        iterateResult(data);
    }


    private void requestSearchData(final String s) {
        // api_key=ce1672fa9b97cf806c105a0c7b5df8e0&language=en-US&query=
        Map<String, String> opt = new HashMap<>();
        opt.put("api_key", "ce1672fa9b97cf806c105a0c7b5df8e0");
        opt.put("language", "en-US");
        opt.put("query", s);
MoviesAPI.Factory.getInstance().getS(opt).enqueue(new Callback<Movies>() {
    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        Movies temp = response.body();
        if(temp!=null) {
            pages = temp.getTotalPages();
            total = temp.getResults();
            Log.d("Search", pages + ":" + total.size());
            iterateResult(s);
        } else
            Toast.makeText(getApplicationContext(),"No result!",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onFailure(Call<Movies> call, Throwable t) {

    }
    });
    }

    private void iterateResult(String query) {
        Log.d("itr","Inside");
        Log.d("itr",""+pages);
        for (int i = 2; i <= pages; i++) {
            Log.d("Search",""+i);
            Map<String, String> opt = new HashMap<>();
            opt.put("api_key", "ce1672fa9b97cf806c105a0c7b5df8e0");
            opt.put("language", "en-US");
            opt.put("query", query);
            opt.put("page", String.valueOf(i));
            MoviesAPI.Factory.getInstance().getS(opt).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Movies body = response.body();
                    if(body!=null) {
                        List<Result> t = body.getResults();
                        Iterator itrMovie = t.iterator();
                        while (itrMovie.hasNext()) {
                            Result r = (Result) itrMovie.next();
                            total.add(r);
                        }
                    }

                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {

                }
            });
        }
        adapter = new SearchAdapter(deleteNull(total), getApplicationContext());
        search.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        search.setAdapter(adapter);
    }

    /*
    *
    * */
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
