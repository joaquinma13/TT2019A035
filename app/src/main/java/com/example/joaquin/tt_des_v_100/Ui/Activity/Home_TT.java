package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.androidmapsextensions.MarkerOptions;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseHelper;
import com.example.joaquin.tt_des_v_100.Api.backservices.LocationService;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.FrgZonas;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.FrgMap;
import com.example.joaquin.tt_des_v_100.Ui.Fragment.FrgContactos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.*;

public class Home_TT extends AppCompatActivity {

    /* Objetos de Base de datos*/
    private SQLiteDatabase db = null;
    public static DataBaseHelper sqliteHelper;
    private Cursor c = null;
    private Utils a;
    public static String tipo = "";
    public static String descrpcion = "";


    /* Objetos Pager*/
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private int[] tabIcons = {
            R.drawable.baseline_map_white_24dp,
            R.drawable.baseline_supervisor_account_white_24dp,
            R.drawable.baseline_location_on_white_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //a = new Utils(Home_TT.this);

        /* CREACION DE PAGER */
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {

            System.out.println("cadena: " + getIntent().getStringExtra("cadena"));



        }


        stopService(new Intent(this, LocationService.class));
        //startService(new Intent(this, LocationService.class));

        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setFocusable(false);

        setupTabIcons();

    }


    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        /*View v1 = LayoutInflater.from(Home_TT.this).inflate(R.layout.custom_tab, null);
        ImageView imgTab1 = v1.findViewById(R.id.imgTab);
        imgTab1.setImageResource(R.drawable.ic_map_white_24dp);
        tabLayout.getTabAt(0).setCustomView(v1);

        View v2 = LayoutInflater.from(Home_TT.this).inflate(R.layout.custom_tab, null);
        ImageView imgTab2 = v2.findViewById(R.id.imgTab);
        imgTab2.setImageResource(R.drawable.ic_contacts_white_24dp);
        tabLayout.getTabAt(1).setCustomView(v2);

        View v3 = LayoutInflater.from(Home_TT.this).inflate(R.layout.custom_tab, null);
        ImageView imgTab3 = v3.findViewById(R.id.imgTab);
        imgTab3.setImageResource(R.drawable.ic_place_white_24dp);
        tabLayout.getTabAt(2).setCustomView(v3);*/
    }


    private void addTabs(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FrgMap(), "MAPAS");
        adapter.addFrag(new FrgContactos(), "CONTACTOS");
        adapter.addFrag(new FrgZonas(), "ZONA");

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

            //return mFragmentTitleList.get(position);
            return null;

        }

    }

    public static Bitmap loadBitmapFromView(View v) {

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }
}
