package com.example.beatthevirus;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.le.BluetoothLeScanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 6768;
    boolean mScanning;
    BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    ListAdapter listAdapter;
    RecyclerView recyclerView;
    List<list_Info> infoList;
    ToneGenerator toneGen1;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MAX_VALUE);
               int distance =(int) getDistance(rssi, 70);
               infoList.add(new list_Info(device.getName(),distance));
               if(distance<2)
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,700);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        listAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        infoList = new ArrayList<>();
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        recyclerView = findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listAdapter =new ListAdapter(infoList,this);
            recyclerView.setAdapter(listAdapter);
        bluetoothAdapter.startDiscovery();

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      infoList.clear();
                                      bluetoothAdapter.startDiscovery();
                                  }

                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                0,
//Set the amount of time between each execution (in milliseconds)
                13000);



//        Intent discoverableIntent =
//                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
//        startActivity(discoverableIntent);


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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        bluetoothAdapter.cancelDiscovery();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_ENABLE_BT && resultCode==RESULT_OK){

            Toast.makeText(ListActivity.this, "Bluetooth Enabled", Toast.LENGTH_LONG).show();
        }
    }



}
