package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseHelper;
import com.example.joaquin.tt_des_v_100.Api.backservices.LocationService;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.Fragment_dos;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.Fragment_tres;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.Fragment_uno;

import java.util.ArrayList;
import java.util.List;

public class Home_TT extends AppCompatActivity {

    /* Objetos de Base de datos*/
    private SQLiteDatabase db = null;
    public static DataBaseHelper sqliteHelper;
    private Cursor c = null;


    /* Objetos Pager*/
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* CREACION DE PAGER */
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            SQLiteDatabase db;
            db = openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

            if(getIntent().getStringExtra("bandera").equals("1")){
                int idSave = 0;
                if (db != null) {
                    ContentValues values = new ContentValues();
                    values.put(DataBaseDB.ID_USER, getIntent().getStringExtra("id_user"));
                    values.put(DataBaseDB.NOMBRE, getIntent().getStringExtra("nombre"));
                    values.put(DataBaseDB.ESTATUS, "Pendiente2");
                    idSave = db.update(DataBaseDB.TB_CONTACTO, values,
                            DataBaseDB.TELEFONO + "='" + getIntent().getStringExtra("telefono")  + "'", null);

                    if (idSave == 0){

                        values.clear();
                        values.put(DataBaseDB.ID_USER, getIntent().getStringExtra("id_user"));
                        values.put(DataBaseDB.NOMBRE, getIntent().getStringExtra("nombre"));
                        values.put(DataBaseDB.TELEFONO, getIntent().getStringExtra("telefono"));
                        values.put(DataBaseDB.ESTATUS, "Pendiente2");
                        db.insert(DataBaseDB.TB_CONTACTO, null, values);

                    }

                }else{
                    System.out.println("puntero cerrado");
                }
            }else if (getIntent().getStringExtra("bandera").equals("2")){
                if (db != null) {
                    ContentValues values = new ContentValues();
                    values.put(DataBaseDB.ESTATUS, "Activo");
                    db.update(DataBaseDB.TB_CONTACTO, values,
                            DataBaseDB.ID_USER + "='" + getIntent().getStringExtra("id_user")  + "'", null);

                }else{
                    System.out.println("puntero cerrado");
                }
            }else if (getIntent().getStringExtra("bandera").equals("3")){
                if (db != null) {
                    ContentValues values = new ContentValues();
                    values.put(DataBaseDB.ESTATUS, "nulo");
                    db.update(DataBaseDB.TB_CONTACTO, values,
                            DataBaseDB.ID_USER + "='" + getIntent().getStringExtra("id_user")  + "'", null);

                }else{
                    System.out.println("puntero cerrado");
                }
            }

            db.close();

        }

        stopService(new Intent(this, LocationService.class));
        //startService(new Intent(this, LocationService.class));

        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();

    }

    /*
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }
    */

    private void addTabs(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment_uno(), "CONTACTOS");
        adapter.addFrag(new Fragment_dos(), "ZONA");
        adapter.addFrag(new Fragment_tres(), "MAPAS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
