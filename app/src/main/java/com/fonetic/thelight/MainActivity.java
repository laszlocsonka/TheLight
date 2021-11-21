package com.fonetic.thelight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView battery;
    private TextView lowBattery;
    ImageButton close;
    static Camera camera = null;

    private ImageButton switchOff, switchOn;
    boolean hasCameraFlash = false;
    boolean flashOn = false;
    //private BroadcastReceiver batteryLevelReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //
        setContentView(R.layout.activity_main);

        switchOff = findViewById(R.id.powerOff_btn);
        switchOn = findViewById(R.id.powerOn_btn);
        close = findViewById(R.id.close_btn);
        battery = findViewById(R.id.batteryLevel);
        lowBattery = findViewById(R.id.lowBatteryText);

        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        switchOff.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(hasCameraFlash){
                    if(flashOn){
                        flashOn = false;
                        switchOn.setVisibility(View.GONE);
                        switchOff.setVisibility(View.VISIBLE);
                        try {
                            flashLightOff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }else{
                        flashOn = true;
                        switchOn.setVisibility(View.VISIBLE);
                        switchOff.setVisibility(View.GONE);
                        try {
                            flashLightOn();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this,"No flash available on your device",Toast.LENGTH_LONG).show();
                }
            }
        });

        switchOn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(hasCameraFlash){
                    if(flashOn){
                        flashOn = false;
                        switchOn.setVisibility(View.GONE);
                        switchOff.setVisibility(View.VISIBLE);
                        try {
                            flashLightOff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }else{
                        flashOn = true;
                        switchOn.setVisibility(View.VISIBLE);
                        switchOff.setVisibility(View.GONE);
                        try {
                            flashLightOn();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this,"No flash available on your device",Toast.LENGTH_LONG).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        battery = (TextView) this.findViewById(R.id.batteryLevel);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryPct = level * 100 / (Integer)scale;
            battery.setText(String.valueOf(batteryPct) + "%");

            lowBattery.setVisibility(View.GONE);
            if(batteryPct<20){
                lowBattery.setText("Low Battery Level");
                lowBattery.setVisibility(View.VISIBLE);
            }else{
                lowBattery.setVisibility(View.GONE);
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOn() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, true);
        Toast.makeText(MainActivity.this, "FlashLight is ON", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId, false);
        Toast.makeText(MainActivity.this, "FlashLight is OFF", Toast.LENGTH_SHORT).show();
    }

}

