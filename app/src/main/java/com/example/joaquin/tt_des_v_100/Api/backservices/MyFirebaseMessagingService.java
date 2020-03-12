package com.example.joaquin.tt_des_v_100.Api.backservices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActRegistro;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static  final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SharePreference preference;


    @Override
    public void onNewToken(String s) {

        preference = SharePreference.getInstance(getApplicationContext());

        /*
            En este método recibimos el 'token' del dispositivo.
            Lo necesitamos si vamos a comunicarnos con el dispositivo directamente.
        */
        super.onNewToken(s);
        Log.e("TOKEN",s);
        preference.saveData("token", s);
        /*
            A partir de aquí podemos hacer lo que queramos con el token como
            enviarlo al servidor para guardarlo en una B.DD.
            Nosotros no haremos nada con el token porque no nos vamos a comunicar con un sólo
            dispositivo.
         */
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    }



}
