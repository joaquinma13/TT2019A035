package com.example.joaquin.tt_des_v_100.Api.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DataBaseHelper.class.getName();

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        //---------------------------------- TABLE_NAME_USUARIO ------------------------------------
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_NAME_USUARIO + "(" +
                DataBaseDB.ID_USER + " TEXT, " +                // 0
                DataBaseDB.NOMBRE + " TEXT, " +                 // 1
                DataBaseDB.CORREO_ELECTRONICO + " TEXT, " +     // 2
                DataBaseDB.TELEFONO + " TEXT, " +               // 3
                DataBaseDB.CONTRASENA + " TEXT); "              // 4
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_NAME_USUARIO");

        //---------------------------------- TB_CONTACTO ------------------------------------
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_CONTACTO + "(" +
                DataBaseDB.ID_USER + " TEXT, " +        // 0
                DataBaseDB.NOMBRE + " TEXT, " +        // 0
                DataBaseDB.SENAL + " TEXT, " +        // 0
                DataBaseDB.BATERIA + " TEXT, " +        // 0
                DataBaseDB.IMEI + " TEXT, " +        // 0
                DataBaseDB.MODELO + " TEXT, " +        // 0
                DataBaseDB.LATITUD + " TEXT, " +        // 0
                DataBaseDB.LONGITUD + " TEXT, " +        // 0
                DataBaseDB.TELEFONO + " TEXT, " +        // 0
                DataBaseDB.FECHA+ " TEXT); "         // 1
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_CONTACTO");

        //---------------------------------- TB_ZONA ------------------------------------
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_ZONAS + "(" +
                DataBaseDB.NOMBRE + " TEXT, " +          // 0
                DataBaseDB.LATITUD + " TEXT, " +         // 1
                DataBaseDB.LONGITUD + " TEXT); "         // 2
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_ZONAS");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {

        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            super.onDowngrade(db, oldVersion, newVersion);
            Log.e(TAG, "ALTER TABLE onDowngrade");
        } catch (Exception e) {
            Log.e(TAG, "Exception onDowngrade: " + e.toString());
        }
    }
}
