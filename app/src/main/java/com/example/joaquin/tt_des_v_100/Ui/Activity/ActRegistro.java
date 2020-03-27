package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecUser;
import com.example.joaquin.tt_des_v_100.R;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ActRegistro extends AppCompatActivity {


    Button btnGuardar;
    // UI
    private TextInputLayout inputNomUser;
    private TextInputLayout inputCorreo;
    private TextInputLayout inputNumTel;
    private TextInputLayout inputPassword;
    private TextInputLayout inputConfirmar;

    private TextInputEditText editNomUser;
    private TextInputEditText editCorreo;
    private TextInputEditText editNumTel;
    public static EditText editTextPassword;
    public static EditText editTextConfirmar;

    private RelativeLayout relCodigo;
    private ScrollView scrollForm;
    private TextView titleCodigo;
    private TextInputLayout inputCodigo;
    private TextInputEditText editCodigo;

    public static String bandera = "0";
    private SharePreference preference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_registro);
        preference = SharePreference.getInstance(getApplicationContext());

        initBinding();
        listeners();


    }

    public void initBinding(){

        btnGuardar = findViewById(R.id.btnGuardar);
        inputNomUser = findViewById(R.id.inputNomUser);
        inputCorreo = findViewById(R.id.inputCorreo);
        inputNumTel = findViewById(R.id.inputNumTel);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmar = findViewById(R.id.inputConfirmar);

        editNomUser= findViewById(R.id.editNomUser);
        editCorreo= findViewById(R.id.editCorreo);
        editNumTel= findViewById(R.id.editNumTel);
        editTextPassword= findViewById(R.id.editTextPassword);
        editTextConfirmar= findViewById(R.id.editTextConfirmar);

        relCodigo = findViewById(R.id.relCodigo);
        scrollForm  = findViewById(R.id.scrollForm);
        titleCodigo  = findViewById(R.id.titleCodigo);
        inputCodigo  = findViewById(R.id.inputCodigo);
        editCodigo  = findViewById(R.id.editCodigo);

    }

    public void listeners(){

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActRegistro.bandera.equals("0")){
                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");


                    boolean flag = true;

                    if ( editNomUser.getText().toString().trim().equalsIgnoreCase("")) {
                        inputNomUser.setError("Usuario obligatorio");
                        flag = false;
                    }
                    if ( editCorreo.getText().toString().trim().equalsIgnoreCase("")) {
                        inputCorreo.setError("Correo obligatorio");
                        flag = false;
                    }
                    if ( editNumTel.getText().toString().trim().equalsIgnoreCase("")) {
                        inputNumTel.setError("Telefono obligatorio");
                        flag = false;
                    }
                    if ( editTextPassword.getText().toString().trim().equalsIgnoreCase("")) {
                        inputPassword.setError("Contraseña obligatoria");
                        flag = false;
                    }
                    if ( editTextConfirmar.getText().toString().trim().equalsIgnoreCase("")) {
                        inputConfirmar.setError("Confirmacion obligatoria");
                        flag = false;
                    }

                    if (flag){
                        preference.saveData("id_user",hourFormat.format(date) + "-" + editNumTel.getText().toString());
                        new WsRecUser(ActRegistro.this, "master").setUser(preference.getStrData("id_user"), editNomUser.getText().toString(), editCorreo.getText().toString(),
                                editNumTel.getText().toString(), editTextPassword.getText().toString(), ActRegistro.bandera, relCodigo, scrollForm, titleCodigo);
                        ActRegistro.bandera = "1";

                    }else{
                        Alerter.create(ActRegistro.this)
                                .setTitle("Revise la informacion solicitada.")
                                .setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_error_white))
                                .setBackgroundColorRes(R.color.red)
                                .setDuration(2000)
                                .show();
                    }


                }else{
                    new WsRecUser(ActRegistro.this, "master").setUser("11111", editCodigo.getText().toString(),
                            editTextPassword.getText().toString(), ActRegistro.bandera);
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(ActRegistro.this, ActLogin.class);
        final ActivityOptions options = ActivityOptions.makeCustomAnimation(ActRegistro.this, R.anim.slide_in_act2, R.anim.slide_out_act2);


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
