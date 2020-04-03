package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeUsuario;
import com.example.joaquin.tt_des_v_100.R;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class WsVinculaUser {

    private static  final String TAG = WsVinculaUser.class.getSimpleName();

    private Activity act;
    private APIInterface apiInterface;
    private Permission permission;
    private LocationLibrary ubicacion;
    private SharePreference preference;

    public WsVinculaUser(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
        permission = new Permission(act);
        ubicacion = new LocationLibrary(act, "GetWebLogin");
        preference = SharePreference.getInstance(act);
    }


    public void setVinculo(final String id_user, final String telefono, final String bandera) {
        final Call<WsRecibeUsuario> call = apiInterface.postVinculaUsuario(new WsRecibeUsuario(id_user, telefono, bandera));
        final CustomAlert alert = new CustomAlert(act);
        alert.setTypeProgress("Vinculando...", "", "Cancelar");
        alert.setCancelable(false);
        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.cancel();
                alert.close();
            }
        });
        alert.show();
        call.enqueue(new Callback<WsRecibeUsuario>() {
            @Override
            public void onResponse(final Call<WsRecibeUsuario> call, Response<WsRecibeUsuario> response) {

                Log.d(TAG, String.valueOf(response.code()));

                if (response.isSuccessful()) {
                    Log.d("JSON: ", new Gson().toJson(response.body()));
                    WsRecibeUsuario vinUser = response.body();
                    for (WsRecibeUsuario.Usuario usuario : vinUser.Usuario) {
                        System.out.println("Server Response: " + vinUser.Estatus);


                        if (!vinUser.Estatus.toUpperCase(Locale.ENGLISH).equals("OK")) {
                            alert.setTypeError("Error", vinUser.Estatus, "Cancelar", "Volver a intentar");
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
                                    setVinculo(id_user, telefono, bandera);
                                }
                            });
                        } else {
                            if(saveUser(usuario.id_user, usuario.telefono, usuario.status) > 0){
                                getContact();
                                alert.close();
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<WsRecibeUsuario> call, Throwable t) {
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
                        setVinculo(id_user, telefono, bandera);
                    }
                });
            }
        });
    }

    private int saveUser(String id_user, String telefono, String status) {
        System.out.println("Estado: " + status);
        int idSave = 0;
        SQLiteDatabase db = null;
        db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put(DataBaseDB.ID_USER, id_user);
        values.put(DataBaseDB.ESTATUS, status);
        idSave = (int) db.update(DataBaseDB.TB_CONTACTO, values,
                DataBaseDB.TELEFONO + "='" + telefono  + "'", null);
        return idSave;
    }

    public void getContact(){
        Utils.itemsContact.clear();
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + ", " +
                    DataBaseDB.TELEFONO + ", " +
                    DataBaseDB.ESTATUS +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " ORDER BY " + DataBaseDB.NOMBRE, null);

            if (c.moveToFirst()) {
                do {
                    Utils.itemsContact.add(new Item(c.getString(0),c.getString(1),c.getString(2)));
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
