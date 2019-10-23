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
import android.widget.TextView;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Db.DataBaseDB;
import com.example.joaquin.tt_des_v_100.Api.Listener.Recycler;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_uno#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_uno extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private Button btnGetContact;
    private FloatingActionButton btnGetContact;
    private RecyclerView recyclerContact;
    private AdpCuentas recyclerAdapter;
    private ArrayList<Item> contacto = new ArrayList<>();
    public static ArrayList<Item> itemsContact = new ArrayList<>();
    public static ArrayList<String> selectedContact = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Fragment_uno() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_uno.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_uno newInstance(String param1, String param2) {
        Fragment_uno fragment = new Fragment_uno();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContact();
                getContactList();
            }
        }).start();
        btnGetContact = getActivity().findViewById(R.id.btnGetContact);
        recyclerContact = getActivity().findViewById(R.id.recyclerContact);

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
                //TextView tvCuant;
                View child;
                for (int i = 0; i < itemsContact.size(); i++) {
                    child = getLayoutInflater().inflate(R.layout.checbox_group_route, null);

                    cb = child.findViewById(R.id.checkBox);
                    //tvCuant = child.findViewById(R.id.Cuantos);

                    cb.setText(itemsContact.get(i).getNombre());
                    //tvCuant.setText(String.valueOf(itemsEs.get(i).getiEMC()));
                    //cb.setChecked(selectedEs.contains(cb.getText().toString()));

                    cb.setId(i);

                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                System.out.println("index: " + compoundButton.getId());
                                selectedContact.add(compoundButton.getText().toString());
                            } else {
                                for (int i = 0; i < selectedContact.size(); i++) {
                                    if (selectedContact.get(i).equals((compoundButton.getText().toString()))) {
                                        selectedContact.remove(i);
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


                        SQLiteDatabase db = null;
                        ContentValues values = new ContentValues();
                        Cursor c = null;

                        try {
                            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);

                            for (int i = 0; i < selectedContact.size(); i++) {

                                System.out.println("Usuario: " + selectedContact.get(i));
                                values.put(DataBaseDB.USUARIO, selectedContact.get(i));
                                values.put(DataBaseDB.TELEFONO, "11111111");
                                db.insert(DataBaseDB.TB_CONTACTO, null, values);
                                values.clear();
                            }

                            contacto.clear();
                            System.out.println("Tam: " + contacto.size());
                            c = db.rawQuery("SELECT " + DataBaseDB.USUARIO + ", " +
                                    DataBaseDB.TELEFONO + " FROM " + DataBaseDB.TB_CONTACTO, null);

                            if (c.moveToFirst()) {
                                do {

                                    System.out.println(c.getString(0));
                                    contacto.add(new Item(
                                                    c.getString(0),
                                                    c.getString(1)
                                            )
                                    );
                                } while (c.moveToNext());

                            } else {
                                System.out.println("No existen registros!!!");
                            }

                            recyclerAdapter = new AdpCuentas(getActivity(), contacto);
                            recyclerContact.setAdapter(recyclerAdapter);



                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (db != null) {
                                db.close();
                                db = null;
                            }
                            Utils.freeMemory();
                        }

                        alerta.close();
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
            values.put(DataBaseDB.USUARIO, "Luis");
            values.put(DataBaseDB.TELEFONO, "11111111");
            db.insert(DataBaseDB.TB_CONTACTO, null, values);

            values.clear();

            values.put(DataBaseDB.USUARIO, "Juan");
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
            c = db.rawQuery("SELECT " + DataBaseDB.USUARIO + ", " +
                    DataBaseDB.TELEFONO + " FROM " + DataBaseDB.TB_CONTACTO, null);

            if (c.moveToFirst()) {
                do {
                    contacto.add(new Item(
                            c.getString(0),
                            c.getString(1)
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
