package com.example.joaquin.tt_des_v_100.Api.backservices;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecBitacora;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class backServiceBitacora extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("EnvioAutomatico", "se inicio el servicio");

        final LocationLibrary ubicacion;
        ubicacion = new LocationLibrary(getApplicationContext(), "Mapa");
        final LatLng myLocation = ubicacion.getLocation().getUbicacion();

        final Permission permission;
        permission = new Permission(getApplicationContext());

        final Date date = new Date();
        final DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final SharePreference preference;
        preference = SharePreference.getInstance(getApplicationContext());

        //myTask = new MyTask();
        //myTask.execute();

        //startCheckThread();

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //Ejecuta tu AsyncTask!
                            AsyncTask myTask = new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] objects) {

                                    //Calcular el porcentaje de bateria actual
                                    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                                    Intent batteryStatus = registerReceiver(null, ifilter);
                                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                                    float batteryPct = level / (float) scale;

                                    if (permission.checkPermissionIMEI()) {
                                        WsRecBitacora sendBtacora = new WsRecBitacora( getBaseContext() );
                                        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                                        sendBtacora.setBitacora(preference.getStrData("id_user"),String.valueOf(getMobileDataEstatus()),String.valueOf((int) (batteryPct * 100)),telephonyManager.getDeviceId(), Build.MODEL,String.valueOf(myLocation.latitude),String.valueOf(myLocation.longitude),hourFormat.format(date));

                                    }

                                    return null;
                                }
                            };
                            myTask.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 1000*60*2);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //isService = false;
        super.onDestroy();
        System.out.println("se acabo el servicio");
        //System.out.println(“El servicio a Terminado”);
    }

    private int getMobileDataEstatus() {
        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mData.isConnected()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*private void ejecutar(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                metodoEjecutar();//llamamos nuestro metodo
                handler.postDelayed(this,30000);//se ejecutara cada 10 segundos
            }
        },5000);//empezara a ejecutarse después de 5 milisegundos
    }
    private void metodoEjecutar() {
        System.out.println("que pedooo?");
    }*/
}

