package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Model.WsEnviaReporte;
import com.example.joaquin.tt_des_v_100.Api.Model.WsEnviaReporte;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WsRecReporte {

    private static final String TAG = WsRecReporte.class.getSimpleName();
    private Activity act;
    private APIInterface apiInterface;

    public WsRecReporte(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
    }

    public void setReporte(final String id_user_manda, final String id_user_recive, final String id_evento, final String descripcion) {
        
        /*
        private String id_user_manda;
    private String id_user_recive;
    private String id_evento;
    private String descripcion;
         */

        final Call<WsEnviaReporte> call = apiInterface.postReportes(new WsEnviaReporte(id_user_manda, id_user_recive, id_evento, descripcion));
        final CustomAlert alert = new CustomAlert(act);
        alert.setTypeProgress("Enviando...", "", "Cancelar");
        alert.setCancelable(false);
        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.cancel();
                alert.close();
            }
        });
        alert.show();
        call.enqueue(new Callback<WsEnviaReporte>() {
            @Override
            public void onResponse(final Call<WsEnviaReporte> call, Response<WsEnviaReporte> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {

                    Log.d("JSONN: ", new Gson().toJson(response.body()));
                    WsEnviaReporte getNotfication = response.body();


                    if (!getNotfication.Estatus.toUpperCase(Locale.ENGLISH).equals("OK")) {

                        alert.setTypeError("Error", getNotfication.Estatus, "Cancelar", "Volver a intentar");
                        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                call.cancel();
                                alert.close();
                            }
                        });
                        alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.close();
                                setReporte(id_user_manda, id_user_recive, id_evento, descripcion);
                            }
                        });
                    } else {
                        alert.setTypeReady("Envio de Reporte exitoso", "OK");
                        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.close();
                            }
                        });

                    }


                }
            }
            @Override
            public void onFailure(Call<WsEnviaReporte> call, Throwable t) {
                call.cancel();
                System.out.println("ERROR: " + t.toString());
                alert.setTypeError("ON FAILURE", t.toString(), "Cancelar", "Volver a intentar");
                alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.close();
                    }
                });
                alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.close();
                        setReporte(id_user_manda, id_user_recive, id_evento, descripcion);
                    }
                });
            }
        });
    }
}
