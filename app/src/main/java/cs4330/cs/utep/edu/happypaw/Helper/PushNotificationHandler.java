package cs4330.cs.utep.edu.happypaw.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import cs4330.cs.utep.edu.happypaw.Activity.HomeActivity;
import cs4330.cs.utep.edu.happypaw.R;

public class PushNotificationHandler {
    Context ctx;

    public PushNotificationHandler(Context ctx){
        this.ctx = ctx;
    }

    public void showNotifications(String title, String msg) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx.getApplicationContext(), "notify_001");
        Intent i = new Intent(ctx.getApplicationContext(), HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, i, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.setBigContentTitle(title);
        bigText.setSummaryText(msg);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager manager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);


        manager.notify(0, mBuilder.build());

    }



}
