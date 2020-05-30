package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsEnviaEvento;
import com.example.joaquin.tt_des_v_100.Api.Service.WsNotification;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecZona;
import com.example.joaquin.tt_des_v_100.Api.Service.WsSolicitaUser;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActCam;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActWebPhoto;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import static android.content.ContentValues.TAG;


public class FrgMap extends Fragment  implements OnMapReadyCallback {

    private FloatingActionButton btnEvento;
    private Button cleanMap;
    private Button marcarMap;
    private View viewVertical;
    private View viewHorizontal;

    private SupportMapFragment mapaG;
    public static GoogleMap Map;
    private LocationLibrary ubicacion;
    private SharePreference preference;


    public static Spinner spContacto;
    public static ArrayList<String> arrayContacto;
    private Map<String, String> hasMapContacto = new HashMap<>();

    //BottomSheet
    private LinearLayout linearDatos;
    public static BottomSheetBehavior sheetBehavior;
    public static RelativeLayout bottom_sheet;
    private TextView txtNombre; //nombre de contacto
    private TextView txtSite;//nombre del sitio
    private TextView labelFecha; //fecha dato
    private TextView labelHora; // hora dato
    private TextView labelBateria; // bateria dato
    private TextView labelSenal; // señal del dato

    private boolean mapFlag = false;
    private boolean type = false;
    private LatLng coordCamera = null;

    private LinearLayout linearEvento;
    private Spinner spTypeEvent;
    private TextInputLayout inputDescripcionEvento;
    private TextInputEditText editDescripcionEvento;
    private Button btnEnviarEvento;

    public static ArrayList<String> arrayEvento;

    public static String strImagen = "";
    private ImageButton btnAddPhoto;
    private ArrayList<WsEnviaEvento> arrayEvent;
    private ImageButton btnSeePhoto;
    private LinearLayout linearButtons;
    private Button btnReportar;
    private Button btnDescartar;
    private Marker markerGlobal;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tres, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObjects();
        initBinding(view);
        initControls();
        getEventos();
        listener();


    }

    private void initControls() {
        getContact();
        FrgMap.MyAdapter myAdapter = new FrgMap.MyAdapter(getActivity(), arrayContacto);
        spContacto.setAdapter(myAdapter);
        myAdapter = null;
        myAdapter = new FrgMap.MyAdapter(getActivity(), arrayEvento);
        spTypeEvent.setAdapter(myAdapter);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng myUbicacion = ubicacion.getLocation().getUbicacion();
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        FrgMap.Map = googleMap;
        FrgMap.Map.clear();
        FrgMap.Map.setMyLocationEnabled(true);
        FrgMap.Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myUbicacion.latitude, myUbicacion.longitude), 18));



        if ( arrayEvent.size() > 0  ){
            FrgMap.Map.clear();
            type = true;
            for (int i = 0; i < arrayEvent.size(); i++ ){
                View v = getLayoutInflater().inflate(R.layout.layout_marker, null);
                ImageView image = v.findViewById(R.id.place);
                image.setBackground(getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                LatLng point = new LatLng(Double.parseDouble(arrayEvent.get(i).getLatitud()), Double.parseDouble(arrayEvent.get(i).getLongitud()));
                MarkerOptions marker = new MarkerOptions().position(point).title(arrayEvent.get(i).getCadena()).icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v)));
                FrgMap.Map.addMarker(marker);
            }
        }


        FrgMap.Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); //esconder el bottom sheet
            }
        });
        FrgMap.Map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerGlobal = marker;
                btnAddPhoto.setVisibility(View.GONE);
                linearButtons.setVisibility(View.VISIBLE);
                btnEnviarEvento.setVisibility(View.GONE);
                for(int i = 0; i < arrayEvent.size(); i++){
                    if (arrayEvent.get(i).getCadena().equals(marker.getTitle())){
                        if( arrayEvent.get(i).getRuta().equals("") ){
                            btnSeePhoto.setVisibility(View.GONE);
                        }
                        else{
                            btnSeePhoto.setVisibility(View.VISIBLE);
                        }
                        linearDatos.setVisibility(View.GONE);
                        linearEvento.setVisibility(View.VISIBLE);
                        editDescripcionEvento.setEnabled(false);
                        editDescripcionEvento.setText(arrayEvent.get(i).getDescripcion());
                        System.out.println("MARKER:" + arrayEvent.get(i).getTipo());
                        System.out.println("DESCRIPCION:" + arrayEvent.get(i).getDescripcion());
                        for(int j = 0; j < arrayEvento.size(); j++){
                            if( arrayEvent.get(i).getTipo().equals(arrayEvento.get(j))){
                                spTypeEvent.setSelection(j);
                                spTypeEvent.setEnabled(false);
                                break;
                            }
                        }
                    }
                }
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            }
        });


        FrgMap.Map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                System.out.println( "Center======================== taget: " + FrgMap.Map.getCameraPosition().target);
                coordCamera = FrgMap.Map.getCameraPosition().target;

            }
        });

        /*
        FrgZonas.Map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                System.out.println( "Center======================== taget: " + FrgZonas.Map.getCameraPosition().target);
                coordCamera = FrgZonas.Map.getCameraPosition().target;

            }
        });
         */



    }

    private void initObjects() {
        arrayEvento = new ArrayList<>();
        arrayEvento.add("Seleccione");
        arrayEvento.add("Robo");
        arrayEvento.add("Secuestro");
        arrayEvento.add("Extorsión");
        arrayEvento.add("Acoso sexual");
        arrayEvento.add("Tiroteo");
        arrayEvento.add("Incendio");
        arrayEvento.add("Inundaciones");
        arrayEvento.add("Derrumbe");
        arrayEvento.add("Accidente Automovilístico");
        arrayEvento.add("Disturbios");


        arrayContacto = new ArrayList<>();
        preference = SharePreference.getInstance(getActivity());
        ubicacion = new LocationLibrary(getContext(), "Mapa");
    }

    private void initBinding(View view) {
        mapaG = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapaG.getExtendedMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.useViewLifecycleInFragment(true);
        mapaG = SupportMapFragment.newInstance(options);
        mapaG.setRetainInstance(true);
        btnEvento = view.findViewById(R.id.btnEvento);
        cleanMap = view.findViewById(R.id.cleanMap);
        marcarMap = view.findViewById(R.id.marcarMap);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        linearDatos = view.findViewById(R.id.linearDatos);
        spContacto = view.findViewById(R.id.spContacto);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtSite = view.findViewById(R.id.txtSite);
        labelFecha = view.findViewById(R.id.labelFecha);
        labelHora = view.findViewById(R.id.labelHora);
        labelBateria = view.findViewById(R.id.labelBateria);
        labelSenal = view.findViewById(R.id.labelSenal);
        viewVertical = view.findViewById(R.id.viewVerticalMap);
        viewHorizontal = view.findViewById(R.id.viewHorizontalMap);

        linearEvento = view.findViewById(R.id.linearEvento);
        spTypeEvent = view.findViewById(R.id.spTypeEvent);
        inputDescripcionEvento = view.findViewById(R.id.inputDescripcionEvento);
        editDescripcionEvento = view.findViewById(R.id.editDescripcionEvento);
        btnEnviarEvento = view.findViewById(R.id.btnEnviarEvento);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
        btnSeePhoto = view.findViewById(R.id.btnSeePhoto);
        linearButtons = view.findViewById(R.id.linearButtons);
        btnReportar = view.findViewById(R.id.btnReportar);
        btnDescartar = view.findViewById(R.id.btnDescartar);
    }

    public void listener(){

        spContacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(position != 0){
                    linearDatos.setVisibility(View.VISIBLE);
                    linearEvento.setVisibility(View.GONE);
                    viewVertical.setVisibility(View.GONE);
                    viewHorizontal.setVisibility(View.GONE);
                    cleanMap.setVisibility(View.GONE);
                    marcarMap.setVisibility(View.GONE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    type = false;
                    new WsSolicitaUser(getActivity(), "master").getContacto("11111",
                            txtNombre,
                            txtSite,
                            labelFecha,
                            labelHora,
                            labelBateria,
                            labelSenal,
                            sheetBehavior);
                    spTypeEvent.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (type && ( newState == BottomSheetBehavior.STATE_DRAGGING  )   ){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FrgMap.Map.clear();

                if (!type){

                    spTypeEvent.setEnabled(true);
                    spTypeEvent.setSelection(0);
                    editDescripcionEvento.setEnabled(true);
                    editDescripcionEvento.setText("");
                    linearDatos.setVisibility(View.GONE);
                    linearEvento.setVisibility(View.VISIBLE);
                    viewVertical.setVisibility(View.VISIBLE);
                    viewHorizontal.setVisibility(View.VISIBLE);
                    cleanMap.setVisibility(View.VISIBLE);
                    marcarMap.setVisibility(View.VISIBLE);
                    type = true;
                }else{
                    linearDatos.setVisibility(View.VISIBLE);
                    linearEvento.setVisibility(View.GONE);
                    viewVertical.setVisibility(View.GONE);
                    viewHorizontal.setVisibility(View.GONE);
                    cleanMap.setVisibility(View.GONE);
                    marcarMap.setVisibility(View.GONE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    type = false;
                }
            }
        });


        cleanMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FrgMap.Map.clear();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


            }
        });

        marcarMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = getActivity().getLayoutInflater().inflate(R.layout.layout_marker, null);
                ImageView image = v.findViewById(R.id.place);
                image.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_place24dp));
                FrgMap.Map.clear();
                FrgMap.Map.addMarker(new MarkerOptions().position(new LatLng(coordCamera.latitude, coordCamera.longitude)).icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v))));
                btnAddPhoto.setVisibility(View.VISIBLE);
                linearButtons.setVisibility(View.GONE);
                btnEnviarEvento.setVisibility(View.VISIBLE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mapFlag = true;


            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActCam.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_act2, R.anim.slide_out_act2);
                startActivity(intent, options.toBundle());
            }
        });

        btnEnviarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = true;

                Date date = new Date();
                DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                if ( editDescripcionEvento.getText().toString().trim().equalsIgnoreCase("")) {
                    inputDescripcionEvento.setError("Descripcion obligatorio");
                    flag = false;
                }
                if ( spTypeEvent.getSelectedItem().toString().trim().equalsIgnoreCase("Seleccione")) {
                    flag = false;
                }

                if (flag){

                    Log.d("JSON2: ",FrgMap.strImagen);

                    new WsNotification(getActivity(), "master").getNotification(preference.getStrData("id_user"),
                            spTypeEvent.getSelectedItem().toString(),
                            editDescripcionEvento.getText().toString(),
                            String.valueOf(coordCamera.latitude),
                            String.valueOf(coordCamera.longitude),
                            FrgMap.strImagen,
                            hourFormat.format(date));


                    FrgMap.strImagen = "";

                    linearDatos.setVisibility(View.VISIBLE);
                    linearEvento.setVisibility(View.GONE);
                    viewVertical.setVisibility(View.GONE);
                    viewHorizontal.setVisibility(View.GONE);
                    cleanMap.setVisibility(View.GONE);
                    marcarMap.setVisibility(View.GONE);
                    editDescripcionEvento.setText("");
                    FrgMap.Map.clear();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    type = false;
                    Utils.hideKeyboard(getActivity());
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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

        btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Envia al activity de reportar

            }
        });

        btnDescartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = null;
                Cursor c = null;
                try {
                    db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    db.delete(DataBaseDB.TB_EVENTO, DataBaseDB.CADENA + "='" + markerGlobal.getTitle() + "'", null);

                } catch (Exception ex) {
                    Log.e(TAG, "getCorreoPreregistro: " + ex.toString());
                } finally {
                    Utils.close(c);
                    Utils.close(db);
                    Utils.freeMemory();
                }

                for(int i = 0; i < arrayEvent.size(); i++){
                    if (arrayEvent.get(i).getCadena().equals(markerGlobal.getTitle())){
                        arrayEvent.remove(i);
                    }
                }
                FrgMap.Map.clear();
                if ( arrayEvent.size() > 0  ){
                    for (int i = 0; i < arrayEvent.size(); i++ ){
                        View v = getLayoutInflater().inflate(R.layout.layout_marker, null);
                        ImageView image = v.findViewById(R.id.place);
                        image.setBackground(getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                        LatLng point = new LatLng(Double.parseDouble(arrayEvent.get(i).getLatitud()), Double.parseDouble(arrayEvent.get(i).getLongitud()));
                        MarkerOptions marker = new MarkerOptions().position(point).title(arrayEvent.get(i).getCadena()).icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v)));
                        FrgMap.Map.addMarker(marker);
                    }
                }
            }
        });

        btnSeePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String endPoint="";
                for(int i = 0; i < arrayEvent.size(); i++){
                    if (arrayEvent.get(i).getCadena().equals(markerGlobal.getTitle())){
                        endPoint = arrayEvent.get(i).getRuta();
                    }
                }

                final Intent intent = new Intent(getActivity(), ActWebPhoto.class);
                intent.putExtra("endPoint", endPoint);
                final ActivityOptions options = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_act2, R.anim.slide_out_act2);


                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(intent, options.toBundle());
                    }
                }, 250);

            }
        });
    }

    public void getContact(){
        SQLiteDatabase db = null;
        Cursor c = null;
        arrayContacto.clear();
        arrayContacto.add("Seleccione");
        try {
            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + "," +
                    DataBaseDB.ID_USER  +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " WHERE " + DataBaseDB.ESTATUS +
                    " = 'Activo' " +
                    " ORDER BY " + DataBaseDB.NOMBRE, null);

            /*c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + "," +
                    DataBaseDB.ID_USER  +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " ORDER BY " + DataBaseDB.NOMBRE, null);*/

            if (c.moveToFirst()) {
                do {
                    hasMapContacto.put(c.getString(0), c.getString(1));
                    arrayContacto.add(c.getString(0));
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

    public void getEventos(){
        arrayEvent = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " +
                    " *FROM " + DataBaseDB.TB_EVENTO, null);

            if (c.moveToFirst()) {
                do {
                    arrayEvent.add(new WsEnviaEvento(
                            c.getString(0),
                            c.getString(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4),
                            c.getString(5),
                            c.getString(6),
                            "",
                            ""
                    ));

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

    public void removeEvent(String cadena){

    }


}
