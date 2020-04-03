package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeBitacora;
import com.example.joaquin.tt_des_v_100.Ui.Adapter.AdpZonas;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.FrgZonas;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WsRecZona {

    private static  final String TAG = WsVinculaUser.class.getSimpleName();

    private Activity act;
    private APIInterface apiInterface;
    private Permission permission;
    private LocationLibrary ubicacion;
    private SharePreference preference;

    public WsRecZona(Activity act, String endpoint) {

        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
        permission = new Permission(act);
        ubicacion = new LocationLibrary(act, "GetWebLogin");
        preference = SharePreference.getInstance(act);
    }


    public void setZona(final String id_user,
                        final String id_zona,
                        final String nombre,
                        final String latitud,
                        final String longitud,
                        final String radio) {

        final Call<WsRecibeBitacora> call = apiInterface.postRegistrarZona(new WsRecibeBitacora(id_user,id_zona,nombre,latitud,longitud,radio));
        final CustomAlert alert = new CustomAlert(act);
        alert.setTypeProgress("Registrando", "", "Cancelar");
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
                    WsRecibeBitacora getZona = response.body();
                    if ( !getZona.Estatus.toUpperCase(Locale.ENGLISH).equals("OK")){
                        alert.setTypeError("Error", getZona.Estatus, "Cancelar", "Volver a intentar");
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
                                setZona(id_user,
                                        id_zona,
                                        nombre,
                                        latitud,
                                        longitud,
                                        radio);
                            }
                        });

                    }else{

                        if(saveZona(id_zona,
                                nombre,
                                latitud,
                                longitud,
                                radio) > 0 ){

                            getZone();
                            FrgZonas.recyclerAdapter = new AdpZonas(act, FrgZonas.itemsZonas);
                            FrgZonas.recyclerZona.setAdapter(FrgZonas.recyclerAdapter);
                            alert.close();
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
                        setZona(id_user,
                                id_zona,
                                nombre,
                                latitud,
                                longitud,
                                radio);
                    }
                });
            }
        });
    }


    private int saveZona(String id_zona,
                         String nombre,
                         String latitud,
                         String longitud,
                         String radio) {
        int idSave = 0;
        SQLiteDatabase db = null;
        db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put(DataBaseDB.ID_ZONA, id_zona);
        values.put(DataBaseDB.RADIO, radio);
        values.put(DataBaseDB.NOMBRE, nombre);
        values.put(DataBaseDB.LATITUD, latitud);
        values.put(DataBaseDB.LONGITUD, longitud);
        idSave = (int) db.insert(DataBaseDB.TB_ZONAS, null, values);
        return idSave;
    }

    public void getZone(){
        FrgZonas.itemsZonas.clear();
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " +
                    " *FROM " + DataBaseDB.TB_ZONAS, null);

            if (c.moveToFirst()) {
                do {
                    FrgZonas.itemsZonas.add(new Item(c.getString(0),
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

}
