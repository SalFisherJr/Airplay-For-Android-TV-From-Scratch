package com.github.sallyfisher.airplayforandroidtvfromscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import com.github.sallyfisher.airplayforandroidtvfromscratch.helper.DeviceHelper;
import com.github.sallyfisher.airplayforandroidtvfromscratch.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDeviceInfo();
    }

    @SuppressLint("SetTextI18n")
    private void displayDeviceInfo(){
        TextView textViewDeviceName = (TextView) findViewById(R.id.deviceName);
        textViewDeviceName.setText(DeviceHelper.getDeviceName());

        TextView textViewWifiInfo = (TextView) findViewById(R.id.wifiInfo);
        if (NetworkUtils.isWifiConnected(this)){
            textViewWifiInfo.setText("Connection successful!");
        } else {
            textViewWifiInfo.setText("Connection failed!");
        }
    }
}

