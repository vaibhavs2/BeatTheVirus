package com.example.beatthevirus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Button start;
    private String[] permissions = {Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN};
    List<String> remainingPermissions;
 final int   MY_PERMISSIONS_REQUEST_BLUETOOTH = 7878;
    final int REQUEST_ENABLE_BT=5344;
    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=findViewById(R.id.start_btn);
        bluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("You don't have Bluetooth hardware, Can't proceed");
            builder.setTitle("System Error").create().show();
            // Use this check to determine whether BLE is supported on the device. Then
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(MainActivity.this, "Bluetooth version 4 not supported", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else{
            requestMultiplePermissions();

        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else{
               Intent intent = new Intent(MainActivity.this, ListActivity.class);
               startActivity(intent);}

            }
        });

    }

    private void requestMultiplePermissions(){
        remainingPermissions  = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        if(remainingPermissions.size()>0)
        ActivityCompat.requestPermissions(MainActivity.this, remainingPermissions.toArray(new String[remainingPermissions.size()]) ,MY_PERMISSIONS_REQUEST_BLUETOOTH);
        else{


        }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_BLUETOOTH: {
                for(int i=grantResults.length ;i>0 ;i--){

                    if(grantResults[i-1] != PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissions[i])){
                            new AlertDialog.Builder(this).setTitle("Important Request")
                                    .setMessage("As we are using bluetooth to measure distance, please permit these requests")
                                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestMultiplePermissions();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,"Cannot be done", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .create()
                                    .show();
                        }

                    }
                }
            }


        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_ENABLE_BT && resultCode==RESULT_OK){
            Toast.makeText(MainActivity.this, "Bluetooth Enabled", Toast.LENGTH_LONG).show();
        }
    }


}
