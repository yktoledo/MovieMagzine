package org.app.anand.moviemagzine2.Alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.app.anand.moviemagzine2.MovieDetails;
import org.app.anand.moviemagzine2.R;
import org.app.anand.moviemagzine2.UserProfile.Tab1;

/**
 * Created by User on 10/30/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra("peliId");
        String title = intent.getStringExtra("title");
        Log.d("onReceive", id);
        createNotification(context, title, id, "Alert");
    }



    private void createNotification(Context context, String msg, String msgText, String msgAlert) {
        Intent i = new Intent(context,MovieDetails.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("movieId", Integer.parseInt(msgText));
        PendingIntent notificationIntent = PendingIntent.getActivity(context,0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.drawable.icon);
        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
