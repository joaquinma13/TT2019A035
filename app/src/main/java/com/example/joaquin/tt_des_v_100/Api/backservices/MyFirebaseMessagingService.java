package com.example.joaquin.tt_des_v_100.Api.backservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static  final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SharePreference preference;
    private LocationLibrary ubicacion;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;


    @Override
    public void onNewToken(String s) {

        preference = SharePreference.getInstance(getApplicationContext());
        super.onNewToken(s);
        Log.e("TOKEN",s);
        preference.saveData("token", s);
    }
    

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("Bnaderaaaaa: " + remoteMessage.getData().get("bandera"));

        if( remoteMessage.getData().get("bandera").equals("4")  ){
            final String lat = remoteMessage.getData().get("latitud");
            final String log = remoteMessage.getData().get("longitud");
            final String tipo = remoteMessage.getData().get("tipo");

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run()
                {
                    ubicacion = new LocationLibrary(getApplicationContext(), "Mapa");
                    LatLng myLocation = ubicacion.getLocation().getUbicacion();
                    System.out.println("Latitud: " + myLocation.latitude + " Longitud: " + myLocation.longitude);

                    if (  CalculationByDistance(myLocation, new LatLng( Double.parseDouble(lat),Double.parseDouble(log))) > 1  ){
                        createNotificationChannel();
                        createNotification("Evento cercano", tipo);
                    }
                }
            });
        }




    }

    private double CalculationByDistance(LatLng StartP, LatLng EndP) {

        int Radius = 6371;
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;

        //return km / 0.001;
        return km;
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(String titulo, String texto){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_place_white_24dp);
        builder.setContentTitle(titulo);
        builder.setContentText(texto);
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }



}
