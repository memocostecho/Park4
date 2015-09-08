package costecho.com.androidwearkeynote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    RelativeLayout container;
    TextView parkingInfo;
    String EXTRA_VOICE_REPLY = "extra_voice_reply";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref= getPreferences(Context.MODE_PRIVATE);
        container = (RelativeLayout)findViewById(R.id.parking_container);
        parkingInfo = (TextView)findViewById(R.id.datos_parking);
        setApiClient();
        getMessageText(getIntent());
        container.setBackgroundColor(getColor());
        parkingInfo.setText(getNumberLetter());
    }


    public void setApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_custom_notification) {
            sendInfo();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
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


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onStart() {
        super.onStart();

    }



    public void sendInfo(){

        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);

        PutDataMapRequest putDataMapIntReq = PutDataMapRequest.create("/notification");
        putDataMapIntReq.getDataMap().putString("notification.custom", last4Str);
        PutDataRequest putDataIntReq = putDataMapIntReq.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataIntReq);

    }



    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {

            CharSequence sequence = remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
            Toast.makeText(this,sequence,Toast.LENGTH_LONG).show();
            return sequence;
        }

        return null;
    }


}
