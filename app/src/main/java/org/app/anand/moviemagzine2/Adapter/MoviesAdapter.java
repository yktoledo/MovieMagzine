package org.app.anand.moviemagzine2.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.app.anand.moviemagzine2.JsonParser.TutorialJSONParser;
import org.app.anand.moviemagzine2.Model.Result;
import org.app.anand.moviemagzine2.Model.Tutorial;
import org.app.anand.moviemagzine2.MovieDetails;
import org.app.anand.moviemagzine2.R;
import org.app.anand.moviemagzine2.Web.HttpManager;
import org.app.anand.moviemagzine2.Web.RequestPackage;
import org.app.anand.moviemagzine2.Web.TimeTracker;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by User on 10/20/2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>{
    private List<Result> results;
    Context context;
    String YoutubeAPIKey="AIzaSyDx-MDXhjIe015fy0sECEksbz0DtHoXIAU";//AIzaSyADvSwy4Rcs4mAHTBd20Iox-QzzxVjrF2M
    String tutorialBaseLink="https://www.googleapis.com/youtube/v3/search";
    Result user;

    public MoviesAdapter(List<Result> results, Context context) {
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




        holder.txt.setText(content);
        Glide.with(context).load("http://image.tmdb.org/t/p/w300"+movie.getBackdropPath()).placeholder(R.drawable.poster).override(400,400).centerCrop().into(holder.img);

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
            int position = getAdapterPosition();
           user = results.get(position);
            String name = user.getTitle();

            RequestPackage p2 = new RequestPackage();
            p2.setMethod("GET");
            p2.setUri(tutorialBaseLink);

            String from=paramWebEncoder(1430438400);
            String to=paramWebEncoder(new TimeTracker().getCurrent());

            p2.setParam("part","snippet");
            p2.setParam("maxResults","50");
            p2.setParam("publishedAfter",from);
            p2.setParam("publishedBefore",to);
            p2.setParam("q",name + " official trailer");
            p2.setParam("relevanceLanguage","en");
            p2.setParam("type","video");
            p2.setParam("key",YoutubeAPIKey);

            GetTrailer getTrailer = new GetTrailer();
            getTrailer.execute(p2);


            Toast.makeText(context, user.getTitle(), Toast.LENGTH_LONG).show();

        }
    }


    public String paramWebEncoder(long limiter)
    {
        long unixSeconds = limiter;//1372339860;
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        String formattedDate = sdf.format(date);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        formattedDate = formattedDate.replace(" ", "T");
        String rm=formattedDate.substring(19,formattedDate.length());
        formattedDate = formattedDate.replace(rm, "Z");

        return formattedDate;
    }

    private  class GetTrailer extends AsyncTask<RequestPackage, String, List<Tutorial>> {
        public List<Tutorial> ttl;
        @Override
        protected List<Tutorial> doInBackground(RequestPackage... params) {
            String content2 = HttpManager.getData(params[0]);
            Log.d("async",content2);
            ttl= TutorialJSONParser.TutorialParser(content2);
            Tutorial t=null;
            for(int k=0;k<ttl.size();k++){
                if(ttl.get(k) != null) {
                    t = ttl.get(k);
                    break;
                }
            }
            String ID = t.getVideoId();
            // We can access the data within the views
            Log.d("DDD",  ""+user.getId());
            Intent i = new Intent(context, MovieDetails.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("movieId", user.getId());
            i.putExtra("ID",ID);
            context.startActivity(i);
            //removeAt(position);
            return ttl;
        }

        public List<Tutorial> getList(){
            return ttl;
        }

    }
}