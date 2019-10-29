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


import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
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

        final Item cuenta = (Item) listCuentas.get(position);

        holder.textCardContacto.setText(cuenta.getNombre());
    }

    @Override
    public int getItemCount() {
        return listCuentas.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textCardContacto;


        ViewHolder(View itemView) {
            super(itemView);
            textCardContacto = itemView.findViewById(R.id.textCardContacto);
        }
    }

}
