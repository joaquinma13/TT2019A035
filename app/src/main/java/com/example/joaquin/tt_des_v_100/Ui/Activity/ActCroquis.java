package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.Frg_croquis;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ActCroquis extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {

    private static final String TAG = Frg_croquis.class.getSimpleName();
    public static GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private double objetiveLat, objetiveLong, objetiveAlt;
    public static Fragment frag_editar;
    public static CustomAlert edit_alert;
    private SupportMapFragment mapaG;
    private double longitudeBest, latitudeBest;

    private TextView textLat, textLong, mostrarDirecion, textLatActual, textLongActual;
    private EditText direccion;
    String searchPattern = "";
    List<Address> addresses = null;
    private final int REQ_PERMISSION = 999;
    private boolean flagZoom = false;
    public static double altitud;

    private CameraPosition cameraPos;

    private LatLng newCoordenadas;
    private FloatingActionButton cambiarCoordenadas;
    private SQLiteDatabase db = null;   // Objeto para usar la base de datos local

    private SharePreference preference;
    private Marker newMarker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_croquis);

        textLat = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.lon);
        textLongActual = (TextView) findViewById(R.id.lonActual);
        textLatActual = (TextView) findViewById(R.id.latActual);
        mostrarDirecion = (TextView) findViewById(R.id.mostrardireccion);
        cambiarCoordenadas = findViewById(R.id.cambiarCoordenadas);
        Button buscar = (Button) findViewById(R.id.bucar);
        Button marcar = findViewById(R.id.marcar);
        Button miUbicacion = findViewById(R.id.miUbicacion);
        Button clean = findViewById(R.id.clean);
        direccion = findViewById(R.id.direccion);
        direccion.setText("");

        mapaG = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEditar);


        cambiarCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("me voy!!!!");

                finish();

                /*if (newCoordenadas == null) {
                    final CustomAlert alert = new CustomAlert(ActCroquis.this);
                    alert.setTypeWarning("Sin coordenadas editadas", "No has establecido tu ubicación", "Aceptar");
                    alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.close();
                        }
                    });
                    alert.getBtnRight().setVisibility(View.GONE);
                    alert.show();

                } else {
                }*/
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.clear();
                float zoom = 17f;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), zoom);
                map.animateCamera(cameraUpdate);
            }
        });

        marcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.clear();
                System.out.println("presiono el marker");
                String title = cameraPos.target.latitude + ", " + cameraPos.target.longitude;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(cameraPos.target.latitude, cameraPos.target.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .title(title);
                newCoordenadas = new LatLng(cameraPos.target.latitude, cameraPos.target.longitude);

                System.out.println();

                if (map != null) {
                    //locationMarker.remove();
                    newMarker = map.addMarker(markerOptions);
                    System.out.println("Termino el marker");
                }
            }
        });
        Log.i(TAG, "onCreate()");

        initGMaps();
        createGoogleApi();

    }



    //////////////////////////////////////////

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void initGMaps() {
        mapaG.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.useViewLifecycleInFragment(true);


        mapaG = SupportMapFragment.newInstance(options);
        mapaG.setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }



    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        int UPDATE_INTERVAL = 1000;
        int FASTEST_INTERVAL = 900;
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        System.out.println("=================================================== se cam");
        writeActualLocation(location);
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " + "Long: " + lastLocation.getLongitude() + " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private void writeActualLocation(Location location) {
        float zoom = 17f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom);
        map.animateCamera(cameraUpdate);
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private Marker locationMarker;

    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");

        getLastKnownLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");

        map = googleMap;
        map.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                System.out.println( "Center======================== taget: " + cameraPosition.target);
                cameraPos = cameraPosition;
            }
        });
        map.setOnMapClickListener(this);
        map.setBuildingsEnabled(true);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                newCoordenadas = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude );
                System.out.println("Coordenadas del marquer guardadas");
                System.out.println("Latitud Guardada: " + newCoordenadas.latitude);
                System.out.println("Longitud Guardada: " + newCoordenadas.longitude);
                return false;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
