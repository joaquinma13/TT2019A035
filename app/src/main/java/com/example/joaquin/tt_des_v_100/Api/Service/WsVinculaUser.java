package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeUsuario;
import com.example.joaquin.tt_des_v_100.Ui.Adapter.AdpCuentas;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.Fragment_uno;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.joaquin.tt_des_v_100.Ui.Fragment.Fragment_uno.recyclerAdapter;
import static com.example.joaquin.tt_des_v_100.Ui.Fragment.Fragment_uno.recyclerContact;

public class WsVinculaUser {

    private static  final String TAG = WsVinculaUser.class.getSimpleName();

    private Activity act;
    private APIInterface apiInterface;
    private Permission permission;
    private LocationLibrary ubicacion;
    private SharePreference preference;
    private ArrayList<Item> contacto = new ArrayList<>();

    public WsVinculaUser(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
        permission = new Permission(act);
        ubicacion = new LocationLibrary(act, "GetWebLogin");
        preference = SharePreference.getInstance(act);
    }


    public void setVinculo(final String id_user, final String telefono, final String nombre, final SQLiteDatabase db, final CustomAlert alert) {
        Fragment_uno.count += 1;
        final Call<WsRecibeUsuario> call = apiInterface.postVinculaUsuario(new WsRecibeUsuario(id_user, telefono));
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

                            Fragment_uno.error += 1;

                            if ( Fragment_uno.count == Fragment_uno.selectedContact.size() ){

                                alert.close();
                                db.close();

                            }

                        } else {
                            if ( saveUser(usuario.id_user, nombre, usuario.telefono,db) == 0  )
                                Fragment_uno.error += 1;

                            if ( Fragment_uno.count == Fragment_uno.selectedContact.size() ){

                                Cursor c = null;

                                contacto.clear();
                                System.out.println("Tam: " + contacto.size());
                                c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + ", " +
                                        DataBaseDB.TELEFONO + " FROM " + DataBaseDB.TB_CONTACTO, null);

                                if (c.moveToFirst()) {
                                    do {
                                        System.out.println(c.getString(0));
                                        contacto.add(new Item(
                                                        c.getString(0),
                                                        c.getString(1),
                                                        true
                                                )
                                        );
                                    } while (c.moveToNext());

                                } else {
                                    System.out.println("No existen registros!!!");
                                }
                                recyclerAdapter = new AdpCuentas(act, contacto);
                                recyclerContact.setAdapter(recyclerAdapter);

                                alert.close();
                                c.close();
                                db.close();

                            }

                        }

                    }
                }



            }

            @Override
            public void onFailure(Call<WsRecibeUsuario> call, Throwable t) {
                call.cancel();

                alert.close();

                /*alert.setTypeError("ON FAILURE", t.toString(), "Cancelar", "Volver a intentar");
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
                        setVinculo(id_user, telefono,nombre,db,alert,act);
                    }
                });*/
            }
        });
    }

    private int saveUser(String id_user, String nombre, String telefono,SQLiteDatabase db) {
        int idSave = 0;
        if (db != null) {
            ContentValues values = new ContentValues();

            values.put(DataBaseDB.ID_USER, id_user);
            values.put(DataBaseDB.NOMBRE, nombre);
            values.put(DataBaseDB.TELEFONO, telefono);

            idSave = (int) db.insert(DataBaseDB.TB_CONTACTO, null, values);

        }
        return idSave;
    }

}
