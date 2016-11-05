package org.app.anand.moviemagzine2.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.app.anand.moviemagzine2.Model.Movie;
import org.app.anand.moviemagzine2.MovieDetails;
import org.app.anand.moviemagzine2.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 10/20/2016.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder>{
    private List<Movie> results;
Context context;

    public ProfileAdapter(List<Movie> results, Context context) {
        this.results = results;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Movie movie = results.get(position);

        String date[] = movie.getReleaseDate().split("-");
        int remainingDays=remainingDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Log.d("num",""+remainingDays);

        String name = movie.getTitle();
        if(name.length()>26)
            name = name.substring(0,21) +"...";
        BigDecimal fame = new BigDecimal(movie.getPopularity());
        fame = fame.setScale(2,BigDecimal.ROUND_HALF_UP);
        String content = "Name: " + name + "\n" + "Popularity:"+String.valueOf(fame);
        String txtRemainingDays;
        if(remainingDays>0)
            txtRemainingDays = "Releasing in "+remainingDays+" Days";
        else
            txtRemainingDays = "";
        Double dObj = new Double(movie.getVoteAverage());
        float rating=dObj.floatValue()*5/10;//en base a 5
        Log.d("rating", ""+rating);
        holder.ratingBar.setRating(rating);
        holder.txt.setText(content);
        holder.txtRemainingDays.setText(txtRemainingDays);
        Glide.with(context).load("http://image.tmdb.org/t/p/w300"+movie.getBackdropPath()).placeholder(R.drawable.poster).override(500,300).centerCrop().into(holder.img);

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

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt, txtRemainingDays;
        ImageView img;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {

            super(itemView);
            txtRemainingDays = (TextView) itemView.findViewById(R.id.txtRemainingDays_id);
            txt = (TextView) itemView.findViewById(R.id.txt_profile);
            img = (ImageView) itemView.findViewById(R.id.img_profile);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar_favorite);
            ratingBar.setRating(4);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie user = results.get(position);
            // We can access the data within the views
            Log.d("DDD",  ""+user.getId());
            Intent i = new Intent(context, MovieDetails.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("movieId", user.getId());
            context.startActivity(i);
            //removeAt(position);
            Toast.makeText(context, user.getTitle(), Toast.LENGTH_LONG).show();

        }
    }


}