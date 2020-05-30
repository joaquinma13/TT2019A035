package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecZona;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Adapter.AdpZonas;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class FrgZonas extends Fragment implements OnMapReadyCallback {

    private FloatingActionButton btnGetZona;
    private LocationLibrary ubicacion;
    private SharePreference preference;

    public static ArrayList<Item> itemsZonas = new ArrayList<>();
    public static RecyclerView recyclerZona;
    public static AdpZonas recyclerAdapter; /// el es el bueno


    //BottomSheet
    public static BottomSheetBehavior sheetBehavior;
    public static RelativeLayout bottom_sheet;
    private Button btnContinuar;
    public static Spinner spUnidades;
    public static ArrayList<String> arrayUnidades;

    //Mapa

    private SupportMapFragment mapaG;
    public static GoogleMap Map;
    private Button clean;
    private Button hiden;
    private Button marcar;
    private LatLng coordCamera = null;
    private boolean mapFlag = false;

    private TextInputLayout inputNombreZona;
    private TextInputEditText editNombreZona;
    private TextInputLayout inputRadio;
    private TextInputEditText editRadio;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObjects();
        initBinding(view);
        initControls();
        listener();
    }




    private void initObjects() {
        arrayUnidades = new ArrayList<>();
        arrayUnidades.add("metros");
        arrayUnidades.add("kilometros");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getZone();
            }
        }).start();
        ubicacion = new LocationLibrary(getContext(), "Mapa");
        preference = SharePreference.getInstance(getActivity());
    }

    private void initBinding(View view) {

        mapaG = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapZonas));
        mapaG.getExtendedMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.useViewLifecycleInFragment(true);
        mapaG = SupportMapFragment.newInstance(options);
        mapaG.setRetainInstance(true);

        inputNombreZona = view.findViewById(R.id.inputNombreZona);
        editNombreZona = view.findViewById(R.id.editNombreZona);
        inputRadio = view.findViewById(R.id.inputRadio);
        editRadio = view.findViewById(R.id.editRadio);

        bottom_sheet = view.findViewById(R.id.bottom_sheet_map);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        btnGetZona = view.findViewById(R.id.btnGetZona);
        btnContinuar = view.findViewById(R.id.btnContinuar);
        spUnidades = view.findViewById(R.id.spUnidades);
        clean = view.findViewById(R.id.clean);
        hiden = view.findViewById(R.id.hiden);
        marcar = view.findViewById(R.id.marcar);
        recyclerZona = view.findViewById(R.id.recyclerZona);
        recyclerZona.setHasFixedSize(true);
        recyclerZona.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new AdpZonas(getActivity(), itemsZonas);
        recyclerZona.setAdapter(recyclerAdapter);

    }

    private void initControls() {
        FrgZonas.MyAdapter myAdapter = new FrgZonas.MyAdapter(getActivity(), arrayUnidades);
        spUnidades.setAdapter(myAdapter);
        recyclerZona.setHasFixedSize(true);
        recyclerZona.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void listener() {
        btnGetZona.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(getContext(), ActCroquis.class);
                startActivity(intent);*/

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                btnGetZona.setVisibility(View.GONE);

            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                btnGetZona.setVisibility(View.VISIBLE);
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                boolean flag = true;


                if ( editNombreZona.getText().toString().trim().equalsIgnoreCase("")) {
                    inputNombreZona.setError("Nombre obligatorio");
                    flag = false;
                }
                if ( editRadio.getText().toString().trim().equalsIgnoreCase("")) {
                    inputRadio.setError("Radio obligatoria");
                    flag = false;
                }

                if (flag & mapFlag ){

                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    new WsRecZona(getActivity(), "master").setZona(preference.getStrData("id_user"),
                            preference.getStrData("id_user") + hourFormat.format(date),
                            editNombreZona.getText().toString(),
                            String.valueOf(coordCamera.latitude),
                            String.valueOf(coordCamera.longitude),
                            editRadio.getText().toString());
                    FrgZonas.Map.clear();
                    editNombreZona.setText("");
                    editRadio.setText("");
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);



                }else{
                    Alerter.create(getActivity())
                            .setTitle("Revise la informacion solicitada.")
                            .setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_warning))
                            .setBackgroundColorRes(R.color.red)
                            .setDuration(2000)
                            .show();
                }



            }
        });


        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coordCamera = ubicacion.getLocation().getUbicacion();
                FrgZonas.Map.clear();
                System.out.println("clean.setOnClickListener");
            }
        });

        hiden.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                btnGetZona.setVisibility(View.VISIBLE);
            }
        });

        marcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = getActivity().getLayoutInflater().inflate(R.layout.layout_marker, null);
                ImageView image = v.findViewById(R.id.place);
                image.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_place24dp));
                FrgZonas.Map.clear();
                FrgZonas.Map.addMarker(new MarkerOptions().position(new LatLng(coordCamera.latitude, coordCamera.longitude)).icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v))));
                mapFlag = true;


            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng myLocation = ubicacion.getLocation().getUbicacion();
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        FrgZonas.Map = googleMap;
        FrgZonas.Map.clear();
        FrgZonas.Map.setMyLocationEnabled(true);
        FrgZonas.Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.latitude, myLocation.longitude), 18));
        FrgZonas.Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {
                //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); esconder el bottom sheet
            }
        });
        FrgZonas.Map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMarkerClick(Marker marker) {


                return true;
            }
        });


        FrgZonas.Map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                System.out.println( "Center======================== taget: " + FrgZonas.Map.getCameraPosition().target);
                coordCamera = FrgZonas.Map.getCameraPosition().target;

            }
        });


    }

    private class MyAdapter extends BaseAdapter {

        private ArrayList<String> myList;
        private Activity parentActivity;
        private LayoutInflater inflater;

        public MyAdapter(Activity parent, ArrayList<String> l) {
            parentActivity = parent;
            myList = l;
            inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.row, null);

            TextView text2 = view.findViewById(R.id.text2);
            text2.setText(myList.get(position));
            return view;
        }
    }

    public void getZone(){
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " +
                    " *FROM " + DataBaseDB.TB_ZONAS, null);

            if (c.moveToFirst()) {
                do {
                    itemsZonas.add(new Item(c.getString(0),
                            c.getString(2),
                            Double.parseDouble(c.getString(3)),
                            Double.parseDouble(c.getString(4)),
                            c.getString(1)));
                } while (c.moveToNext());

            } else {
                System.out.println("No existen registros!!!");
            }

        } catch (Exception ex) {
            Log.e(TAG, "getCorreoPreregistro: " + ex.toString());
        } finally {
            Utils.close(c);
            Utils.close(db);
            Utils.freeMemory();
        }
    }

    public static Bitmap loadBitmapFromView(View v) {

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }
}
