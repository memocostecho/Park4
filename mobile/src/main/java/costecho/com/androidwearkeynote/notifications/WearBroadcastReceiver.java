package costecho.com.androidwearkeynote.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import costecho.com.androidwearkeynote.MainActivity;
import costecho.com.androidwearkeynote.R;

/**
 * Created by leonelmendez on 02/08/15.
 */
public class WearBroadcastReceiver extends ParsePushBroadcastReceiver {


    @Override
    protected void onPushReceive(Context context, Intent intent) {

        JSONObject json;
        try {
            json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            sendTimeLineNotification(context, intent, json.get("message").toString());
        } catch (JSONException e) {
            sendTimeLineNotification(context, intent, "Phone and wear notification with actions");

        }


    }


    public void sendTimeLineNotification(final Context context, Intent intent,String msg) {

        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);

        String EXTRA_VOICE_REPLY = "extra_voice_reply";
        String[] replyChoices = context.getResources().getStringArray(R.array.talking_options);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel("Habla")
                .setChoices(replyChoices)
                .build();

        Intent replyIntent = new Intent(context, MainActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(context, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_cast_dark,
                        "Habla", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        Intent payIntent = new Intent(context, MainActivity.class);
        final PendingIntent pendingAttendIntent =  PendingIntent.getActivity(context, 0, payIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent notifyIntent =
                new Intent(context, MainActivity.class);


        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle("Page 2")
                .bigText("A lot of text...");
        Notification secondPageNotification =
                new NotificationCompat.Builder(context)
                        .setStyle(secondPageStyle)
                        .build();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cast_ic_notification_connecting)
                        .setContentTitle("Android Wear Keynote")
                        .setAutoCancel(true)
                        .setGroup("grouping.this")
                        .extend(new NotificationCompat.WearableExtender().addAction(action).addPage(secondPageNotification))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000, 1000})
                        .addAction(R.drawable.ic_media_play, "Reply", pendingAttendIntent)
                        .setContentText(msg).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(notifyPendingIntent)
                ;

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, mBuilder.build());

    }



}
