package com.example.joaquin.tt_des_v_100.Ui.Fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActCroquis;
import com.example.joaquin.tt_des_v_100.Ui.Activity.ActLogin;
import com.example.joaquin.tt_des_v_100.Ui.Activity.Home_TT;
import com.example.joaquin.tt_des_v_100.Ui.Adapter.AdpZonas;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Fragment_dos extends Fragment {

    private FloatingActionButton btnGetZona;
    private RecyclerView recyclerZona;
    private AdpZonas recyclerAdapter;
    private ArrayList<Item> zona = new ArrayList<>();
    public static ArrayList<Item> itemsZona = new ArrayList<>();
    public static ArrayList<String> selectedZona = new ArrayList<>();


    public Fragment_dos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnGetZona = getActivity().findViewById(R.id.btnGetZona);
        recyclerZona = getActivity().findViewById(R.id.recyclerZona);

        btnGetZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ActCroquis.class);
                startActivity(intent);

            }
        });


    }

}
