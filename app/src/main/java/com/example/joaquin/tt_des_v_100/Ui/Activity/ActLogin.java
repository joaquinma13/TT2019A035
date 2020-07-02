package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import com.mc.oops.BuildConfig;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Model.Autenticar;
import com.example.joaquin.tt_des_v_100.Api.Service.APIInterface;
import com.example.joaquin.tt_des_v_100.Api.Service.APIUtils;
import com.example.joaquin.tt_des_v_100.Api.Service.WsAut;
import com.example.joaquin.tt_des_v_100.BuildConfig;
import com.example.joaquin.tt_des_v_100.Api.Class.Connection;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseHelper;
import com.example.joaquin.tt_des_v_100.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActLogin extends AppCompatActivity {

    private static final String TAG = ActLogin.class.getSimpleName();

    // OBJECTS
    private Permission permission;
    private Connection connection;
    public static LocationLibrary ubicacion;
    private SharePreference preference;

    /* Objetos de Base de datos*/
    private SQLiteDatabase db = null;
    public static DataBaseHelper sqliteHelper;
    private Cursor c = null;

    // UI
    private TextInputLayout inputUser;
    private TextInputLayout inputPass;
    public static EditText etUser, etPass;
    public static Button btnLogin;
    public TextView btnPreregistro;
    private ImageView txtCerrar;



    //WebService
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);



        apiInterface = APIUtils.getUtils(getApplicationContext(), "master").create(APIInterface.class);


        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {

                            findViewById(R.id.txtVersion).setVisibility(View.GONE);
                            findViewById(R.id.textViewVersion).setVisibility(View.GONE);
                            findViewById(R.id.imageViewLogoMaster).setVisibility(View.GONE);
                            findViewById(R.id.icon_app).setVisibility(View.INVISIBLE);
                            findViewById(R.id.textViewTest).setVisibility(View.GONE);
                            findViewById(R.id.textViewCerrar).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.txtVersion).setVisibility(View.VISIBLE);
                            findViewById(R.id.textViewVersion).setVisibility(View.VISIBLE);
                        }
                    }
                });

        /* CREACION DE BASE DE DATOS */
        sqliteHelper = new DataBaseHelper(this, DataBaseDB.DB_NAME, null, DataBaseDB.VERSION);
        db = sqliteHelper.getWritableDatabase();
        db.close();


        permission = new Permission(this);
        connection = new Connection(this);
        ubicacion = new LocationLibrary(this, "Login");
        preference = SharePreference.getInstance(this);

        inputUser = findViewById(R.id.etUsuario);
        inputPass = findViewById(R.id.etPassword);

        etUser = findViewById(R.id.editTextUsuario);
        etPass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonEntrar);
        btnPreregistro = findViewById(R.id.btnPreregistro);
        txtCerrar = findViewById(R.id.textViewCerrar);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !permission.checkPermissions())
                    Permissions();
                else {
                    inputUser.setError(null);
                    inputUser.setErrorEnabled(false);
                    inputPass.setError(null);
                    inputPass.setErrorEnabled(false);

                    String user = etUser.getText().toString();
                    String pass = etPass.getText().toString();

                    if (user.trim().equalsIgnoreCase("")) {
                        inputUser.setError("Usuario obligatorio");
                        etUser.requestFocus();
                    } else if (pass.trim().equalsIgnoreCase("")) {
                        inputPass.setError("Contraseña obligatoria");
                        etPass.requestFocus();
                    }else {
                        new WsAut(ActLogin.this, "master").getWebLogin(user, pass);
                    }
                }
            }
        });

        btnPreregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !permission.checkPermissions()  )
                    Permissions();
                else {
                    final Intent intent = new Intent(ActLogin.this, ActRegistro.class);
                    final ActivityOptions options = ActivityOptions.makeCustomAnimation(ActLogin.this, R.anim.slide_in_act2, R.anim.slide_out_act2);


                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(intent, options.toBundle());
                                    finish();
                                }
                            });
                        }
                    }, 250);
                }



            }
        });

    }



    private void getSession() {
        try {
            db = openOrCreateDatabase(DataBaseDB.DB_NAME, MODE_PRIVATE, null);
            c = db.rawQuery("SELECT * FROM " + DataBaseDB.TB_NAME_USUARIO, null);

            if (c.moveToFirst()) {

                final Intent intent = new Intent(ActLogin.this, Splash.class);
                startActivity(intent);
                finish();

            }
        } catch (Exception ex) {
            Log.e(TAG, "Error al consultar session: " + ex);
        } finally {
            c.close();
        }
    }

    public void Permissions(){
        Utils.hideKeyboard(this);
        findViewById(R.id.relativeMask).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.txtNotification)).setText("Actualmente la aplicacion no tiene los permisos necesarios, para poder continuar debes autorizarlos");
        ((ImageView) findViewById(R.id.imgMask)).setImageResource(R.drawable.ic_accessibility_white_24dp);
        findViewById(R.id.btnMaskCancelar).setVisibility(View.VISIBLE);
        findViewById(R.id.btnMaskActivar).setVisibility(View.VISIBLE);
        findViewById(R.id.btnMaskCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btnMaskActivar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.relativeMask).setVisibility(View.GONE);
                permission.askPermissions();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseMessaging.getInstance().subscribeToTopic("topic_general");
        preference.saveData("chanel_event", 1);
        preference.saveData("chanel_problem", 2);
        preference.saveData("chanel_notify", 3);



        if (  !ubicacion.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)  ){
            Utils.hideKeyboard(this);
            findViewById(R.id.relativeMask).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txtNotification)).setText("Actualmente tienes la ubicación desactivada, para poder continuar debes permitir acceso a la ubicación");
            ((ImageView) findViewById(R.id.imgMask)).setImageResource(R.drawable.ic_place_white);
            findViewById(R.id.btnMaskCancelar).setVisibility(View.VISIBLE);
            findViewById(R.id.btnMaskActivar).setVisibility(View.VISIBLE);
            findViewById(R.id.btnMaskCancelar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            findViewById(R.id.btnMaskActivar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
        }else if( !permission.checkPermissions()  ){
            Permissions();
        }else{
            getSession();
        }

    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.freeMemory();
    }
}
