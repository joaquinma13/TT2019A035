package com.example.joaquin.tt_des_v_100.Api.Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.joaquin.tt_des_v_100.R;


public class Connection {

    private Context context;

    public Connection(Context context) {
        this.context = context;
    }

    /**
     * @param info 0 -> Solamente verificar
     *             1 -> Informar siempre
     *             2 -> Informar solo si no tiene internet
     * @return boolean
     */
    public boolean getConnection(int info) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        /*--------------------------------- CONECTADO A INTERNET ---------------------------------*/
        if (activeNetwork != null) {
            /*------------------------------- CONECTADO A WIFI -----------------------------------*/
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                if (info == 1) {
                    final CustomAlert alert = new CustomAlert(context);
                    alert.setTypeCustom(ContextCompat.getDrawable(context, R.drawable.ic_wifi),
                            "CONECTADO", "Tu dispositivo tiene conexión a WiFi", "Ok");
                    alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.close();
                        }
                    });
                    alert.show();
                }
                return true;
            }
            /*------------------------------- CONECTADO A DATOS ----------------------------------*/
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (info == 1) {
                    final CustomAlert alert = new CustomAlert(context);
                    alert.setTypeCustom(ContextCompat.getDrawable(context, R.drawable.ic_network),
                            "CONECTADO", "Tu dispositivo tiene conexión móvil", "Ok");
                    alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.close();
                        }
                    });
                    alert.show();
                }
                return true;
            }
        }
        /*-------------------------------- NO CONECTADO A INTERNET -------------------------------*/
        else {
            if (info == 1 || info == 2) {
                final CustomAlert alert = new CustomAlert(context);
                alert.setTypeCustom(ContextCompat.getDrawable(context, R.drawable.ic_wifi_off),
                        "NO CONECTADO", "Tu Dispositivo no tiene Conexion a Internet", "Ok");
                alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.close();
                    }
                });
                alert.show();
            } else {
                return false;
            }
        }
        return false;
    }
}
