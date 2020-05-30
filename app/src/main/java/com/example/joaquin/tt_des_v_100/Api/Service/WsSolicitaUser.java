package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidmapsextensions.MarkerOptions;
import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeBitacora;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.FrgMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WsSolicitaUser {

    private static  final String TAG = WsVinculaUser.class.getSimpleName();

    private Activity act;
    private APIInterface apiInterface;

    public WsSolicitaUser(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
    }


    public void getContacto(final String id_user,
                            final TextView txtNombre,
                            final TextView txtSite,
                            final TextView labelFecha,
                            final TextView labelHora,
                            final TextView labelBateria,
                            final TextView labelSenal,
                            final BottomSheetBehavior sheetBehavior) {

        final Call<WsRecibeBitacora> call = apiInterface.postSolicitarUsuario(new WsRecibeBitacora(id_user));
        final CustomAlert alert = new CustomAlert(act);
        alert.setTypeProgress("Solicitando...", "", "Cancelar");
        alert.setCancelable(false);
        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.cancel();
                alert.close();
            }
        });
        alert.show();
        call.enqueue(new Callback<WsRecibeBitacora>() {
            @Override
            public void onResponse(final Call<WsRecibeBitacora> call, Response<WsRecibeBitacora> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {

                    Log.d("JSON: ", new Gson().toJson(response.body()));
                    WsRecibeBitacora getUser = response.body();

                    for (WsRecibeBitacora.Bitacora usuario : getUser.Bitacora) {
                        System.out.println("Server Response: " + getUser.Estatus);
                        if (!getUser.Estatus.toUpperCase(Locale.ENGLISH).equals("OK")) {

                            alert.setTypeError("Error", getUser.Estatus, "Cancelar", "Volver a intentar");
                            alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    call.cancel();
                                    alert.close();
                                }
                            });
                            alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.close();
                                    getContacto(id_user,
                                            txtNombre,
                                            txtSite,
                                            labelFecha,
                                            labelHora,
                                            labelBateria,
                                            labelSenal,
                                            sheetBehavior);
                                }
                            });
                        } else {
                            System.out.println("esto 1");
                            if ( updateUser(usuario.id_user, usuario.senal, usuario.bateria, usuario.imei, usuario.modelo,
                                    usuario.latitud, usuario.longitud, usuario.fecha, usuario.nombre, usuario.telefono) > 0){
                                System.out.println("esto 3");
                                alert.close();

                                View v = act.getLayoutInflater().inflate(R.layout.layout_marker, null);
                                ImageView image = v.findViewById(R.id.place);
                                image.setBackground(act.getResources().getDrawable(R.drawable.ic_person_pin_black_24dp));
                                FrgMap.Map.clear();
                                LatLng point = new LatLng(Double.parseDouble(usuario.latitud), Double.parseDouble(usuario.longitud));
                                FrgMap.Map.addMarker(new MarkerOptions().position(point).title(usuario.nombre).icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(v))));
                                FrgMap.Map.moveCamera(CameraUpdateFactory.newLatLng(point));
                                txtNombre.setText(usuario.nombre);
                                txtSite.setText("Latitud: " + usuario.latitud + " Longitud: " + usuario.longitud);
                                labelFecha.setText(usuario.fecha.split(" ")[0]);
                                labelHora.setText(usuario.fecha.split(" ")[1]);
                                labelBateria.setText(usuario.bateria);
                                labelSenal.setText(usuario.senal);


                            }

                        }

                    }
                }
            }
            @Override
            public void onFailure(Call<WsRecibeBitacora> call, Throwable t) {
                call.cancel();
                alert.setTypeError("ON FAILURE", t.toString(), "Cancelar", "Volver a intentar");
                alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.close();
                    }
                });
                alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.close();
                        getContacto(id_user,
                                txtNombre,
                                txtSite,
                                labelFecha,
                                labelHora,
                                labelBateria,
                                labelSenal,
                                sheetBehavior);
                    }
                });
            }
        });
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

    private int updateUser(String id_user, String senal, String bateria, String imei, String modelo,
                           String latitud, String longitud, String fecha, String nombre, String telefono) {
        System.out.println("esto 2");


        int idSave = 0;
        SQLiteDatabase db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
        if (db != null) {
            ContentValues values = new ContentValues();

            values.put(DataBaseDB.SENAL, senal);
            values.put(DataBaseDB.BATERIA, bateria);
            values.put(DataBaseDB.IMEI, imei);
            values.put(DataBaseDB.MODELO, modelo);
            values.put(DataBaseDB.LATITUD, latitud);
            values.put(DataBaseDB.LONGITUD, longitud);
            values.put(DataBaseDB.FECHA, fecha);
            //values.put(DataBaseDB.NOMBRE, nombre);
            values.put(DataBaseDB.TELEFONO, telefono);

            idSave = (int) db.update(DataBaseDB.TB_CONTACTO, values,
                    DataBaseDB.ID_USER + "='" + id_user  + "'", null);

            db.close();
        }
        return idSave;
    }



}
