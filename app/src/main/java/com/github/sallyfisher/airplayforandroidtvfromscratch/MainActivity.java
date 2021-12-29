package com.github.sallyfisher.airplayforandroidtvfromscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.github.sallyfisher.airplayforandroidtvfromscratch.helper.DeviceHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textViewDeviceName = (TextView) findViewById(R.id.deviceName);
        textViewDeviceName.setText(DeviceHelper.getDeviceName());
    }
}

