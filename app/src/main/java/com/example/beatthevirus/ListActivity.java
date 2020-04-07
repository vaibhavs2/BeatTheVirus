package com.example.beatthevirus;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 6768;
    boolean mScanning;
    BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    ListAdapter listAdapter;
    RecyclerView recyclerView;
    List<list_Info> infoList;
    Uri notification;
    Ringtone ringtone;
    boolean BT_enabled=false;

    private ScanCallback mLeScanCallback = new ScanCallback() {


        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            int txPower;
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                //average tx power in android bluetooth (google)
               txPower=8;
            } else{
                txPower=result.getTxPower();
            }
          float  distance = getDistance(result.getRssi(),txPower);
                infoList.add(new list_Info(result.getDevice().getName(),distance));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });
                if(distance<2) //metre
                ringtone.play();
                else
                    ringtone.stop();



        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);

            Toast.makeText(ListActivity.this, "Problem in scanning",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        infoList = new ArrayList<>();
         bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(ListActivity.this, notification);
        recyclerView = findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (bluetoothAdapter.isEnabled()) {
            scanLeDevice(true);
        }
        else{
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        listAdapter =new ListAdapter(infoList,this);
            recyclerView.setAdapter(listAdapter);

//        Intent discoverableIntent =
//                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
//        startActivity(discoverableIntent);

//          https://altbeacon.github.io/android-beacon-library/distance-calculations2.html
//        https://altbeacon.github.io/android-beacon-library/distance-calculations.html
//          https://docs.google.com/spreadsheets/d/1ymREowDj40tYuA5CXd4IfC4WYPXxlx5hq1x8tQcWWCI/edit#gid=0
//        https://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing
//        https://stackoverflow.com/questions/29790853/get-tx-power-of-ble-beacon-in-android
//        https://stackoverflow.com/questions/36862185/what-exactly-is-txpower-for-bluetooth-le-and-how-is-it-used/36862382

    }


    private void scanLeDevice( boolean enable) {

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        if (enable) {
            bluetoothLeScanner.startScan(mLeScanCallback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }

    float getDistance(int rssi, int txPower) {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */

        return (float)Math.pow(10, ((float) txPower - rssi) / (10 * 2));
    }


    @Override
    protected void onStop() {
        super.onStop();
        ringtone.stop();
        bluetoothLeScanner.stopScan(mLeScanCallback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_ENABLE_BT && resultCode==RESULT_OK){

            Toast.makeText(ListActivity.this, "Bluetooth Enabled", Toast.LENGTH_LONG).show();
        }
    }
}
