package com.example.joaquin.tt_des_v_100.Api.Class;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationLibrary {
    private static final double INIT_VALUE = -1_000_000.0;
    private static final double INVALID_COORDINATE = 0.0;

    private LocationManager locationManager;
    private double longitudeBest, latitudeBest = INIT_VALUE;
    private SharePreference preference;
    private Context context;
    private String provider;
    private String from;
    private Location locationActual;

    public LocationLibrary(Context context, String from) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        preference = SharePreference.getInstance(context);
        this.from = from;
        startLocationServiceUpdates();
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
            preference.saveData("LastLatitud", (float) location.getLatitude());
            preference.saveData("LastLongitud", (float) location.getLongitude());
            preference.saveData("lastProvider", location.getProvider());
            locationManager.removeUpdates(locationListenerBest);

            System.out.println("Se actualizo la ubicacion correctamente en el objeto LocationLibrary");


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


    public void startLocationServiceUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 0, 0, locationListenerBest);
            preference.saveData("lastProvider", provider);

        }
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public String getTime() {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "01/01/1111 11:11:11";
        }
        locationActual = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        String timeGps = "";

        if (locationActual != null) {
            DateFormat dateGPS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date locationDate = new Date(locationActual.getTime());
            timeGps = dateGPS.format(locationDate);
            System.out.println("GetTime():" + timeGps);
        }

        return timeGps;
    }

    public LocationProvider getLocation() {

        LocationProvider locationProvider = new LocationProvider();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationProvider.setUbicacion(new LatLng(0.1, 0.1));
            locationProvider.setProvider("Not permission");
            return locationProvider;
        }

        Location location = locationManager.getLastKnownLocation(preference.getStrData("lastProvider"));

        if (location != null) {

            if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {

                locationProvider.setUbicacion(new LatLng(location.getLatitude(), location.getLongitude()));
                locationProvider.setProvider(preference.getStrData("lastProvider"));
                return locationProvider;

            } else {

                locationProvider.setUbicacion(new LatLng(preference.getFloatData("LastLatitud"), preference.getFloatData("LastLongitud")));
                locationProvider.setProvider("Preferences");
                return locationProvider;

            }

        }


        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){

            if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {

                locationProvider.setUbicacion(new LatLng(location.getLatitude(), location.getLongitude()));
                locationProvider.setProvider(LocationManager.GPS_PROVIDER);
                return locationProvider;

            } else {

                locationProvider.setUbicacion(new LatLng(preference.getFloatData("LastLatitud"), preference.getFloatData("LastLongitud")));
                locationProvider.setProvider("Preferences");
                return locationProvider;

            }

        }

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null){

            if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {

                locationProvider.setUbicacion(new LatLng(location.getLatitude(), location.getLongitude()));
                locationProvider.setProvider(LocationManager.NETWORK_PROVIDER);
                return locationProvider;

            } else {

                locationProvider.setUbicacion(new LatLng(preference.getFloatData("LastLatitud"), preference.getFloatData("LastLongitud")));
                locationProvider.setProvider("Preferences");
                return locationProvider;

            }

        }

        locationProvider.setUbicacion(new LatLng(preference.getFloatData("LastLatitud"), preference.getFloatData("LastLongitud")));
        locationProvider.setProvider("Preferences");


        return locationProvider;

    }

}
