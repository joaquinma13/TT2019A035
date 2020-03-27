package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.example.joaquin.tt_des_v_100.Api.Service.WsSolicitaUser;
import com.example.joaquin.tt_des_v_100.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class Fragment_tres extends Fragment  implements OnMapReadyCallback {

    private SupportMapFragment mapaG;
    public static GoogleMap Map;
    private LocationLibrary ubicacion;
    private SharePreference preference;


    public static Spinner spContacto;
    public static ArrayList<String> arrayContacto;
    private Map<String, String> hasMapContacto = new HashMap<>();

    //BottomSheet
    public static BottomSheetBehavior sheetBehavior;
    public static RelativeLayout bottom_sheet;
    private TextView txtNombre; //nombre de contacto
    private TextView txtSite;//nombre del sitio
    private TextView labelFecha; //fecha dato
    private TextView labelHora; // hora dato
    private TextView labelBateria; // bateria dato
    private TextView labelSenal; // señal del dato


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
        listener();
    }

    private void initControls() {
        getContact();
        Fragment_tres.MyAdapter myAdapter = new Fragment_tres.MyAdapter(getActivity(), arrayContacto);
        spContacto.setAdapter(myAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        LatLng myUbicacion = ubicacion.getLocation().getUbicacion();
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        Fragment_tres.Map = googleMap;
        Fragment_tres.Map.clear();
        Fragment_tres.Map.setMyLocationEnabled(true);
        Fragment_tres.Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myUbicacion.latitude, myUbicacion.longitude), 18));
        Fragment_tres.Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {
                //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); esconder el bottom sheet
            }
        });
        Map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMarkerClick(Marker marker) {
                //System.out.println("que ongo!!!");

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            }
        });

    }

    private void initObjects() {
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

        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        spContacto = view.findViewById(R.id.spContacto);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtSite = view.findViewById(R.id.txtSite);
        labelFecha = view.findViewById(R.id.labelFecha);
        labelHora = view.findViewById(R.id.labelHora);
        labelBateria = view.findViewById(R.id.labelBateria);
        labelSenal = view.findViewById(R.id.labelSenal);
    }

    public void listener(){

        spContacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(position != 0){
                    new WsSolicitaUser(getActivity(), "master").getContacto("11111",
                            txtNombre,
                            txtSite,
                            labelFecha,
                            labelHora,
                            labelBateria,
                            labelSenal,
                            sheetBehavior);
                }



                //tipoJuicio = hmT.get(arrayT.get(position));

                //System.out.println("holaaaaaaa");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
            /*c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + "," +
                    DataBaseDB.ID_USER  +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " WHERE " + DataBaseDB.ESTATUS +
                    " = 'Actvo' " +
                    " ORDER BY " + DataBaseDB.NOMBRE, null);*/

            c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + "," +
                    DataBaseDB.ID_USER  +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " ORDER BY " + DataBaseDB.NOMBRE, null);

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
}
