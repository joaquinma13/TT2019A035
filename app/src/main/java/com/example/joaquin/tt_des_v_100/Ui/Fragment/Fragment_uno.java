package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Listener.Recycler;
import com.example.joaquin.tt_des_v_100.Api.Service.WsVinculaUser;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Adapter.AdpCuentas;
import com.google.android.gms.maps.model.LatLng;
import com.tapadoo.alerter.Alerter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class Fragment_uno extends Fragment {

    private FloatingActionButton btnGetContact;
    public static RecyclerView recyclerContact;
    public static AdpCuentas recyclerAdapter;
    private ArrayList<Item> contacto = new ArrayList<>();
    public static ArrayList<Item> itemsContact = new ArrayList<>();
    public static ArrayList<String> selectedContact = new ArrayList<>();
    public static ArrayList<String> selectedPhone = new ArrayList<>();
    public static int count = 0;
    public static int error = 0;
    private SharePreference preference;
    private RelativeLayout relAlerta;

    public Fragment_uno() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uno, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //putElements();
        preference = SharePreference.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContact();
                getContactList();
            }
        }).start();
        btnGetContact = getActivity().findViewById(R.id.btnGetContact);
        recyclerContact = getActivity().findViewById(R.id.recyclerContact);

        relAlerta = getActivity().findViewById(R.id.relAlerta);

        recyclerContact.setHasFixedSize(true);
        recyclerContact.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapter = new AdpCuentas(getActivity(), contacto);
        recyclerContact.setAdapter(recyclerAdapter);

        btnGetContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedContact.clear();
                //getContactList();
                System.out.println("Entre en onClick");
                final CustomAlert alerta = new CustomAlert(getActivity());
                alerta.setTypeCustomBtn(
                        ContextCompat.getDrawable(getActivity(), R.drawable.baseline_person_24),
                        "CONTACTOS",
                        "OK"
                );

                final View viewE = getLayoutInflater().inflate(R.layout.item_tipo_ruteo, null);

                LinearLayout layout = viewE.findViewById(R.id.checkBoxGroupL);
                CheckBox cb;
                View child;
                for (int i = 0; i < itemsContact.size(); i++) {
                    child = getLayoutInflater().inflate(R.layout.checbox_group_route, null);

                    cb = child.findViewById(R.id.checkBox);

                    cb.setText(itemsContact.get(i).getNombre());

                    cb.setId(i);

                    for(Item a : contacto){

                        if (a.getNombre().equals(itemsContact.get(i).getNombre()))
                            cb.setEnabled(false);

                    }

                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                final CustomAlert alert = new CustomAlert(getActivity());
                                alert.setTypeProgress("Conectando...", "", "Cancelar");
                                alert.setCancelable(false);
                                alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        relAlerta.setVisibility(View.VISIBLE);
                                        alert.close();
                                    }
                                });
                                relAlerta.setVisibility(View.GONE);
                                alert.show();
                                /*
                                System.out.println("nombre: " + compoundButton.getText().toString());
                                System.out.println("telefono: " + itemsContact.get(compoundButton.getId()).getTelefono().replace(" ",""));
                                //System.out.println("LONGITUD: " + itemsContact.get(compoundButton.getId()).getTelefono().replace(" ","").substring(itemsContact.get(compoundButton.getId()).getTelefono().replace(" ","").length() - 10)  );
                                selectedContact.add(compoundButton.getText().toString());
                                selectedPhone.add( itemsContact.get(compoundButton.getId()).getTelefono().replace(" ","").substring(itemsContact.get(compoundButton.getId()).getTelefono().replace(" ","").length() - 10)  );
                            */
                            } else {
                                for (int i = 0; i < selectedContact.size(); i++) {
                                    if (selectedContact.get(i).equals((compoundButton.getText().toString()))) {
                                        selectedContact.remove(i);
                                        selectedPhone.remove(i);
                                    }
                                }
                            }
                        }
                    });

                    layout.addView(child);
                }

                alerta.getClose().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alerta.close();
                    }
                });

                alerta.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Fragment_uno.count = 0;
                        Fragment_uno.error = 0;
                        SQLiteDatabase db = null;
                        alerta.close();

                        final CustomAlert alert = new CustomAlert(getActivity());
                        alert.setTypeProgress("Conectando...", "", "Cancelar");
                        alert.setCancelable(false);
                        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.close();
                            }
                        });
                        alert.show();

                        try {
                            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
                            for (int i = 0; i < selectedContact.size(); i++) {
                                new WsVinculaUser(getActivity(), "master").setVinculo(preference.getStrData("id_user"), selectedPhone.get(i), selectedContact.get(i),db,alert);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            /*if (db != null) {
                                db.close();
                                db = null;
                            }*/
                            Utils.freeMemory();
                        }


                    }
                });


                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int height = metrics.heightPixels;
                alerta.setCancelable(false);
                alerta.addView(viewE);
                alerta.ajustar(height);
                //TODO alerta.setCancelActionForRouting("Estado", getView(), 1);
                Utils.freeMemory();
                alerta.show();
                Utils.freeMemory();
            }
        });


        recyclerContact.addOnItemTouchListener(new Recycler(getActivity(), recyclerContact, new Recycler.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                System.out.println("Esto es onClick");
            }

            @Override
            public void onLongClick(View view, int position) {
                System.out.println("Esto es onLongClick");
            }
        }));
    }


    public void putElements(){
        SQLiteDatabase db = null;

        try {
            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

            ContentValues values = new ContentValues();
            values.put(DataBaseDB.NOMBRE, "Luis");
            values.put(DataBaseDB.TELEFONO, "11111111");
            db.insert(DataBaseDB.TB_CONTACTO, null, values);

            values.clear();

            values.put(DataBaseDB.NOMBRE, "Juan");
            values.put(DataBaseDB.TELEFONO, "22222222");
            db.insert(DataBaseDB.TB_CONTACTO, null, values);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
            Utils.freeMemory();
        }
    }

    public void getContact(){
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
            c = db.rawQuery("SELECT " + DataBaseDB.NOMBRE + ", " +
                    DataBaseDB.TELEFONO + " FROM " + DataBaseDB.TB_CONTACTO, null);

            if (c.moveToFirst()) {
                do {
                    contacto.add(new Item(
                            c.getString(0),
                            c.getString(1),
                            true
                            )
                    );
                } while (c.moveToNext());

            } else {
                System.out.println("No existen registros!!!");
            }

        } catch (Exception ex) {
            Log.e(TAG, "getCorreoPreregistro: " + ex.toString());
        } finally {
            Utils.close(c);
            Utils.close(db);
            Utils.freeMemory();
        }
    }



    private void getContactList() {
        itemsContact.clear();
        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        itemsContact.add(new Item(name, phoneNo));
                        break;
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }


}
