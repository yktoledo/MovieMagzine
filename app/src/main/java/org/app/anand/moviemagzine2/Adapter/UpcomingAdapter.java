package org.app.anand.moviemagzine2.Adapter;


import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.R;

import java.util.List;

/**
 * Created by User on 10/20/2016.
 */

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.MyViewHolder>{
    private List<Result> results;
    private List<String> images;
Context context;
    int mode;

    public UpcomingAdapter(List<Result> results, Context context) {
        this.results = results;
        this.context = context;
        mode = -1;
    }

    public UpcomingAdapter(Context c, List<String> img,int m){
        this.context = c;
        this.images = img;
        mode = m;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_movies, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(mode == 1){
            Glide.with(context).load(images.get(position)).override(800, 550).centerCrop().into(holder.img);
        } else {
            Result movie = results.get(position);
            Glide.with(context).load("http://image.tmdb.org/t/p/w300" + movie.getBackdropPath()).override(800, 550).centerCrop().into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        if(mode==1)
            return images.size();
        else
            return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public MyViewHolder(View itemView) {

            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.upImgId);
        }
    }


}