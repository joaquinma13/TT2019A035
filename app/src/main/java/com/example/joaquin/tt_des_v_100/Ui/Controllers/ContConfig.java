package com.example.joaquin.tt_des_v_100.Ui.Controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Model.WsConfiguracion;
import com.example.joaquin.tt_des_v_100.R;
import com.example.joaquin.tt_des_v_100.Ui.Models.ModelConfig;

import java.util.ArrayList;

public class ContConfig {

    //CONTROLES
    private Spinner spLongitudRadio;
    private Switch simpleSwitch;
    private Spinner spTiempoGuardian;
    private RelativeLayout relSession;
    private Activity activity;
    private Context context;

    private ModelConfig modelConfig;
    private SharePreference preference;

    //COSAS
    private ArrayList<String> arrayDistancia;
    private ArrayList<String> arrayTiempo;

    public ContConfig(Spinner spLongitudRadio, Switch simpleSwitch, Spinner spTiempoGuardian,
                      RelativeLayout relSession, Activity activity, Context context) {
        this.spLongitudRadio = spLongitudRadio;
        this.simpleSwitch = simpleSwitch;
        this.spTiempoGuardian = spTiempoGuardian;
        this.relSession = relSession;
        this.activity = activity;
        this.context = context;
        modelConfig = new ModelConfig(context);
        preference = SharePreference.getInstance(context);

    }

    public void setDataSpinners(){
        arrayDistancia = new ArrayList<>();
        arrayDistancia.add("Seleccione");
        arrayDistancia.add("50 metros");
        arrayDistancia.add("100 metros");
        arrayDistancia.add("250 metros");
        arrayDistancia.add("500 metros");
        arrayDistancia.add("750 metros");
        arrayDistancia.add("1000 metros");

        arrayTiempo = new ArrayList<>();
        arrayTiempo.add("Seleccione");
        arrayTiempo.add("10 minutos");
        arrayTiempo.add("15 minutos");
        arrayTiempo.add("30 minutos");
        arrayTiempo.add("45 minutos");
        arrayTiempo.add("60 minutos");
    }

    public void initSpinnerRadio (){
        ContConfig.MyAdapter myAdapter = new ContConfig.MyAdapter(activity, arrayDistancia);
        spLongitudRadio.setAdapter(myAdapter);
    }

    public void initSpinnerGuardian(){
        ContConfig.MyAdapter myAdapter = new ContConfig.MyAdapter(activity, arrayTiempo);
        spTiempoGuardian.setAdapter(myAdapter);
    }

    public void setInitialConfig(){
        WsConfiguracion wsConfiguracion = modelConfig.getStartedConfig();
        if (wsConfiguracion.getId_user() != null){
            if(wsConfiguracion.getGuardian().equals("0"))
                simpleSwitch.setChecked(false);
            putSpinnerDistancia(wsConfiguracion);
            putSpinnerTiempo(wsConfiguracion);
        }
    }

    public void changeDistancia(){
        final CustomAlert alert = new CustomAlert(activity);

        alert.setTypeWarning(
                "ATENCIÓN",
                "¿Desea cambiar la distancia?",
                "Cancelar",
                "Aceptar");
        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.close();
                WsConfiguracion wsConfiguracion = modelConfig.getStartedConfig();
                if (wsConfiguracion.getId_user() != null){
                    putSpinnerDistancia(wsConfiguracion);
                }
            }
        });
        alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setTypeProgress("Espere...");
                boolean res = modelConfig.changeDistancia(preference.getStrData("id_user"), spLongitudRadio.getSelectedItem().toString(), getSwich(),
                        spTiempoGuardian.getSelectedItem().toString(),"0");
                if(res){
                    alert.close();
                }else{
                    alert.setTypeError("Error", "Imposible conectar con el Servidor", "Ok");
                    alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.close();
                        }
                    });
                }

            }
        });
        alert.show();
    }








    public void putSpinnerDistancia(WsConfiguracion wsConfiguracion){
        switch (wsConfiguracion.getDistancia()) {
            case "0":
                spLongitudRadio.setSelection(0);
                break;
            case "50":
                spLongitudRadio.setSelection(1);
                break;
            case "100":
                spLongitudRadio.setSelection(2);
                break;
            case "250":
                spLongitudRadio.setSelection(3);
                break;
            case "500":
                spLongitudRadio.setSelection(4);
                break;
            case "750":
                spLongitudRadio.setSelection(5);
                break;
            case "1000":
                spLongitudRadio.setSelection(6);
                break;
        }
    }

    public void putSpinnerTiempo(WsConfiguracion wsConfiguracion){
        switch (wsConfiguracion.getTiempo()) {
            case "0":
                spTiempoGuardian.setSelection(0);
                break;
            case "10":
                spTiempoGuardian.setSelection(1);
                break;
            case "15":
                spTiempoGuardian.setSelection(2);
                break;
            case "30":
                spTiempoGuardian.setSelection(3);
                break;
            case "45":
                spTiempoGuardian.setSelection(4);
                break;
            case "60":
                spTiempoGuardian.setSelection(5);
                break;
        }
    }

    public String getSwich(){
        if(simpleSwitch.isChecked())
            return "1";
        else
            return "0";
    }




    private class MyAdapter extends BaseAdapter {

        private ArrayList<String> myList;
        private Activity parentActivity;
        private LayoutInflater inflater;

        public MyAdapter(Activity parent, ArrayList<String> l) {
            parentActivity = parent;
            myList = l;
            inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.row, null);

            TextView text2 = view.findViewById(R.id.text2);
            text2.setText(myList.get(position));
            return view;
        }
    }
}
