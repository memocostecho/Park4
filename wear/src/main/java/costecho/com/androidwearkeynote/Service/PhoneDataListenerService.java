package costecho.com.androidwearkeynote.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

import costecho.com.androidwearkeynote.MainActivity;
import costecho.com.androidwearkeynote.R;

/**
 * Created by guillermo.rosales on 08/09/15.
 */
public class PhoneDataListenerService extends WearableListenerService {

    private static final String TAG = "PhoneToWatch::";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onDataChanged: " + dataEvents);
        }


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);

        Notification myFullScreenNotification = new Notification.Builder(getApplicationContext())
                .extend(new Notification.WearableExtender()
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                        .setDisplayIntent(notificationPendingIntent))
                .build();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_full_sad)
                        .setContentTitle("Android Wear Keynote")
                        .setAutoCancel(true)
                        .setGroup("grouping.this")
                        .extend(new NotificationCompat.WearableExtender().addPage(myFullScreenNotification))
                ;

        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


    }
}

