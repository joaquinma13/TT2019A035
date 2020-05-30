package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Service.WsAut;
import com.example.joaquin.tt_des_v_100.Api.Service.WsRecSplash;
import com.example.joaquin.tt_des_v_100.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private ImageView engrane;
    private SharePreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initBindig();
        startAnimationEngine(this);
        restartDB();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startThreadCats();
            }
        }, 3500);


    }

    private void initBindig() {
        preference = SharePreference.getInstance(this);
        engrane = findViewById(R.id.engrane);
    }


    private void startAnimationEngine(final Activity act) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation rotation = AnimationUtils.loadAnimation(act, R.anim.rotate);
                rotation.setFillAfter(true);
                rotation.reset();
                engrane.startAnimation(rotation);
            }
        });

    }

    public void restartDB(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db;
                db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
                getContactList(db);
                db.delete(DataBaseDB.TB_ZONAS, null, null);
                db.close();

            }
        }).start();
    }

    private void startThreadCats() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                new WsRecSplash(Splash.this, "master").startThreads(preference.getStrData("id_user"), engrane);
            }
        }).start();

    }

    public void getContactList(SQLiteDatabase db) {
        HashMap<String, String> contactos = new HashMap<String, String>();
        Utils.itemsContact.clear();
        db.delete(DataBaseDB.TB_CONTACTO, null, null);
        String[] projeccion = new String[] { ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE };
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                null);
        while(c.moveToNext()){
            String aux = c.getString(1).replace(" ", "");
            if (aux.length() > 9){
                contactos.put(aux.substring( aux.length() - 10 ), c.getString(0));
            }

        }
        c.close();
        if (db != null) {
            ContentValues values = new ContentValues();
            for (Map.Entry<String, String> entry : contactos.entrySet()) {

                values.put(DataBaseDB.NOMBRE, entry.getValue());
                values.put(DataBaseDB.TELEFONO, entry.getKey());
                values.put(DataBaseDB.ESTATUS, "nulo");
                db.insert(DataBaseDB.TB_CONTACTO, null, values);
            }
        }else{
            System.out.println("puntero cerrado");
        }
    }
}
