package com.example.joaquin.tt_des_v_100.Ui.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Controllers.ContConfig;


public class FrgConfig extends Fragment {



    private Spinner spLongitudRadio;
    private Switch simpleSwitch;
    private Spinner spTiempoGuardian;
    private RelativeLayout RelSession;
    private ContConfig controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBinding(view);
        initObjects();
        initControls();
        listeners();
    }

    private void initBinding(View view) {
        spLongitudRadio = view.findViewById(R.id.spLongitudRadio);
        simpleSwitch = view.findViewById(R.id.simpleSwitch);
        spTiempoGuardian = view.findViewById(R.id.spTiempoGuardian);
        RelSession = view.findViewById(R.id.RelSession);
    }


    private void initObjects() {
        controller = new ContConfig(spLongitudRadio, simpleSwitch, spTiempoGuardian, RelSession, getActivity(), getContext());
    }

    private void initControls() {
        controller.setDataSpinners();
        controller.initSpinnerRadio();
        controller.initSpinnerGuardian();
        controller.setInitialConfig();
    }

    private void listeners() {

        spLongitudRadio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0)
                    controller.changeDistancia();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println(b);
                /*if (b) {
                    System.out.println(b);
                } else {
                    System.out.println(b);
                }*/
            }
        });


        spTiempoGuardian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        RelSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Hola");
            }
        });

    }
}
