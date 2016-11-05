package org.app.anand.moviemagzine2.categories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Categories extends AppCompatActivity {
    NowPlaying nowPlaying;
    Popular popular;
    TopRated topRated;
    Upcoming upComing;

    Button btnNowplaying, btnPopular, btnToprated, btnUpcoming;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);





        Intent i = getIntent();
        String expression = i.getStringExtra("expression");

        switch(expression) {
            case "nowplaying" :
                nowPlaying = new NowPlaying();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,nowPlaying).commit();
                break; // optional

            case "popular" :
                popular = new Popular();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,popular).commit();
                break; // optional
            case "toprated" :
                topRated = new TopRated();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,topRated).commit();
                break; // optional
            case "upcoming" :
                upComing = new Upcoming();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,upComing).commit();
                break; // optional

            // You can have any number of case statements.
            default : // Optional
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,nowPlaying).commit();
        }
        btnUpcoming = (Button) findViewById(R.id.btnnupcoming_id);
        btnUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Categories.this.finish();
                Intent recreate = new Intent(Categories.this,Categories.class);
                recreate.putExtra("expression","upcoming");

                startActivity(recreate);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,nowPlaying).commit();

            }
        });
        btnNowplaying = (Button) findViewById(R.id.btnnowplaing_id);
        btnNowplaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Categories.this.finish();
                Intent recreate = new Intent(Categories.this,Categories.class);
                recreate.putExtra("expression","nowplaying");

                startActivity(recreate);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,nowPlaying).commit();

            }
        });
        btnPopular = (Button) findViewById(R.id.btnpopular_id);
        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
                Categories.this.finish();
                Log.d("Categories","Pop");
                Intent recreate = new Intent(Categories.this,Categories.class);
                recreate.putExtra("expression","popular");

                startActivity(recreate);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,popular).commit();

            }
        });
        btnToprated = (Button) findViewById(R.id.btntoprated_id);
        btnToprated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
                Categories.this.finish();
                Log.d("Categories","top");
                Intent recreate = new Intent(Categories.this,Categories.class);
                recreate.putExtra("expression","toprated");

                startActivity(recreate);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category,topRated).commit();

            }
        });
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
