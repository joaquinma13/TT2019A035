package com.example.joaquin.tt_des_v_100.Api.backservices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.LocationLibrary;
import com.example.joaquin.tt_des_v_100.Api.Class.Permission;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecBitacora;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActLogin;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.example.joaquin.tt_des_v_100.Ui.Adapter.AdpCuentas;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.FrgContactos;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static  final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SharePreference preference;
    private LocationLibrary ubicacion;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private PendingIntent pendingIntent;
    private Permission permission;


    @Override
    public void onNewToken(String s) {

        preference = SharePreference.getInstance(getApplicationContext());
        super.onNewToken(s);
        Log.e("TOKEN",s);
        preference.saveData("token", s);
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final RemoteMessage aux = remoteMessage;

        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run()
            {

                preference = SharePreference.getInstance(getApplicationContext());


                if(aux.getData().get("bandera").equals("1")){

                    System.out.println("bandera 1");


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
                                reloadContact(db);
                                createNotificationChannel();
                                createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));
                            }

                        }else{
                            reloadContact(db);
                            setPendingIntent();
                            createNotificationChannel();
                            createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));
                        }

                    }

                    db.close();

                }else if(aux.getData().get("bandera").equals("2")){

                    System.out.println("bandera 2");

                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    if (db != null) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseDB.ESTATUS, "Activo");

                        db.update(DataBaseDB.TB_CONTACTO, values,
                                DataBaseDB.ID_USER + "='" + aux.getData().get("id_user")  + "'", null);

                        reloadContact(db);
                        setPendingIntent();
                        createNotificationChannel();
                        createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));

                    }

                    db.close();

                }else if(aux.getData().get("bandera").equals("3")){

                    System.out.println("bandera 3");

                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                    if (db != null) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseDB.ESTATUS, "nulo");

                        db.update(DataBaseDB.TB_CONTACTO, values,
                                DataBaseDB.ID_USER + "='" + aux.getData().get("id_user")  + "'", null);

                        reloadContact(db);
                        setPendingIntent();
                        createNotificationChannel();
                        createNotificationGen(aux.getData().get("title"), aux.getData().get("body"));
                    }
                    db.close();

                }else if( aux.getData().get("bandera").equals("4")  ){



                    if (!preference.getStrData("id_user").equals(aux.getData().get("id_user"))){

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
                        values.put(DataBaseDB.ESTADO, "positivo");
                        db.insert(DataBaseDB.TB_EVENTO, null, values);
                        db.close();

                        ubicacion = new LocationLibrary(getApplicationContext(), "Mapa");
                        LatLng myLocation = ubicacion.getLocation().getUbicacion();

                        if (  CalculationByDistance(myLocation, new LatLng( Double.parseDouble(aux.getData().get("latitud")),Double.parseDouble(aux.getData().get("longitud")))) < 1  ){

                            setPendingIntent();
                            createNotificationChannel();
                            createNotificationEvent(aux.getData().get("tipo"), aux.getData().get("descripcion"));
                        }
                    }
                }else if(aux.getData().get("bandera").equals("5")){

                    System.out.println("bandera 5");

                    ubicacion = new LocationLibrary(getApplicationContext(), "Mapa");
                    LatLng myLocation = ubicacion.getLocation().getUbicacion();
                    permission = new Permission(getApplicationContext());

                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    //Calcular el porcentaje de bateria actual
                    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = registerReceiver(null, ifilter);
                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    float batteryPct = level / (float) scale;

                    if (permission.checkPermissionIMEI()) {
                        WsRecBitacora sendBtacora = new WsRecBitacora( getBaseContext() );
                        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                        sendBtacora.setBitacora(preference.getStrData("id_user"),String.valueOf(getMobileDataEstatus()),String.valueOf((int) (batteryPct * 100)),telephonyManager.getDeviceId(),Build.MODEL,String.valueOf(myLocation.latitude),String.valueOf(myLocation.longitude),hourFormat.format(date));

                    }
                }else if(aux.getData().get("bandera").equals("6")){

                    System.out.println("bandera 6");

                    //final String mensaje = "Hola " + aux.getData().get("nombre") +  ", te mandamos un mensaje al email: " + aux.getData().get("corre") + "esperamos lo puedas atender a la brevedad";

                    setPendingIntent();
                    createNotificationChannel();
                    createNotificationProblem("Atención", "Ocurrio un problema con uno de tus Eventos.");
                }else if(aux.getData().get("bandera").equals("7")){

                    System.out.println("bandera 7");
                    SQLiteDatabase db;
                    db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
                    String nombre = "";
                    if (db != null) {
                        nombre = returnContact(db, aux.getData().get("id_user"));
                    }
                    db.close();
                    setPendingIntent();
                    createNotificationChannel();
                    createNotificationProblem("Urgente!!!", "Su contacto " + nombre + " podria estar en peligro");
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

    private void createNotificationProblem(String titulo, String texto){

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
        notificationManagerCompat.notify(preference.getIntData("chanel_problem"), builder.build());
    }

    private void reloadContact(SQLiteDatabase db){
        Utils.itemsContact.clear();
        Cursor c = null;

        try {
            c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + ", " +
                    DataBaseDB.TELEFONO + ", " +
                    DataBaseDB.ESTATUS +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " ORDER BY " + DataBaseDB.NOMBRE, null);

            if (c.moveToFirst()) {
                do {
                    Utils.itemsContact.add(new Item(c.getString(0),c.getString(1),c.getString(2)));
                } while (c.moveToNext());

            } else {
                System.out.println("No existen registros!!!");
            }

        } catch (Exception ex) {
            Log.e(TAG, "getCorreoPreregistro: " + ex.toString());
        } finally {
            Utils.close(c);
            Utils.freeMemory();
        }
    }

    private String returnContact(SQLiteDatabase db, String id){
        Utils.itemsContact.clear();
        Cursor c = null;

        try {
            c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE +
                    " FROM " + DataBaseDB.TB_CONTACTO +
                    " WHERE " + DataBaseDB.ID_USER +
                    " = '" + id + "'", null);

            if (c.moveToFirst()) {
                do {
                    return c.getString(0);
                } while (c.moveToNext());

            } else {
                return "algo";
            }

        } catch (Exception ex) {
            Log.e(TAG, "getCorreoPreregistro: " + ex.toString());
        } finally {
            Utils.close(c);
            Utils.freeMemory();
        }
        return "algo";
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



}
