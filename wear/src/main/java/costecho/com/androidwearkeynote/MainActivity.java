package costecho.com.androidwearkeynote;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.larswerkman.holocolorpicker.ColorPicker;

public class MainActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnItemSelectedListener {


    private GoogleApiClient mGoogleApiClient;
    private ColorPicker colorPicker;
    private Spinner letter;
    private Spinner number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setApiClient();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                colorPicker = (ColorPicker) stub.findViewById(R.id.picker);
                letter = (Spinner) stub.findViewById(R.id.letter_spinner);
                number = (Spinner) stub.findViewById(R.id.number_spinner);
                number.setOnItemSelectedListener(MainActivity.this);


            }
        });


    }


    public void sendInfo(){

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/letter");
        putDataMapReq.getDataMap().putString("parking.letter", letter.getSelectedItem().toString() + " " +number.getSelectedItem().toString());
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    Log.d("WEAR", "Data item set: " + result.getDataItem().getUri());
                }
                }
            });

        PutDataMapRequest putDataMapIntReq = PutDataMapRequest.create("/color");
        putDataMapIntReq.getDataMap().putInt("parking.color", colorPicker.getColor());
        PutDataRequest putDataIntReq = putDataMapIntReq.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataIntReq);

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
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sendInfo();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
