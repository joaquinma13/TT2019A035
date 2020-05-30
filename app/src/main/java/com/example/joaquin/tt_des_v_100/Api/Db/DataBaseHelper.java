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
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_   NAME_USUARIO");



        //---------------------------------- TB_CONTACTO ------------------------------------
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_CONTACTO + "(" +
                DataBaseDB.ID_USER + " TEXT, " +        // 0
                DataBaseDB.NOMBRE + " TEXT, " +         // 1
                DataBaseDB.SENAL + " TEXT, " +          // 2
                DataBaseDB.BATERIA + " TEXT, " +        // 3
                DataBaseDB.IMEI + " TEXT, " +           // 4
                DataBaseDB.MODELO + " TEXT, " +         // 5
                DataBaseDB.LATITUD + " TEXT, " +        // 6
                DataBaseDB.LONGITUD + " TEXT, " +       // 7
                DataBaseDB.TELEFONO + " TEXT, " +       // 8
                DataBaseDB.ESTATUS + " TEXT, " +        // 9
                DataBaseDB.FECHA+ " TEXT); "            // 10
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_CONTACTO");

        //---------------------------------- TB_ZONA ------------------------------------
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_ZONAS + "(" +
                DataBaseDB.ID_ZONA + " TEXT, " +         // 1
                DataBaseDB.RADIO + " TEXT, " +         // 1
                DataBaseDB.NOMBRE + " TEXT, " +          // 0
                DataBaseDB.LATITUD + " TEXT, " +         // 1
                DataBaseDB.LONGITUD + " TEXT); "         // 2
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_ZONAS");


        //---------------------------------- TB_EVENTO ------------------------------------
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_EVENTO + "(" +
                DataBaseDB.CADENA + " TEXT, " +         // 0
                DataBaseDB.RUTA + " TEXT, " +           // 1
                DataBaseDB.DESCRIPCION + " TEXT, " +    // 2
                DataBaseDB.ID_USER + " TEXT, " +        // 3
                DataBaseDB.TIPO + " TEXT, " +           // 4
                DataBaseDB.LATITUD + " TEXT, " +        // 5
                DataBaseDB.LONGITUD + " TEXT); "        // 6
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_EVENTO");



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
