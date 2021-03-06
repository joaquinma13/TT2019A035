package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Model.Autenticar;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Splash;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WsAut {

    private static final String TAG = WsAut.class.getSimpleName();

    private Activity act;
    private APIInterface apiInterface;

    private Permission permission;
    private LocationLibrary ubicacion;

    private SharePreference preference;


    public WsAut(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);

        permission = new Permission(act);
        ubicacion = new LocationLibrary(act, "GetWebLogin");
        preference = SharePreference.getInstance(act);
    }

    public void getWebLogin(final String user, final String pass) {

        Utils.cancel = false;

        final Call<Autenticar> call = apiInterface.postAutenticar(new Autenticar(user, pass, preference.getStrData("token")));

        final CustomAlert alert = new CustomAlert(act);
        alert.setTypeProgress("Conectando...", "", "Cancelar");
        alert.setCancelable(false);
        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.cancel();
                alert.close();
                Utils.cancel = true;
                SQLiteDatabase db = null;
                try {
                    db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
                    db.delete(DataBaseDB.TB_NAME_USUARIO, null, null);
                    //TODO db.delete(DB.TB_INFO_GRAL, null, null);
                    //TODO db.delete(DB.TB_ACUERDOS, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        });
        alert.show();

        call.enqueue(new Callback<Autenticar>() {
            @Override
            public void onResponse(final Call<Autenticar> call, Response<Autenticar> response) {

                Log.d(TAG, String.valueOf(response.code()));

                if (response.isSuccessful()) {

                    Log.d("JSON: ", new Gson().toJson(response.body()));

                    Autenticar aut = response.body();

                    for (Autenticar.Usuario usuario : aut.Usuario) {
                        System.out.println("Server Response: " + aut.Estatus);

                        if (!aut.Estatus.toUpperCase(Locale.ENGLISH).equals("OK")) {
                            alert.setTypeError("Error", aut.Estatus, "Cancelar", "Volver a intentar");
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
                                    getWebLogin(user, pass);
                                }
                            });

                        } else {



                            preference.saveData("id_user", usuario.id_user);
                            preference.saveData("nombre", usuario.nombre);

                            if( saveUser(usuario.id_user, usuario.nombre, usuario.correo, usuario.telefono, usuario.contrasena) > 0 ){

                                if ( getContactList() ){
                                    alert.close();
                                    Utils.hideKeyboard(act);
                                    final Intent intent = new Intent(act, Splash.class);
                                    final ActivityOptions options = ActivityOptions.makeCustomAnimation(act, R.anim.slide_in_act2, R.anim.slide_out_act2);

                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            act.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    act.startActivity(intent, options.toBundle());
                                                    act.finish();
                                                }
                                            });
                                        }
                                    }, 250);
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Autenticar> call, Throwable t) {
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
                        getWebLogin(user, pass);
                    }
                });
            }
        });
    }

    /* ---------------------------------- Guardar Nombre y Password ------------------------------*/
    private int saveUser(String id_user, String nombre, String correo, String telefono, String contrasena) {
        int idSave = 0;
        SQLiteDatabase db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
        if (db != null) {
            ContentValues values = new ContentValues();

            values.put(DataBaseDB.ID_USER, id_user);
            values.put(DataBaseDB.NOMBRE, nombre);
            values.put(DataBaseDB.CORREO_ELECTRONICO, correo);
            values.put(DataBaseDB.TELEFONO, telefono);
            values.put(DataBaseDB.CONTRASENA, contrasena);

            idSave = (int) db.insert(DataBaseDB.TB_NAME_USUARIO, null, values);

            db.close();
        }
        return idSave;
    }


    /*public void setPassword(final CustomAlert alert, final String user, final String pass) {

        Utils.cancel = false;

        final Call<Autenticar> call = apiInterface.setPassword(new Autenticar(encrypt(user), encrypt(pass)));

        final CustomAlert alert2 = new CustomAlert(act);
        alert2.setTypeProgress(
                "Cambiando contraseña...",
                "",
                "Cancelar");
        alert2.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.cancel();
                alert.close();
            }
        });
        alert2.show();

        call.enqueue(new Callback<Autenticar>() {
            @Override
            public void onResponse(final Call<Autenticar> call, Response<Autenticar> response) {

                Log.d(TAG, String.valueOf(response.code()));

                if (response.isSuccessful()) {

                    Log.d("JSON: ", new Gson().toJson(response.body()));

                    Autenticar password = response.body();

                    for (Autenticar.Informacion p : password.Informacion) {

                        if (password.Estatus.toUpperCase(Locale.ENGLISH).equals("OK")) {

                            if (p.Mensaje.toUpperCase().contains("ERROR")) {
                                alert2.setTypeError("Error:", p.Mensaje, "Aceptar");
                                alert2.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alert2.close();
                                    }
                                });
                            } else {
                                SQLiteDatabase db = act.openOrCreateDatabase(DB.DB_NAME, Context.MODE_PRIVATE, null);
                                try {
                                    ContentValues update = new ContentValues();
                                    update.put(DB.CONTRASENA, pass);
                                    db.update(DB.TB_SESSION, update, null, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    db.close();
                                }
                                alert2.close();
                                if (alert != null)
                                    alert.close();

                                Alerter.create(act)
                                        .setTitle(p.Mensaje)
                                        .setIcon(ContextCompat.getDrawable(act, R.drawable.ic_lock))
                                        .setBackgroundColorRes(R.color.colorAccent)
                                        .setDuration(2000)
                                        .show();
                            }

                        } else {
                            alert2.setTypeError("Error:", p.Mensaje, "Aceptar");
                            alert2.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert2.close();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Autenticar> call, Throwable t) {
                call.cancel();

                alert2.setTypeError("ON FAILURE", t.toString(), "Aceptar");
                alert2.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert2.close();
                    }
                });
            }
        });
    }*/

    public boolean getContactList() {
        HashMap<String, String> contactos = new HashMap<String, String>();
        SQLiteDatabase db;
        db = act.openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
        Utils.itemsContact.clear();
        db.delete(DataBaseDB.TB_CONTACTO, null, null);
        String[] projeccion = new String[] { ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE };
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        Cursor c = act.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                null);
        while(c.moveToNext()){
            String aux = c.getString(1).replace(" ", "");
            if (aux.length() > 9){
                contactos.put(aux.substring( aux.length() - 10 ), c.getString(0));
            }

        }
        c.close();
        if (db != null) {
            ContentValues values = new ContentValues();
            for (Map.Entry<String, String> entry : contactos.entrySet()) {

                values.put(DataBaseDB.NOMBRE, entry.getValue());
                values.put(DataBaseDB.TELEFONO, entry.getKey());
                values.put(DataBaseDB.ESTATUS, "nulo");
                db.insert(DataBaseDB.TB_CONTACTO, null, values);
            }
            db.close();
        }else{
            System.out.println("puntero cerrado");
        }
        return true;
    }

    private String encrypt(String pass) {
        int idx;
        String result = "";
        for (idx = 0; idx < pass.length(); idx++) {
            result += encryptChar(pass.substring(idx, idx + 1), pass.length(), idx);
        }
        return result;
    }

    private String encryptChar(String caracter, int variable, int a_indice) {
        String ptr_busqueda = "qpwoeirutyQPWOEIRUTYañsld1234567890kfjghAÑSLDKFJGHzmxncbvZMXNCBV.";
        String ptr_encripta = "zmxncbvZMXNCBVañsldkfjghAÑ.SLDKFJGHqpwoeirutyQPWOEIRUTY0987654321.";
        int indice;

        if (ptr_busqueda.indexOf(caracter) != -1) {
            indice = (ptr_busqueda.indexOf(caracter) + variable + a_indice) % ptr_busqueda.length();
            return ptr_encripta.substring(indice, indice + 1);
        }
        return caracter;
    }

}
