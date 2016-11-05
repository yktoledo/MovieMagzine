package org.app.anand.moviemagzine2.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.MovieDetails;
import org.app.anand.moviemagzine2.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by User on 10/20/2016.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    private List<Result> results;
Context context;

    public SearchAdapter(List<Result> results, Context context) {
        this.results = results;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Result movie = results.get(position);

        String name = movie.getOriginalTitle();
        if(name.length()>26)
            name = name.substring(0,21) +"...";
        BigDecimal fame = new BigDecimal(movie.getPopularity());
        fame = fame.setScale(2,BigDecimal.ROUND_HALF_UP);
        String content = "Name: " + name + "\n" + "Popularity:"+String.valueOf(fame);
        holder.txt.setText(movie.getTitle());
        Glide.with(context).load("http://image.tmdb.org/t/p/w300"+movie.getPosterPath()).override(300,200).centerCrop().into(holder.img);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt;
        ImageView img;
        public MyViewHolder(View itemView) {

            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
            img = (ImageView) itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position

            //RecyclerView rv = ((RecyclerView) view.findViewById(R.id.rvContacts));

            //if (position != RecyclerView.NO_POSITION) {  Check if an item was deleted, but the user clicked it before the UI removed it
                Result user = results.get(position);
                // We can access the data within the views
                Log.d("DDD",  ""+user.getId());
                    Intent i = new Intent(context, MovieDetails.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("movieId", user.getId());
                    context.startActivity(i);
                //removeAt(position);
                Toast.makeText(context, user.getTitle(), Toast.LENGTH_LONG).show();

            //}

        }
    }
//    private void accessData(int id){
//        Intent i = new Intent(context, MovieDetails.class);
//        i.putExtra("movieId", id);
//        startActivity(i);
//        //removeAt(position);
//        Toast.makeText(context, user.getTitle(), Toast.LENGTH_LONG).show();
//    }

}