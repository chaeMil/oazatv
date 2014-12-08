package com.chaemil.hgms;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by chaemil on 8.12.14.
 */
public class NotificationPanel {

    private Context context;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;

    public NotificationPanel(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        nBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("")
                .setSmallIcon(R.drawable.ic_white_logo)
                .setOngoing(true);

        remoteView = new RemoteViews(context.getPackageName(), R.layout.audio_player_notification);

        //set the button listeners
        setListeners(remoteView);
        nBuilder.setContent(remoteView);

        nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(2, nBuilder.build());
    }

    public void setListeners(RemoteViews view){
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
    }

    public void notificationCancel() {
        nManager.cancel(2);
    }
}
