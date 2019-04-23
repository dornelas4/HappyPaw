package cs4330.cs.utep.edu.happypaw;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import cs4330.cs.utep.edu.happypaw.Fragment.LocationFragment;
import cs4330.cs.utep.edu.happypaw.Services.LocationTrackerService;


public class ServiceNotification extends BroadcastReceiver {

    private static final String TAG = ServiceNotification.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction(); //Get the button actions

        if (!TextUtils.isEmpty(action)) {
            int notifId = intent.getIntExtra(LocationFragment.EXTRA_NOTIFICATION_ID, 0);
            Log.v(TAG + "=======", "Notification id==" + notifId);
            Intent mintent = new Intent(context, LocationTrackerService.class);
            if (action.equalsIgnoreCase(LocationFragment.ACTION_STOP)) {
                Log.v(TAG + "=======", "Pressed YES");

                if (LocationTrackerService.isEnded) {
                    Log.v(TAG + "=======", "Service stopped.");
                    context.stopService(mintent);
                    //  Toast.makeText(context, "Service stopped.", Toast.LENGTH_SHORT).show();
                    cancelNotification(context, notifId);
                }
            }

        }

    }

    /**
     * Cancel the notification
     *
     * @param mContext
     * @param mnotinotifId
     */
    private void cancelNotification(Context mContext, int mnotinotifId) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(mnotinotifId);
    }

}
