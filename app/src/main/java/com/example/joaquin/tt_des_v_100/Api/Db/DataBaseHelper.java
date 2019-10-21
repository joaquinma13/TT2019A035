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
                DataBaseDB.USUARIO + " TEXT, " +                // 0
                DataBaseDB.CONTRASENA + " TEXT, " +             // 1
                DataBaseDB.NOMBRE + " TEXT, " +                 // 2
                DataBaseDB.PERFIL + " TEXT, " +                 // 3
                DataBaseDB.NOMBRE_PERFIL + " TEXT, " +          // 4
                DataBaseDB.SUPERVISOR + " TEXT, " +             // 5
                DataBaseDB.FECHA_ALTA + " TEXT, " +             // 6
                DataBaseDB.CORREO_ELECTRONICO + " TEXT, " +     // 7
                DataBaseDB.ESTATUS_USUARIO + " TEXT); "         // 8
        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_NAME_USUARIO");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DataBaseDB.TB_NAME_CAT_0 + "(" +
                DataBaseDB.CAT_PREG_ID + " INTEGER, " +             // 0
                DataBaseDB.CAT_PREG_CONSECUTIVO + " INTEGER, " +    // 1
                DataBaseDB.CAT_PREG_DESCRIPCION + " TEXT, " +       // 2
                DataBaseDB.CAT_PREG_BLOQUE + " TEXT, " +            // 3
                DataBaseDB.CAT_PREG_SUBLOQUE + " TEXT, " +          // 4
                DataBaseDB.CAT_PREG_HEADER + " TEXT, " +            // 5
                DataBaseDB.CAT_PREG_AGENDABLE + " TEXT, " +         // 6
                DataBaseDB.CAT_PREG_CAPTURA + " TEXT, " +           // 7
                DataBaseDB.CAT_PREG_EDICION + " TEXT, " +           // 8
                DataBaseDB.CAT_PREG_LECTURA + " TEXT, " +           // 9
                DataBaseDB.CAT_PREG_ESTATUS + " TEXT, " +           // 10
                DataBaseDB.CAT_PREG_PROCESO + " TEXT, " +           // 11
                DataBaseDB.CAT_PREG_TIPORESPUESTA + " TEXT, " +     // 12
                DataBaseDB.CAT_PREG_TIPODATO + " TEXT, " +          // 13
                DataBaseDB.CAT_PREG_LONGITUD + " TEXT, " +          // 14
                DataBaseDB.CAT_PREG_OBLIGATORIA + " TEXT, " +       // 15
                DataBaseDB.CAT_PREG_DOCS_MAX + " TEXT, " +          // 16
                DataBaseDB.CAT_PREG_DOCS_MIN + " TEXT, " +          // 17
                DataBaseDB.CAT_PREG_COLOR + " TEXT);"               // 18   git shor log

        );
        Log.i(TAG, "CREATE TABLE IF NOT EXISTS DataBaseDB.TB_NAME_CAT_0");
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
