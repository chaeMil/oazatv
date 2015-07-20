package com.chaemil.hgms.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.chaemil.hgms.activity.AudioPlayer;
import com.chaemil.hgms.R;

/**
 * Created by chaemil on 8.12.14.
 */
public class NotificationPanel {

    private Context context;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;

    public NotificationPanel(Context context, String title) {
        this.context = context;

        Intent resultIntent = new Intent(context, AudioPlayer.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_white_logo)
                .setContentIntent(resultPendingIntent)
                .setOngoing(true);

        remoteView = new RemoteViews(context.getPackageName(), R.layout.audio_player_notification);

        //set the button listeners
        setupNotification(remoteView, title);
        nBuilder.setContent(remoteView);

        nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(2, nBuilder.build());
    }

    public void setupNotification(RemoteViews view, String title){
        //listener 1
        Intent volume = new Intent(context,NotificationReturnSlot.class);
        volume.putExtra("DO", "pause");
        PendingIntent pause = PendingIntent.getActivity(context, 0, volume, 0);
        view.setOnClickPendingIntent(R.id.pause, pause);

        //listener 2
        Intent stop = new Intent(context, NotificationReturnSlot.class);
        stop.putExtra("DO", "play");
        PendingIntent play = PendingIntent.getActivity(context, 1, stop, 0);
        view.setOnClickPendingIntent(R.id.play, play);


        view.setTextViewText(R.id.title, title);
    }

    public void notificationCancel() {
        nManager.cancel(2);
    }
}
