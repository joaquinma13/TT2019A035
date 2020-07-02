package com.example.joaquin.tt_des_v_100.Api.backservices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecBitacora;
import com.example.joaquin.tt_des_v_100.R;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationService extends Service {

    private LocationManager locationManager;
    private double longitudeBest, latitudeBest;
    private SharePreference preference;
    private Permission permission;


    @SuppressLint({"MissingPermission", "HardwareIds"})
    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
            permission = new Permission(getApplicationContext());

            Log.i("Latitud service", "bestlatitude::" + longitudeBest);
            Log.i("Longitud service", "bestlatitude::" + latitudeBest);

            Date date = new Date();
            DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            //Calcular el porcentaje de bateria actual
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float) scale;


            if (permission.checkPermissionIMEI()) {
                WsRecBitacora sendBtacora = new WsRecBitacora( getBaseContext() );
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                sendBtacora.setBitacora(preference.getStrData("id_user"),String.valueOf(getMobileDataEstatus()),String.valueOf((int) (batteryPct * 100)),telephonyManager.getDeviceId(),Build.MODEL,String.valueOf(latitudeBest),String.valueOf(longitudeBest),hourFormat.format(date));

            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("LocationService", "Se inicio el servicio de localizacion");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        preference = SharePreference.getInstance(getApplicationContext());
        startLocationServiceUpdates();
        return Service.START_STICKY;


    }

    private int getWifiEstatus() {

        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



    private int getMobileDataEstatus() {
        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mData.isConnected()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize / 1048576L;
    }

    private long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize / 1048576L;
    }

    private long freeRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        return availableMegs;
    }

    private long totalRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.totalMem / 1048576L;
        return availableMegs;
    }

    @Override
    public void onDestroy() {
        stopLocationServiceUpdates();
        Log.e("Service Destroy", "El servicio fue destruido");
        super.onDestroy();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            Log.e("Service location", "La localizacion no esta habilitada");
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void stopLocationServiceUpdates() {
        Log.i("Se detuvo el servicio", "Se deshabilito el servicio de ubicacion");
        locationManager.removeUpdates(locationListenerBest);

    }

    public void startLocationServiceUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
            //locationManager.requestLocationUpdates(provider, 10000, 0, locationListenerBest);
            locationManager.requestLocationUpdates(provider, 5000, 0, locationListenerBest);
        } else {
            Log.e("Else ", "Las coordenadas son nullas");
        }
    }
}
