package com.example.joaquin.tt_des_v_100.Ui.Adapter;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.joaquin.tt_des_v_100.Api.Class.Item;
import com.example.joaquin.tt_des_v_100.Api.Class.SharePreference;
import com.example.joaquin.tt_des_v_100.R;

import java.util.List;

public class AdpZonas  extends RecyclerView.Adapter<AdpZonas.ViewHolder> {

    private List listZonas;
    private SharePreference preference;
    private Activity act;

    public AdpZonas(Activity act, List listZonas) {
        this.listZonas = listZonas;
        preference = SharePreference.getInstance(act);
        this.act = act;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_zona, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Item zona = (Item) listZonas.get(position);

        holder.textCardZona.setText(zona.getNombre());
        holder.textCardLatitud.setText(String.valueOf(zona.getLatitud()));
        holder.textCardLongitud.setText(String.valueOf(zona.getLongitud()));
    }

    @Override
    public int getItemCount() {
        return listZonas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textCardZona;
        private TextView textCardLatitud;
        private TextView textCardLongitud;


        ViewHolder(View itemView) {
            super(itemView);
            textCardZona = itemView.findViewById(R.id.textCardZona);
            textCardLatitud = itemView.findViewById(R.id.textCardLatitud);
            textCardLongitud = itemView.findViewById(R.id.textCardLongitud);
        }
    }

}
