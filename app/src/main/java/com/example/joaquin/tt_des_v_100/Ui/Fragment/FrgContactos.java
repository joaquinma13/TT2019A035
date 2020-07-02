package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.content.ContentValues.TAG;

public class FrgContactos extends Fragment {

    public static RecyclerView recyclerContact;
    public static AdpCuentas recyclerAdapter;
    public static int error = 0;
    private SharePreference preference;

    //BottomSheet
    public static BottomSheetBehavior sheetBehavior;
    public static RelativeLayout bottom_sheet;
    private TextView txtNombreContact;
    private TextView labelNumero;
    private TextView labelEstatus;
    private Button btnContact;
    private int index;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uno, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObjects();
        initBinding(view);
        listener();
    }

    private void listener() {

        recyclerContact.addOnItemTouchListener(new Recycler(getActivity(), recyclerContact, new Recycler.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                index = position;
                txtNombreContact.setText(Utils.itemsContact.get(position).getNombre());
                labelNumero.setText(Utils.itemsContact.get(position).getTelefono());
                System.out.println("Estatus: " + Utils.itemsContact.get(position).getStatus());
                if(Utils.itemsContact.get(position).getStatus().equals("nulo")){
                    labelEstatus.setText("Sin vinculo");
                    btnContact.setText("Mandar Solicitud");
                }else if(Utils.itemsContact.get(position).getStatus().equals("Activo")){
                    labelEstatus.setText("Vinculados");
                    btnContact.setText("Desvincular");
                }else if(Utils.itemsContact.get(position).getStatus().equals("Pendiente1")){
                    labelEstatus.setText("Confirmacion pendiente del otro usuario");
                    btnContact.setText("Cancelar Solicitud");
                }else if(Utils.itemsContact.get(position).getStatus().equals("Pendiente2")){
                    labelEstatus.setText("Pendiente por confirmar");
                    btnContact.setText("Confirmar Solicitud");
                }
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                String ban = null;
                String str = null;
                if(Utils.itemsContact.get(index).getStatus().equals("nulo")){
                    ban = "1";
                    str = "¿Desea enviar una solicitud al contacto?";
                }else if(Utils.itemsContact.get(index).getStatus().equals("Activo")){
                    ban = "3";
                    str = "¿Desea desvincular al contacto?";
                }else if(Utils.itemsContact.get(index).getStatus().equals("Pendiente1")){
                    ban = "3";
                    str = "¿Desea cancelar la solicitud?";
                }else if(Utils.itemsContact.get(index).getStatus().equals("Pendiente2")){
                    ban = "2";
                    str = "¿Desea aceptar la solicitud?";
                }
                final String bandera = ban;
                final String string = str;
                final CustomAlert alert = new CustomAlert(getActivity());
                alert.setTypeWarning(
                        "ATENCIÓN",
                        string,
                        "Cancelar",
                        "Aceptar");
                alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.close();
                    }
                });
                alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.close();
                        new WsVinculaUser(getActivity(), "master").setVinculo(preference.getStrData("id_user"), Utils.itemsContact.get(index).getTelefono(),bandera);
                    }
                });
                alert.show();
            }
        });
    }

    private void initObjects() {
        preference = SharePreference.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContact();
            }
        }).start();
    }

    private void initBinding(View view) {

        bottom_sheet = view.findViewById(R.id.bottom_sheet_contact);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        txtNombreContact = view.findViewById(R.id.txtNombreContact);
        labelNumero = view.findViewById(R.id.labelNumero);
        labelEstatus = view.findViewById(R.id.labelEstatus);
        btnContact = view.findViewById(R.id.btnContact);
        recyclerContact = view.findViewById(R.id.recyclerContact);
        recyclerContact.setHasFixedSize(true);
        recyclerContact.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new AdpCuentas(getActivity(), Utils.itemsContact);
        recyclerContact.setAdapter(recyclerAdapter);

    }


    public void getContact(){
        Utils.itemsContact.clear();
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = getContext().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
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
            Utils.close(db);
            Utils.freeMemory();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("FrgContactos");
    }
}
