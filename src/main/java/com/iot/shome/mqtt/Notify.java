package com.iot.shome.mqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.iot.shome.MainActivity;
import com.iot.shome.R;
import com.iot.shome.utils.StringHandler;

import java.util.Calendar;

/**
 * Created by Kareem Diab on 3/29/2016.
 */
public class Notify {

    /**
     * Displays a notification in the notification area of the UI
     * @param context Context from which to create the notification
     * @param topic The string to display to the user as a title
     * @param payload The string to display to the user as a title
     */

    static int notificationID = 0;
    static void notifcation(Context context, String topic, String payload) {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v7.app.NotificationCompat.Builder mBuilder = new android.support.v7.app.NotificationCompat.Builder(context);
        topic = StringHandler.handle(topic);
        mBuilder.setContentTitle(topic);
        mBuilder.setContentText(payload);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setSound(soundUri);

        /*Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        mBuilder.setContentIntent(pi);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());
        notificationID++;
       //Log.d("Notify","Notification ID: "+ notificationID);

    }
}
