package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ImageView;

import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsConfiguracion;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeBitacora;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeUsuario;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActLogin;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Splash;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class WsRecSplash {

    private static  final String TAG = WsRecSplash.class.getSimpleName();

    private Activity act;
    private APIInterface apiInterface;
    private SQLiteDatabase db;
    private ImageView imageView;
    private SharePreference preference;
    private boolean contact = false;
    private boolean zones = false;
    private boolean config = false;
    private boolean errorConnexion = false;
    int errores = 0;

    public WsRecSplash(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
        preference = SharePreference.getInstance(act);
        db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
    }

    public void startThreads(String id_user, ImageView imageView){
        getContactos(id_user);
        getZones(id_user);
        getConfig(id_user);
        this.imageView = imageView;
    }


    public void getContactos(final String id_user) {

        final Call<WsRecibeUsuario> call = apiInterface.postContacts(new WsRecibeUsuario(id_user));

        call.enqueue(new Callback<WsRecibeUsuario>() {
            @Override
            public void onResponse(final Call<WsRecibeUsuario> call, Response<WsRecibeUsuario> response) {

                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    Log.d("JSON: ", new Gson().toJson(response.body()));
                    final WsRecibeUsuario getUsers = response.body();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(!getUsers.Resultados.equals("0")){
                                    for (WsRecibeUsuario.Usuario usuario : getUsers.Usuario) {
                                        System.out.println("Contacto: " + usuario.nombre);
                                        updateUser(usuario.id_user, usuario.nombre, usuario.telefono, usuario.estado);
                                    }
                                    contact = true;
                                    checkSplash();
                                }else{
                                    contact = true;
                                    checkSplash();
                                }
                            } catch (Exception e) {
                                errorConnexion = true;
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    errorConnexion = true;
                    contact = true;
                    checkSplash();
                }
            }
            @Override
            public void onFailure(Call<WsRecibeUsuario> call, Throwable t) {
                call.cancel();
                errorConnexion = true;
                contact = true;
                checkSplash();
            }
        });
    }

    public void getZones(final String id_user) {

        final Call<WsRecibeBitacora> call = apiInterface.postZonas(new WsRecibeBitacora(id_user));

        call.enqueue(new Callback<WsRecibeBitacora>() {
            @Override
            public void onResponse(final Call<WsRecibeBitacora> call, Response<WsRecibeBitacora> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    Log.d("JSON: ", new Gson().toJson(response.body()));
                    final WsRecibeBitacora getZones = response.body();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(!getZones.Resultados.equals("0")){
                                    for (WsRecibeBitacora.Zonas zonas : getZones.Zonas) {
                                        System.out.println("Zona: " + zonas.nombre);
                                        updateZone(zonas.id_zona, zonas.nombre, zonas.latitud, zonas.longitud, zonas.radio);
                                    }
                                    zones = true;
                                    checkSplash();
                                }else{
                                    zones = true;
                                    checkSplash();
                                }
                            } catch (Exception e) {
                                errorConnexion = true;
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    errorConnexion = true;
                    zones = true;
                    checkSplash();
                }
            }
            @Override
            public void onFailure(Call<WsRecibeBitacora> call, Throwable t) {
                call.cancel();
                errorConnexion = true;
                zones = true;
                checkSplash();
            }
        });
    }

    public void getConfig(final String id_user) {

        final Call<WsConfiguracion> call = apiInterface.postConfig(new WsConfiguracion(id_user,"","","",""));

        call.enqueue(new Callback<WsConfiguracion>() {
            @Override
            public void onResponse(final Call<WsConfiguracion> call, Response<WsConfiguracion> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    Log.d("JSON: ", new Gson().toJson(response.body()));
                    final WsConfiguracion getConfig = response.body();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(!getConfig.Resultados.equals("0")){
                                    for (WsConfiguracion.Configuracion config : getConfig.Configuracion) {
                                        System.out.println("Config: " + config.id_user);
                                        updateConfig(config.id_user, config.distancia, config.guardian, config.tiempo, config.sesion);
                                    }
                                    config = true;
                                    checkSplash();
                                }else{
                                    config = true;
                                    checkSplash();
                                }
                            } catch (Exception e) {
                                errorConnexion = true;
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    errorConnexion = true;
                    config = true;
                    checkSplash();
                }
            }
            @Override
            public void onFailure(Call<WsConfiguracion> call, Throwable t) {
                call.cancel();
                errorConnexion = true;
                config = true;
                checkSplash();
            }
        });
    }

    private void updateUser(String id_user, String nombre, String telefono, String estado) {

        int idSave = 0;
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(DataBaseDB.ID_USER, id_user);
            values.put(DataBaseDB.ESTATUS, estado);
            idSave = db.update(DataBaseDB.TB_CONTACTO, values,
                    DataBaseDB.TELEFONO + "='" + telefono  + "'", null);

            if (idSave == 0){
                values.clear();
                values.put(DataBaseDB.ID_USER, id_user);
                values.put(DataBaseDB.TELEFONO, telefono);
                values.put(DataBaseDB.ESTATUS, estado);
                values.put(DataBaseDB.NOMBRE, nombre);
                idSave = (int) db.insert(DataBaseDB.TB_CONTACTO, null, values);
                if ( idSave == 0 )
                    errores++;
            }

        }else{
            errores++;
        }
    }

    private void updateZone(String id_zona, String nombre, String latitud, String longitud, String radio) {


        int idSave = 0;
        if (db != null) {
            ContentValues values = new ContentValues();

            values.put(DataBaseDB.RADIO, radio);
            values.put(DataBaseDB.NOMBRE, nombre);
            values.put(DataBaseDB.LATITUD, latitud);
            values.put(DataBaseDB.LONGITUD, longitud);
            idSave = db.update(DataBaseDB.TB_ZONAS, values,
                    DataBaseDB.ID_ZONA + "='" + id_zona  + "'", null);

            if (idSave == 0){

                values.clear();
                values.put(DataBaseDB.ID_ZONA, id_zona);
                values.put(DataBaseDB.RADIO, radio);
                values.put(DataBaseDB.NOMBRE, nombre);
                values.put(DataBaseDB.LATITUD, latitud);
                values.put(DataBaseDB.LONGITUD, longitud);
                idSave = (int) db.insert(DataBaseDB.TB_ZONAS, null, values);
                if ( idSave == 0 )
                    errores++;
            }

        }else{
            errores++;
        }
    }


    private void updateConfig(String id_user, String distancia, String guardian, String tiempo, String sesion) {

        int idSave = 0;
        if (db != null) {
            ContentValues values = new ContentValues();

            values.put(DataBaseDB.ID_USER, id_user);
            values.put(DataBaseDB.DISTANCIA, distancia);
            values.put(DataBaseDB.GUARDIAN, guardian);
            values.put(DataBaseDB.TIEMPO, tiempo);
            values.put(DataBaseDB.SESION, sesion);
            idSave = db.update(DataBaseDB.TB_CONFIGURACION, values,
                    DataBaseDB.ID_USER + "='" + id_user  + "'", null);

            if (idSave == 0){

                values.clear();
                values.put(DataBaseDB.ID_USER, id_user);
                values.put(DataBaseDB.DISTANCIA, distancia);
                values.put(DataBaseDB.GUARDIAN, guardian);
                values.put(DataBaseDB.TIEMPO, tiempo);
                values.put(DataBaseDB.SESION, sesion);
                idSave = (int) db.insert(DataBaseDB.TB_CONFIGURACION, null, values);
                if ( idSave == 0 )
                    errores++;
            }

        }else{
            errores++;
        }
    }

    public void checkSplash(){
        if(contact & zones & config){
            db.close();
            imageView.clearAnimation();
            final Intent intent = new Intent(act, Home_TT.class);
            final ActivityOptions options = ActivityOptions.makeCustomAnimation(act, R.anim.slide_in_act2, R.anim.slide_out_act2);
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    act.startActivity(intent, options.toBundle());
                    act.finish();
                }
            });
        }
    }

}
