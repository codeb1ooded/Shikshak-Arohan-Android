package com.igdtuw.technotwisters.sih_android.OtherFiles;

/**
 * Created by shobhit on 19/3/17.
 */


import java.util.Calendar;
        import java.util.GregorianCalendar;

        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.TaskStackBuilder;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.activity.DashboardActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      Calendar now = GregorianCalendar.getInstance();
        int dayOfWeek = now.get(Calendar.DATE);
        if(dayOfWeek != 5 && dayOfWeek != 7) {
            Notification.Builder mBuilder =
                    new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("REMINDER")
                            .setContentText("Mark Your Attendance");
            Intent resultIntent = new Intent(context, DashboardActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(DashboardActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
