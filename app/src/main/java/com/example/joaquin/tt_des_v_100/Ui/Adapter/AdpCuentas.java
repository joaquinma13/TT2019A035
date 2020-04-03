package com.example.joaquin.tt_des_v_100.Ui.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.Api.Class.Utils;
import com.example.joaquin.tt_des_v_100.Api.Service.WsVinculaUser;
import com.example.joaquin.tt_des_v_100.R;

import java.util.ArrayList;
import java.util.List;

public class AdpCuentas extends RecyclerView.Adapter<AdpCuentas.ViewHolder> {

    private List listCuentas;
    private SharePreference preference;
    private Activity act;

    public AdpCuentas(Activity act, List listaClientes) {
        this.listCuentas = listaClientes;
        preference = SharePreference.getInstance(act);
        this.act = act;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cuentas, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Item cuenta = (Item)listCuentas.get(position);
        holder.textCardContacto.setText(cuenta.getNombre());

        /*if(cuenta.getStatus().equals("nulo")){
            holder.cardStatus.setBackgroundColor(Color.rgb(165, 165, 165));
        }else if(cuenta.getStatus().equals("Activo")){
            holder.cardStatus.setBackgroundColor(Color.rgb(25, 170, 57));
        }else if(cuenta.getStatus().equals("Pendiente1")){
            holder.cardStatus.setBackgroundColor(Color.rgb(209, 199, 69));
        }else if(cuenta.getStatus().equals("Pendiente2")){
            holder.cardStatus.setBackgroundColor(Color.rgb(255, 117, 20));
        }*/


        /*if(cuenta.getStatus().equals("nulo")){
            holder.cardStatus.setBackgroundColor(Color.rgb(165, 165, 165));
            //holder.imgAction.setTag(R.drawable.ic_camera);
        }else if(cuenta.getStatus().equals("Activo")){
            holder.cardStatus.setBackgroundColor(Color.rgb(25, 170, 57));
            //holder.imgAction.setTag(R.drawable.ic_remove_circle_outline_black_24dp);
            holder.imgAction.setImageResource(R.drawable.ic_remove_circle_outline_black_24dp);
        }else if(cuenta.getStatus().equals("Pendiente1")){
            holder.cardStatus.setBackgroundColor(Color.rgb(209, 199, 69));
            //holder.imgAction.setTag(R.drawable.ic_not_interested_black_24dp);
            holder.imgAction.setImageResource(R.drawable.ic_not_interested_black_24dp);
        }else if(cuenta.getStatus().equals("Pendiente2")){
            holder.cardStatus.setBackgroundColor(Color.rgb(255, 117, 20));
            //holder.imgAction.setTag(R.drawable.ic_remove_circle_outline_black_24dp);
            holder.imgAction.setImageResource(R.drawable.ic_remove_circle_outline_black_24dp);
        }*/

        /*holder.imgAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("nombre: " + Utils.itemsContact.get(holder.getAdapterPosition()).getNombre() + " numero: " + Utils.itemsContact.get(holder.getAdapterPosition()).getTelefono() + " status: " + cuenta.getStatus());
                String ban = null;
                String str = null;
                if(cuenta.getStatus().equals("nulo")){
                    ban = "1";
                    str = "¿Desea enviar una solicitud al contacto?";
                }else if(cuenta.getStatus().equals("Activo")){
                    ban = "3";
                    str = "¿Desea desvincular al contacto?";
                }else if(cuenta.getStatus().equals("Pendiente1")){
                    ban = "2";
                    str = "¿Desea aceptar la solicitud del contacto?";
                }else if(cuenta.getStatus().equals("Pendiente2")){
                    ban = "3";
                    str = "¿Desea rechazar la solicitud contacto?";
                }
                final String bandera = ban;
                final String string = str;
                final CustomAlert alert = new CustomAlert(act);
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
                        new WsVinculaUser(act, "master").setVinculo(preference.getStrData("id_user"), Utils.itemsContact.get(holder.getAdapterPosition()).getTelefono(),bandera, holder.imgAction, holder.cardStatus);
                    }
                });
                alert.show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listCuentas.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textCardContacto;
        Item Cuenta;


        ViewHolder(View itemView) {
            super(itemView);
            textCardContacto = itemView.findViewById(R.id.textCardContacto);

        }

    }

}
