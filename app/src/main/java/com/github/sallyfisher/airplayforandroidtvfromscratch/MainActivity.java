package com.github.sallyfisher.airplayforandroidtvfromscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.github.sallyfisher.airplayforandroidtvfromscratch.helper.DeviceHelper;
import com.github.sallyfisher.airplayforandroidtvfromscratch.service.NetworkingService;
import com.github.sallyfisher.airplayforandroidtvfromscratch.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    final static String VIDEO_URL_KEY =
            "com.github.sallyfisher.airplayforandroidtvfromscratch.VIDEO.URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String VIDEO_URL = "https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDeviceInfo();
        startNetworkingService();
        playVideo(VIDEO_URL);
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

    private void playVideo(String videoUrl){
        Intent k = new Intent(this, VideoPlayerActivity.class);
        k.putExtra(VIDEO_URL_KEY, videoUrl);
        startActivity(k);
    }

    private void startNetworkingService() {
        Intent intent = new Intent(this, NetworkingService.class);
        this.startService(intent);
    }

}

