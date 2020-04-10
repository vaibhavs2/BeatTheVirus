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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Button start;
    private String[] permissions = {Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION};
    List<String> remainingPermissions;
 final int   MY_PERMISSIONS_REQUEST_BLUETOOTH = 7878;
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

        } else
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(MainActivity.this, "Bluetooth Low energy not supported", Toast.LENGTH_SHORT).show();
            finish();}

        else{
            requestMultiplePermissions();

        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                    startActivityForResult(discoverableIntent, 0);

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
                for(int i=0 ;i<grantResults.length ;i++){

                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
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

        switch (requestCode){
            case 0:
                if(resultCode!=RESULT_CANCELED)
                    { Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        startActivity(intent);
                    }
                else
                    Toast.makeText(MainActivity.this,"Can't be done!", Toast.LENGTH_SHORT).show();
                break;
        }

    }


}
