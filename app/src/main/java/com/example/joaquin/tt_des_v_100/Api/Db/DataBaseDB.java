package com.example.joaquin.tt_des_v_100.Api.Db;

public class DataBaseDB {

    /* INFORMACION DE BASE DE DATOS*/
    public static final String DB_NAME = "db_inmobiliario";
    public static int VERSION = 1;


    /* TABLAS */
    public static final String TB_NAME_USUARIO = "tb_name_usuario";
    public static final String TB_CONTACTO = "tb_name_contacto";
    public static final String TB_ZONAS = "tb_name_zona";
    public static final String TB_EVENTO = "tb_evento";
    public static final String TB_CONFIGURACION = "tb_configuracion";


    // TB_NAME_USUARIO
    public static String USUARIO = "usuario";
    public static String CONTRASENA = "contrasena";
    public static String NOMBRE = "nombre";
    public static String CORREO_ELECTRONICO = "correo_electronico";
    public static String TELEFONO = "telefono";
    public static String ID_USER = "id_user";
    public static String ESTATUS = "estatus";

    // TB_NAME_CONTACTO
    public static String SENAL = "senal";
    public static String BATERIA = "bateria";
    public static String IMEI = "imei";
    public static String MODELO = "modelo";
    public static String FECHA = "fecha";
    public static String LATITUD = "latitud";
    public static String LONGITUD = "longitud";

    // TB_ZONAS
    public static String ID_ZONA = "id_zona";
    public static String RADIO = "radio";


    //TB_EVENTO
    public static String TIPO = "tipo";
    public static String DESCRIPCION = "descripcion";
    public static String RUTA = "ruta";
    public static String CADENA = "cadena";
    public static String ESTADO = "estado";

    //TB_CONFIGURACION
    public static String DISTANCIA = "distancia";
    public static String GUARDIAN = "guardian";
    public static String TIEMPO = "tiempo";
    public static String SESION = "sesion";


}
