package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Model.WsEnviaEvento;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WsNotification {

    private static final String TAG = WsAut.class.getSimpleName();
    private Activity act;
    private APIInterface apiInterface;

    public WsNotification(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
    }


    public void getNotification(final String id_user, final String tipo, final String descripcion, final String latitud, final String longitud, final String imagen, final String fecha) {

        final Call<WsEnviaEvento> call = apiInterface.postNotificationUser(new WsEnviaEvento(id_user, tipo, descripcion, latitud, longitud, imagen,fecha));
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
        call.enqueue(new Callback<WsEnviaEvento>() {
            @Override
            public void onResponse(final Call<WsEnviaEvento> call, Response<WsEnviaEvento> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {

                    Log.d("JSONN: ", new Gson().toJson(response.body()));
                    WsEnviaEvento getNotfication = response.body();


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
                                getNotification(id_user, tipo, descripcion, latitud, longitud, imagen, fecha);
                            }
                        });
                    } else {
                        alert.setTypeReady("Envio de evento exitoso", "ok");
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
            public void onFailure(Call<WsEnviaEvento> call, Throwable t) {
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
                        getNotification(id_user, tipo, descripcion, latitud, longitud, imagen, fecha);
                    }
                });
            }
        });
    }





















}
