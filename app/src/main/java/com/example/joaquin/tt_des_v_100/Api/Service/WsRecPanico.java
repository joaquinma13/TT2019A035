package com.example.joaquin.tt_des_v_100.Api.Service;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.Api.Model.WsPanico;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WsRecPanico {

    private static final String TAG = WsRecPanico.class.getSimpleName();
    private Activity act;
    private APIInterface apiInterface;

    public WsRecPanico(Activity act, String endpoint) {
        this.act = act;
        apiInterface = APIUtils.getUtils(act, endpoint).create(APIInterface.class);
    }


    public void setPanico(final String id_user) {

        final Call<WsPanico> call = apiInterface.postPanico(new WsPanico(id_user));
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
        call.enqueue(new Callback<WsPanico>() {
            @Override
            public void onResponse(final Call<WsPanico> call, Response<WsPanico> response) {
                Log.d(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()) {

                    Log.d("JSONN: ", new Gson().toJson(response.body()));
                    WsPanico getNotfication = response.body();


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
                                setPanico(id_user);
                            }
                        });
                    } else {
                        alert.setTypeReady("Envio exitoso", "OK");
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
            public void onFailure(Call<WsPanico> call, Throwable t) {
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
                        setPanico(id_user);
                    }
                });
            }
        });
    }

}
