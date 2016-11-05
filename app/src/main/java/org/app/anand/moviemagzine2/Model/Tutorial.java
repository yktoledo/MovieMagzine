package org.app.anand.moviemagzine2.Model;

/**
 * Created by User on 11/3/2016.
 */

import java.net.URL;

import android.graphics.Bitmap;

public class Tutorial {
    String videoId,publishedAt,title;
    URL url;
    Bitmap tile;
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public URL getUrl() {
        return url;
    }
    public void setUrl(URL url) {
        this.url = url;
    }
    public Bitmap getTile() {
        return tile;
    }
    public void setTile(Bitmap tile) {
        this.tile = tile;
    }
}