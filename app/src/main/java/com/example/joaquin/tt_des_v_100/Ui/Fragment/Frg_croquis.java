package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.R;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

//import com.androidmapsextensions.GoogleMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_croquis extends Fragment implements GoogleApiClient.ConnectionCallbacks,
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


    public Frg_croquis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zona, container, false);
    }

    @Override
    public void onDestroyView() {

        try {
            map.clear();
            getFragmentManager().beginTransaction().remove(this).commitNowAllowingStateLoss();
        } catch (Exception e) {
            Log.e(TAG, "onDestroyView: " + e.getMessage());
        } finally {
            Utils.freeMemory();
        }

        super.onDestroyView();

    }

    private void clearResourcesFrgEditarCoord() {
        //TODO: Revisar los recursos clave, es decir, revisar que recursos pueden ser "nulleables".
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preference = SharePreference.getInstance(getActivity());
        frag_editar = this;

        textLat = (TextView) view.findViewById(R.id.lat);
        textLong = (TextView) view.findViewById(R.id.lon);
        textLongActual = (TextView) view.findViewById(R.id.lonActual);
        textLatActual = (TextView) view.findViewById(R.id.latActual);
        mostrarDirecion = (TextView) view.findViewById(R.id.mostrardireccion);
        cambiarCoordenadas = (FloatingActionButton) view.findViewById(R.id.cambiarCoordenadas);
        Button buscar = (Button) view.findViewById(R.id.bucar);
        Button marcar = (Button) view.findViewById(R.id.marcar);
        Button miUbicacion = (Button) view.findViewById(R.id.miUbicacion);
        Button clean = (Button) view.findViewById(R.id.clean);
        direccion = (EditText) view.findViewById(R.id.direccion);
        direccion.setText("");

        miUbicacion.setVisibility(View.GONE);


        //SI LA PREGUNTA ES EDITABLE, SE ACTIVARA EL BOTON DE GUARDAR COORDENADA, POR LO CONTRARIO, NO SE PODRA.
        if (getArguments() != null) {
            if (getArguments().getInt("EDITION_PERMISSION") == 0)
                cambiarCoordenadas.setVisibility(View.GONE);
            else
                cambiarCoordenadas.setVisibility(View.VISIBLE);
        }


        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.clear();
                float zoom = 17f;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), zoom);
                map.animateCamera(cameraUpdate);
            }
        });

        cambiarCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newCoordenadas == null) {
                    final CustomAlert alert = new CustomAlert(getActivity());
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
                }
            }
        });

        miUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!direccion.getText().toString().trim().equalsIgnoreCase("")) {
                    mostrarDirecion.setText("Buscando...");
                    searchPattern = direccion.getText().toString();
                    try {
                        //trying to get all possible addresses by search pattern
                        addresses = (new Geocoder(v.getContext())).getFromLocationName(searchPattern, Integer.MAX_VALUE);
                        System.out.println("Direccione encontradas: ===================" + addresses.size());

                    } catch (IOException e) {
                    }
                    if (addresses == null) {
                        // location service unavailable or incorrect address
                        // so returns null
                        mostrarDirecion.setText("No se encontro la direccion que especificaste");

                    } else if (addresses.size() != 0) {
                        map.clear();
                        String direcciones = "";
                        for (int i = 0; i < addresses.size(); i++) {
                            String lat = addresses.get(i).getLatitude() + "";
                            String lon = addresses.get(i).getLongitude() + "";

                            direcciones += "Latitud: " + lat.substring(0, 6) + ",  Longitud: " + lon.substring(0, 6) + "\n";

                        }
                        String title = direccion.getText().toString();

                        for (int i = 0; i < addresses.size(); i++) {
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(addresses.get(i).getLatitude(), addresses.get(i).getLongitude()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                    .title(title);


                            if (map != null) {
                                //locationMarker.remove();
                                Marker locationMarker = map.addMarker(markerOptions);
                                System.out.println("Termino el marker");
                            }
                            if (i == 0) {
                                newCoordenadas = new LatLng(addresses.get(i).getLatitude(), addresses.get(i).getLongitude());
                                System.out.println("Entro a if de primera direcion");

                            }

                        }

                        //mostrarDirecion.setText(direcciones);
                        float zoom = 17f;
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()), zoom);
                        map.animateCamera(cameraUpdate);


                    } else {
                        mostrarDirecion.setText("No se encontró la dirección que especificaste");

                    }
                } else {
                    mostrarDirecion.setText("Por favor, ingresa un dirección");
                }
            }
        });

        direccion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Comprobamos que se ha pulsado la tecla enter.
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.i(TAG, "BUSCANDO");
                    mostrarDirecion.setText("Buscando...");
                    if (direccion.getText().toString().length() != 0) {
                        searchPattern = direccion.getText().toString();
                        try {
                            //trying to get all possible addresses by search pattern
                            addresses = (new Geocoder(v.getContext())).getFromLocationName(searchPattern, Integer.MAX_VALUE);
                            System.out.println("Direccione encontradas: ===================" + addresses.size());
                        } catch (IOException e) {
                        }
                        if (addresses == null) {
                            // location service unavailable or incorrect address
                            // so returns null
                            mostrarDirecion.setText("No se encontro la direccion que especificaste");

                        } else if (addresses.size() != 0) {
                            String direcciones = "";
                            for (int i = 0; i < addresses.size(); i++) {
                                String lat = addresses.get(i).getLatitude() + "";
                                String lon = addresses.get(i).getLongitude() + "";

                                direcciones += "Latitud: " + lat.substring(0, 6) + ",  Longitud: " + lon.substring(0, 6) + "\n";

                            }
                            String title = direccion.getText().toString();

                            for (int i = 0; i < addresses.size(); i++) {

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng(addresses.get(i).getLatitude(), addresses.get(i).getLongitude()))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                        .title(title);

                                if (map != null) {
                                    //locationMarker.remove();
                                    Marker locationMarker = map.addMarker(markerOptions);
                                    System.out.println("Termino el marker");
                                }
                                if (i == 0) {
                                    newCoordenadas = new LatLng(addresses.get(i).getLatitude(), addresses.get(i).getLongitude());
                                    System.out.println("Entro a if de primera direcion");

                                }

                            }

                            //mostrarDirecion.setText(direcciones);
                            float zoom = 17f;
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()), zoom);
                            map.animateCamera(cameraUpdate);


                        } else {
                            mostrarDirecion.setText("!No se encontró la dirección que especificaste¡");

                        }
                    } else {
                        mostrarDirecion.setText("Por favor, ingresa un dirección");
                        //direccion.setText("");
                    }


                    return true;
                } else {
                    System.out.println("entro en else");
                }
                return false;
            }
        });
        // initialize GoogleMaps
        initGMaps();

        // create GoogleApiClient
        createGoogleApi();
    }

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate( R.menu.main_menu, menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
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

    private void initGMaps() {

        mapaG = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapEditar));
        mapaG.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.useViewLifecycleInFragment(true);


        mapaG = SupportMapFragment.newInstance(options);
        mapaG.setRetainInstance(true);
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
        textLat.setText("Lat: " + location.getLatitude());
        textLong.setText("Long: " + location.getLongitude());
        //markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        if (preference.getBooData("flagfirstTime") && !preference.getBooData("flagEditFromMarker")) {
            float zoom = 17f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom);
            map.animateCamera(cameraUpdate);
            preference.saveData("flagfirstTime", false);
        } else if (!preference.getBooData("flagfirstTime") && !preference.getBooData("flagEditFromMarker")) {
            float zoom = 17f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom);
            map.animateCamera(cameraUpdate);

        }
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
        float zoom = 17f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), zoom);
        map.animateCamera(cameraUpdate);

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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
        map.setMyLocationEnabled(true);

    }

    @Override
    public void onResume() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {




                    final CustomAlert alert = new CustomAlert(getActivity());
                    alert.setTypeWarning("Atención","¿Estás seguro de que quieres salir?","Cancelar","Aceptar");
                    alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.close();
                        }
                    });
                    alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.close();


                            if (getArguments().getInt("TYPE") == 1){
                                getActivity().finish();
                            }else{
                                if (getFragmentManager().getBackStackEntryCount() > 0) {
                                    getFragmentManager().popBackStack();
                                }

                            }
                        }
                    });
                    alert.show();
                    return true;
                }
                return false;
            }
        });
        super.onResume();
    }





}
