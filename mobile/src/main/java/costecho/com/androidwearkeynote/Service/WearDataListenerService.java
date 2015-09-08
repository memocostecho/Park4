package costecho.com.androidwearkeynote.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by guillermo.rosales on 07/09/15.
 */
public class WearDataListenerService extends WearableListenerService {

    private static final String TAG = "WearToPhone";
    private SharedPreferences sharedPref;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onDataChanged: " + dataEvents);
        }

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/letter") == 0) {
                    storeNumberLetter(DataMapItem.fromDataItem(item).getDataMap().getString("parking.letter"));

                }else if(item.getUri().getPath().compareTo("/color")==0){
                    storeColor(DataMapItem.fromDataItem(item).getDataMap().getInt("parking.color"));


                }
            }
        }
    }

    public void storeNumberLetter(String letters){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("parking.letters", letters);
        editor.commit();
    }

    public void storeColor(int color){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("parking.color", color);
        editor.commit();

    }

    public String getNumberLetter(){
        return sharedPref.getString("parking.letters", "A0");
    }

    public int getColor(){
        return sharedPref.getInt("parking.color",0);

    }

}
