package com.wgutermtracker.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.wgutermtracker.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CourseNotificationReceiver extends BroadcastReceiver {

    static int courseNotificationID;
    String channel_id = "course";

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context, channel_id);

        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(intent.getStringExtra("note"))
                .setContentTitle(intent.getStringExtra("title"))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);


    }

    private void createNotificationChannel(Context context, String CHANNEL_ID){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Courses";
            String description = "Receive notifications for Course start & end dates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}