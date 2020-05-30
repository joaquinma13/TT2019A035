package com.example.joaquin.tt_des_v_100.Api.backservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActLogin;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static  final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SharePreference preference;
    private LocationLibrary ubicacion;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private PendingIntent pendingIntent;


    @Override
    public void onNewToken(String s) {

        preference = SharePreference.getInstance(getApplicationContext());
        super.onNewToken(s);
        Log.e("TOKEN",s);
        preference.saveData("token", s);
    }
    

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final RemoteMessage aux = remoteMessage;

        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run()
            {

                preference = SharePreference.getInstance(getApplicationContext());


                if(aux.getData().get("bandera").equals("1")){

                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    int idSave = 0;
                    if (db != null) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseDB.ID_USER, aux.getData().get("id_user"));
                        values.put(DataBaseDB.ESTATUS, "Pendiente2");
                        values.put(DataBaseDB.NOMBRE, aux.getData().get("nombre"));
                        idSave = db.update(DataBaseDB.TB_CONTACTO, values,
                                DataBaseDB.TELEFONO + "='" + aux.getData().get("telefono")  + "'", null);

                        if (idSave == 0){

                            values.clear();
                            values.put(DataBaseDB.ID_USER, aux.getData().get("id_user"));
                            values.put(DataBaseDB.TELEFONO, aux.getData().get("telefono"));
                            values.put(DataBaseDB.ESTATUS, "Pendiente2");
                            values.put(DataBaseDB.NOMBRE, aux.getData().get("nombre"));
                            idSave = (int) db.insert(DataBaseDB.TB_CONTACTO, null, values);

                            if ( idSave > 0 ){
                                createNotificationChannel();
                                createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));
                            }

                        }else{
                            createNotificationChannel();
                            createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));
                        }

                    }

                    db.close();

                }else if(aux.getData().get("bandera").equals("2")){

                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    if (db != null) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseDB.ESTATUS, "Activo");

                        db.update(DataBaseDB.TB_CONTACTO, values,
                                DataBaseDB.ID_USER + "='" + aux.getData().get("id_user")  + "'", null);

                        createNotificationChannel();
                        createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));

                    }

                    db.close();

                }else if(aux.getData().get("bandera").equals("3")){

                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    if (db != null) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseDB.ESTATUS, "nulo");

                        db.update(DataBaseDB.TB_CONTACTO, values,
                                DataBaseDB.ID_USER + "='" + aux.getData().get("id_user")  + "'", null);

                        createNotificationChannel();
                        createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));
                    }
                    db.close();

                }else if( aux.getData().get("bandera").equals("4")  ){

                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    ContentValues values = new ContentValues();

                    values.clear();
                    values.put(DataBaseDB.CADENA, aux.getData().get("token"));
                    values.put(DataBaseDB.RUTA, aux.getData().get("ruta"));
                    values.put(DataBaseDB.DESCRIPCION, aux.getData().get("descripcion"));
                    values.put(DataBaseDB.ID_USER, aux.getData().get("id_user"));
                    values.put(DataBaseDB.TIPO, aux.getData().get("tipo"));
                    values.put(DataBaseDB.LATITUD, aux.getData().get("latitud"));
                    values.put(DataBaseDB.LONGITUD, aux.getData().get("longitud"));
                    db.insert(DataBaseDB.TB_EVENTO, null, values);



                    db.close();



                    ubicacion = new LocationLibrary(getApplicationContext(), "Mapa");
                    LatLng myLocation = ubicacion.getLocation().getUbicacion();

                    if (  CalculationByDistance(myLocation, new LatLng( Double.parseDouble(aux.getData().get("latitud")),Double.parseDouble(aux.getData().get("longitud")))) < 1  ){

                        setPendingIntent();
                        createNotificationChannel();
                        createNotificationEvent(aux.getData().get("tipo"), aux.getData().get("descripcion"));
                    }

                    /*if (!preference.getStrData("id_user").equals(aux.getData().get("id_user"))){

                        preference.saveData("latitud", aux.getData().get("latitud"));
                        preference.saveData("longitud", aux.getData().get("longitud"));
                        preference.saveData("tipo", aux.getData().get("tipo"));
                        preference.saveData("descripcion", aux.getData().get("descripcion"));


                        ubicacion = new LocationLibrary(getApplicationContext(), "Mapa");
                        LatLng myLocation = ubicacion.getLocation().getUbicacion();

                        if (  CalculationByDistance(myLocation, new LatLng( Double.parseDouble(aux.getData().get("latitud")),Double.parseDouble(aux.getData().get("longitud")))) < 1  ){

                            setPendingIntent();
                            createNotificationChannel();
                            createNotification(aux.getData().get("tipo"), aux.getData().get("descripcion"));
                        }
                    }*/
                }

            }
        });
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

        return km;
    }

    private void setPendingIntent(){

        Intent intent = new Intent(this, Home_TT.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Home_TT.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotificationGen(String titulo, String texto){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(titulo);
        builder.setContentText(texto);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(preference.getIntData("chanel_notify"), builder.build());
        int a = preference.getIntData("chanel_notify") + 1;
        preference.saveData("chanel_notify", a);
    }

    private void createNotificationEvent(String titulo, String texto){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(titulo);
        builder.setContentText(texto);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(preference.getIntData("chanel_event"), builder.build());
    }



}
