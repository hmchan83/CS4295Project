package com.cs4295.team;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Marcus on 3/27/2015.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
    static final String TAG = "GCMDemo";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        ctx = context;
        String messageType = gcm.getMessageType(intent);
        //if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
        //  sendNotification("Send error: " + intent.getExtras());
        //} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED .equals(messageType)) {
        //  sendNotification("Deleted messages on server: " + intent.getExtras());
        // } else {
        sendNotification(intent.getExtras());
        //
        setResultCode(Activity.RESULT_OK);
    }

    // Put the GCM message into a notification and post it.
    private void sendNotification(Bundle msg) {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);

        for (String key : msg.keySet()) {
            Object value = msg.get(key);
            Log.d(TAG, String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
        }
        String showmsg = "";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(msg.toString())
                        .setContentText("touch to view")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(showmsg))
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        long[] vibrate = {100,500,100,500};
        mBuilder.setVibrate(vibrate);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}


