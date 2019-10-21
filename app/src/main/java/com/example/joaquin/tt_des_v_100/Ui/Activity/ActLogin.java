package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
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
import com.example.joaquin.tt_des_v_100.BuildConfig;
import com.example.joaquin.tt_des_v_100.Api.Class.Connection;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseHelper;
import com.example.joaquin.tt_des_v_100.R;
import com.tapadoo.alerter.Alerter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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

    // VARIABLE
    private String strPass = "12345678";
    public static boolean isSession = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);


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

        permission.checkPermissions(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                } else if (isSession) {
                    // Si las contraseñas son iguales abrimos el Activity de Planfia
                    if (pass.equals(strPass)) {
                        Utils.hideKeyboard(ActLogin.this);
                        final Intent intent = new Intent(ActLogin.this, Home_TT.class);
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
                    // Si las contraseñas son distintas mostrar mensaje de Error
                    else {
                        Alerter.create(ActLogin.this)
                                .setTitle("Contraseña incorrecta")
                                .setText("Por favor intentelo de nuevo")
                                .setIcon(ContextCompat.getDrawable(ActLogin.this, R.drawable.ic_lock))
                                .setBackgroundColorRes(R.color.colorPrimaryDark)
                                .setDuration(2000)
                                .show();
                    }
                } else {

                    /*if (connection.getConnection(2)) {
                        etUser.setEnabled(false);
                        etPass.setEnabled(false);
                        btnLogin.setEnabled(false);
                        new WsAut(ActLogin.this, "master").getWebLogin(user, pass);
                    }*/
                }


            }
        });

        btnPreregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CustomAlert alert = new CustomAlert(ActLogin.this);
                alert.setTypeWarning("No disponible", "Se esta trabajando en la vista", "Aceptar");
                alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.close();
                    }
                });
                alert.show();

                /*if (getCorreoPreregistro() == null) {
                    Intent intent = new Intent(ActLogin.this, Home_TT.class);
                    ;
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(ActLogin.this, R.anim.slide_in_act2, R.anim.slide_out_act2);
                    startActivity(intent, options.toBundle());
                } else {
                    final CustomAlert alert = new CustomAlert(ActLogin.this);
                    alert.setTypeWarning("Pre-registro activo", "Ya realizaste un pre-registro en este dispositivo", "Aceptar");
                    alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.close();
                        }
                    });
                    alert.show();
                }*/

            }
        });

        /*if (isSession) {

            btnPreregistro.setVisibility(View.GONE);

        } else {
            btnPreregistro.setVisibility(View.VISIBLE);
        }*/





        txtCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cerrar();
            }
        });

        //startSecuenceTutorial();


    }

    private String getCorreoPreregistro() {

        SQLiteDatabase db = null;
        Cursor c = null;
        String reply = null;


        try {
            db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " + DataBaseDB.CORREO_ELECTRONICO + " FROM " + DataBaseDB.TB_NAME_USUARIO, null);
            if (c.moveToFirst()) {
                reply = c.getString(0);
            }

        } catch (Exception ex) {
            Log.e(TAG, "getCorreoPreregistro: " + ex.toString());
        } finally {
            Utils.close(c);
            Utils.close(db);
            Utils.freeMemory();
        }
        System.out.println("Valos del estatus : " + reply);
        return reply;

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
