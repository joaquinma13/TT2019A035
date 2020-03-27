package com.example.joaquin.tt_des_v_100.Api.Service;
import com.example.joaquin.tt_des_v_100.Api.Model.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    /*========================================= GET ==============================================*/
    /*@GET("ZendereHWsMovil/api/usuario?")
    Call<GetLogin> getLogin(@Query("v_usuario") String v_usuario, @Query("v_contrasena") String v_contrasena);*/


    @POST("loginUsers.php")
    Call<Autenticar> postAutenticar(@Body Autenticar wsPass);

    @POST("registerUsers.php")
    Call<WsRecibeUsuario> postRecibeUsuario(@Body WsRecibeUsuario wsUser);

    @POST("recibeBitacora.php")
    Call<WsRecibeBitacora> postRecibeBitacora(@Body WsRecibeBitacora wsBitacora);

    @POST("vinculaUser.php")
    Call<WsRecibeUsuario> postVinculaUsuario(@Body WsRecibeUsuario wsVincula);

    @POST("solicitaUser.php")
    Call<WsRecibeBitacora> postSolicitarUsuario(@Body WsRecibeBitacora wsSolicita);


}
