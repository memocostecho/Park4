package costecho.com.androidwearkeynote.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import costecho.com.androidwearkeynote.MainActivity;
import costecho.com.androidwearkeynote.R;

/**
 * Created by guillermo.rosales on 08/09/15.
 */
public class PhoneDataListenerService extends WearableListenerService {

    private static final String TAG = "PhoneToWatch::";
    int color;



    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onDataChanged: " + dataEvents);
        }


        /*for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/letter") == 0) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.mipmap.ic_p4)
                                    .setContentTitle(DataMapItem.fromDataItem(item).getDataMap().getString("parking.letter"))
                                    .setAutoCancel(true)
                                    .setGroup("grouping.this")
                                    .setColor(color);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());

                }else if(item.getUri().getPath().compareTo("/color")==0){

                    color = DataMapItem.fromDataItem(item).getDataMap().getInt("parking.color");

                }
            }
        }*/
    }

}

