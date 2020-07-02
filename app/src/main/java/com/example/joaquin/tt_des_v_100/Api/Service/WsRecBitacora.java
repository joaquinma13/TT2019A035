package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Model.WsRecibeBitacora;
import com.example.joaquin.tt_des_v_100.Api.backservices.backServiceBitacora;
import com.example.joaquin.tt_des_v_100.R;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class WsRecBitacora {

    private Context context;
    private APIInterface apiInterface;

    public WsRecBitacora(Context context) {
        this.context = context;
        apiInterface = APIUtils.getUtils(context, "master").create(APIInterface.class);
    }


    public void setBitacora(final String id_user, final String senal, final String bateria, final String imei, final String modelo, final String latitud,
                            final String longitud, final String fecha) {


        final Call<WsRecibeBitacora> call = apiInterface.postRecibeBitacora(new WsRecibeBitacora(id_user, senal, bateria, imei, modelo, latitud, longitud, fecha));


        call.enqueue(new Callback<WsRecibeBitacora>() {
            @Override
            public void onResponse(final Call<WsRecibeBitacora> call, Response<WsRecibeBitacora> response) {

                Log.d(TAG, String.valueOf(response.code()));

                if (response.isSuccessful()) {

                    WsRecibeBitacora recBitacora = response.body();
                    try {
                        if (recBitacora.Estatus.toUpperCase().equals("OK"))
                            Log.i("Ubicacion actual", "Se mando la ubicacion al servidor correctamente");
                        else{
                            Toast.makeText(context,"RecibeMonitoreo: " + recBitacora.Estatus, Toast.LENGTH_LONG).show();
                            /*try {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    NotificationChannel mChannel = new NotificationChannel("EM", "Error", NotificationManager.IMPORTANCE_HIGH);
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "EM")
                                            .setSmallIcon(R.drawable.ic_warning)
                                            .setContentTitle("Error Monitoreo")
                                            .setChannelId("EM")
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText(recBitacora.Estatus))
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    mNotificationManager.createNotificationChannel(mChannel);
                                    mNotificationManager.notify(-20, mBuilder.build());

                                } else {
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "EM")
                                            .setSmallIcon(R.drawable.ic_warning)
                                            .setContentTitle("Error Monitoreo")
                                            .setOngoing(false)
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText(recBitacora.Estatus))
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                    notificationManager.notify(-20, mBuilder.build());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Utils.freeMemory();
                            }*/
                        }
                    }catch (Exception e){
                        Toast.makeText(context,"RecibeMonitoreo: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    System.out.println("response: " + response);
                }
            }

            @Override
            public void onFailure(Call<WsRecibeBitacora> call, Throwable t) {
                call.cancel();
                System.out.println("onFailure: " + t);

                if (t instanceof IOException) {
                    System.out.println("error en la red");
                }
                else {
                    System.out.println("error en API");
                }

            }
        });
    }

}
