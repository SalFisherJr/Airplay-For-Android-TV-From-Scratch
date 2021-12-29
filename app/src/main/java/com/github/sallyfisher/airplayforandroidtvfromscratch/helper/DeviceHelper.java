package com.github.sallyfisher.airplayforandroidtvfromscratch.helper;


/*
* This helper aims to interact with the Android device (TV)
* */
public class DeviceHelper {
    public static String getDeviceName(){
        return android.os.Build.MODEL;
    }
}
