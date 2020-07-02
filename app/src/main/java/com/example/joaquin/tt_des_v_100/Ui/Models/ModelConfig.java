package com.example.joaquin.tt_des_v_100.Ui.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsConfiguracion;
import com.example.joaquin.tt_des_v_100.Api.Service.APIInterface;
import com.example.joaquin.tt_des_v_100.Api.Service.APIUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ModelConfig {

    private Context context;
    private APIInterface apiInterface;
    private boolean flag = false;

    public ModelConfig(Context context) {
        this.context = context;
        apiInterface = APIUtils.getUtils(context, "master").create(APIInterface.class);
    }

    public WsConfiguracion getStartedConfig (){

        WsConfiguracion wsConfiguracion = new WsConfiguracion();
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = context.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " +
                    " *FROM " + DataBaseDB.TB_CONFIGURACION, null);

            if (c.moveToFirst()) {
                do {
                    wsConfiguracion = new WsConfiguracion(c.getString(0),
                            c.getString(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4));
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
        return wsConfiguracion;
    }


    public boolean changeDistancia(final String id_user, final String distancia, final String guardian,
                                   final String tiempo, final String sesion){
        flag = false;
        final Call<WsConfiguracion> call = apiInterface.postChangeConfig(new WsConfiguracion(id_user,distancia,guardian,tiempo,sesion));

        call.enqueue(new Callback<WsConfiguracion>() {
            @Override
            public void onResponse(final Call<WsConfiguracion> call, Response<WsConfiguracion> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    Log.d("JSON: ", new Gson().toJson(response.body()));
                    final WsConfiguracion getConfig = response.body();
                    if(!getConfig.Estatus.equals("OK")){
                        for (WsConfiguracion.Configuracion config : getConfig.Configuracion) {
                            updateConfig(config.id_user, config.distancia, config.guardian, config.tiempo, config.sesion);
                        }
                        flag = true;
                    }else{
                        flag = false;
                    }
                }else{
                    flag = false;
                }
            }
            @Override
            public void onFailure(Call<WsConfiguracion> call, Throwable t) {
                call.cancel();
                flag = false;
            }
        });
        return flag;
    }


    private void updateConfig(String id_user, String distancia, String guardian, String tiempo, String sesion) {

        SQLiteDatabase db = null;
        if (db != null) {
            ContentValues values = new ContentValues();

            values.put(DataBaseDB.ID_USER, id_user);
            values.put(DataBaseDB.DISTANCIA, distancia);
            values.put(DataBaseDB.GUARDIAN, guardian);
            values.put(DataBaseDB.TIEMPO, tiempo);
            values.put(DataBaseDB.SESION, sesion);
            db.update(DataBaseDB.TB_CONFIGURACION, values,
                    DataBaseDB.ID_USER + "='" + id_user  + "'", null);
        }
        db.close();
    }
}
