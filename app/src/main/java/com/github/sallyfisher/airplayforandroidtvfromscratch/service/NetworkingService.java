package com.github.sallyfisher.airplayforandroidtvfromscratch.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.github.sallyfisher.airplayforandroidtvfromscratch.constant.Constant;
import com.github.sallyfisher.airplayforandroidtvfromscratch.utils.NetworkUtils;
import com.github.sallyfisher.airplayforandroidtvfromscratch.R;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Locale;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class NetworkingService extends Service {
    private static final String airplayType  = "._airplay._tcp.local";
    private static final String raopType     = "._raop._tcp.local";
    private static final String tag          = NetworkingService.class.getSimpleName();

    private String airplayName;
    private InetAddress localAddress;
    private JmDNS jmdnsAirplay;
    private JmDNS jmdnsRaop;
    private ServiceInfo airplayService;
    private ServiceInfo raopService;
    private HashMap<String, String> values;
    private String preMac;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "onCreate");

        airplayName = Build.MODEL + "@" + getString(R.string.app_name);
        new Thread() {
            public void run() {
                try {
                    registerAirplay();
                }
                catch (IOException e) {
                    Log.e(tag, "problem initializing HTTP server and Bonjour services", e);

                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "onDestroy");

        shutdown();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerAirplay() throws IOException {
        if (!getServiceInfoParams()) {
            Log.d(tag, "Bonjour services NOT successfully registered");
        }
        else {
            Log.d(tag, "Beginning registration of Bonjour services..");
            registerTcpLocal();
            registerRaopLocal();
            Log.d(tag, "Bonjour services successfully registered");
        }
    }

    private void unregisterAirplay() {
        Log.d(tag, "Unregistering Bonjour services");

        if (jmdnsAirplay != null) {
            try {
                jmdnsAirplay.unregisterService(airplayService);
                jmdnsAirplay.close();
            }
            catch (IOException e) {
                Log.e(tag, "problem shutting down Bonjour service (AirPlay)", e);
            }
            finally {
                jmdnsAirplay = null;
            }
        }

        if (jmdnsRaop != null) {
            try {
                jmdnsRaop.unregisterService(raopService);
                jmdnsRaop.close();
            }
            catch (IOException e) {
                Log.e(tag, "problem shutting down Bonjour service (RAOP)", e);
            }
            finally {
                jmdnsRaop = null;
            }
        }
    }

    private void registerTcpLocal() throws IOException {
        airplayService = ServiceInfo.create(airplayName + airplayType, airplayName, Constant.AIRPLAY_PORT, 0, 0, values);
        jmdnsAirplay = JmDNS.create(localAddress); //create must bind IP address (android 4.0+)
        jmdnsAirplay.registerService(airplayService);
    }

    private void registerRaopLocal() throws IOException {
        String raopName = preMac + "@" + airplayName;
        raopService = ServiceInfo.create(raopName + raopType, raopName, Constant.RAOP_PORT, "tp=UDP sm=false sv=false ek=1 et=0,1 cn=0,1 ch=2 ss=16 sr=44100 pw=false vn=3 da=true md=0,1,2 vs=103.14 txtvers=1");
        jmdnsRaop = JmDNS.create(localAddress);
        jmdnsRaop.registerService(raopService);
    }

    private boolean getServiceInfoParams() {
        String strMac = null;

        try {
            Thread.sleep(2 * 1000);
        }
        catch (InterruptedException e) {
            Log.e(tag, "problem putting thread to sleep to allow HTTP server time to initialize prior to registering Bonjour services", e);
        }

        if (localAddress == null)
            localAddress = NetworkUtils.getLocalIpAddress(); //Get local IP object

        if (localAddress == null) {
            Log.d(tag, "No local IP address found for any network interface that supports multicast");
            return false;
        }

        String[] str_Array = new String[2];
        try {
            str_Array = NetworkUtils.getMACAddress(localAddress);
            if (str_Array == null)
                return false;

            strMac = str_Array[0].toUpperCase(Locale.ENGLISH);
            preMac = str_Array[1].toUpperCase(Locale.ENGLISH);
        }
        catch (Exception e) {
            Log.e(tag, "problem determining MAC address of network interface", e);
            return false;
        }
        Log.d(tag, "Registering Bonjour services to: IP address = " + localAddress.getHostAddress() + "; MAC address = " + strMac + "; preMac = " + preMac);

        values = new HashMap<String, String>();
        values.put("deviceid", strMac);
        values.put("features", "0x297f");
        values.put("model",    "AppleTV2,1");
        values.put("srcvers",  "130.14");

        return true;
    }

    private void shutdown() {
        new Thread() {
            public void run() {
                try {
                    unregisterAirplay();
                }
                catch (Exception e) {
                    Log.e(tag, "problem shutting down HTTP server and Bonjour services", e);
                }
                finally {
                    // wait a few moments to allow cleanup to complete, then quit the program by forcefully killing the process.
                    // the only cleanup that may occur is the deletion of all downloaded files from the cache directory.
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Process.killProcess(Process.myPid());
                        }
                    }, 7500);
                }
            }
        }.start();
    }

}