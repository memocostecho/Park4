package costecho.com.androidwearkeynote.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import costecho.com.androidwearkeynote.MainActivity;
import costecho.com.androidwearkeynote.R;

/**
 * Created by leonelmendez on 02/08/15.
 */
public class WearBroadcastReceiver extends ParsePushBroadcastReceiver {


    public static final int NOTIFICATION_ID = 1;
    private NotificationManagerCompat mNotificationManager;

    @Override
    protected void onPushReceive(Context context, Intent intent) {

        JSONObject json;
        try {
            json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            sendTimeLineNotification(context,intent,json.get("message").toString());
            //sendTimeLineNotification(context, intent,json.get("city").toString()+json.get("currency").toString()+json.get("money").toString());
        } catch (JSONException e) {
            sendTimeLineNotification(context,intent,"Phone and wear notification with actions");
        }


    }


    public void sendTimeLineNotification(final Context context, Intent intent,String msg) {

        Intent payIntent = new Intent(context, MainActivity.class);
        final PendingIntent pendingAttendIntent =  PendingIntent.getActivity(context, 0, payIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = NotificationManagerCompat.from(context);
        Intent notifyIntent =
                new Intent(context, MainActivity.class);

// Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cast_ic_notification_connecting)
                        .setContentTitle("Android Wear Keynote")
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000, 1000})
                        .addAction(R.drawable.ic_media_play, "Reply", pendingAttendIntent)
                        .setContentText(msg).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(notifyPendingIntent)

                ;

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());





    }

}
