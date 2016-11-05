package org.app.anand.moviemagzine2.JsonParser;

/**
 * Created by User on 11/3/2016.
 */

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.app.anand.moviemagzine2.Model.Tutorial;
import org.json.JSONArray;
import org.json.JSONObject;



public class TutorialJSONParser {

    public static List<Tutorial> TutorialParser(String content)
    {
        try
        {
            JSONObject jo = new JSONObject(content);
            //arr = new JSONArray(content);
            //JSONObject obj = arr.getJSONObject(i);
            if(jo==null)
                Log.d("parser","jo null");
            List<Tutorial> ltt = new ArrayList<>();
            JSONArray items= (JSONArray) jo.get("items");
            if(items==null)
                Log.d("parser","items null");
            for(int i=0; i<items.length(); i++)
            {
                Tutorial tt = new Tutorial();

                JSONObject obj = items.getJSONObject(i);
                JSONObject id = (JSONObject) obj.get("id");
                JSONObject snippet = (JSONObject) obj.get("snippet");
                JSONObject thumbnails = (JSONObject) snippet.get("thumbnails");
                JSONObject medium = (JSONObject) thumbnails.get("medium");

                tt.setPublishedAt(snippet.getString("publishedAt"));
                tt.setTitle(snippet.getString("title"));
                tt.setUrl(new URL(medium.getString("url")));
                tt.setVideoId(id.getString("videoId"));

                ltt.add(tt);
            }

            return ltt;
        }catch(Exception e)
        {
            Log.d("parser","Error:"+e.getMessage());
            return null;
        }
    }
}

