package com.example.joaquin.tt_des_v_100.Api.Db;

public class DataBaseDB {

    /* INFORMACION DE BASE DE DATOS*/
    public static final String DB_NAME = "db_inmobiliario";
    public static int VERSION = 1;


    /* TABLAS */
    public static final String TB_NAME_USUARIO = "tb_name_usuario";
    public static final String TB_CONTACTO = "tb_name_contacto";
    public static final String TB_NAME_CAT_0 = "tb_name_cat_0"; // Pregunta


    // TB_NAME_USUARIO
    public static String USUARIO = "usuario";
    public static String CONTRASENA = "contrasena";
    public static String NOMBRE = "nombre";
    public static String PERFIL = "perfil";
    public static String NOMBRE_PERFIL = "nombre_perfil";
    public static String SUPERVISOR = "supervisor";
    public static String FECHA_ALTA = "fecha_alta";
    public static String ESTATUS_USUARIO = "estatus_usuario";
    public static String CORREO_ELECTRONICO = "correo_electronico";
    public static String TELEFONO = "telefono";

    // TB_NAME_CAT_0 (Preguntas)
    public static String CAT_PREG_ID = "cat_preg_id";
    public static String CAT_PREG_CONSECUTIVO = "cat_preg_consecutivo";
    public static String CAT_PREG_DESCRIPCION = "cat_preg_descripcion";
    public static String CAT_PREG_BLOQUE = "cat_preg_bloque";
    public static String CAT_PREG_SUBLOQUE = "cat_preg_subloque";

    public static String CAT_PREG_SUBLOQUE_NAME = "cat_preg_subloque_name";
    public static String CAT_PREG_BLOQUE_NAME = "cat_preg_bloque_name";
    public static String CAT_PREG_PROCESO_NAME = "cat_preg_proceso_name";

    public static String CAT_PREG_HEADER = "cat_preg_header";
    public static String CAT_PREG_AGENDABLE = "cat_preg_agendable";
    public static String CAT_PREG_CAPTURA = "cat_preg_captura";
    public static String CAT_PREG_EDICION = "cat_preg_edicion";
    public static String CAT_PREG_LECTURA = "cat_preg_lectura";
    public static String CAT_PREG_ESTATUS = "cat_preg_estatus";
    public static String CAT_PREG_PROCESO = "cat_preg_proceso";
    public static String CAT_PREG_TIPORESPUESTA = "cat_preg_tiporespuesta";
    public static String CAT_PREG_TIPODATO = "cat_preg_tipodato";
    public static String CAT_PREG_LONGITUD = "cat_preg_longitud";
    public static String CAT_PREG_OBLIGATORIA = "cat_preg_obligatoria";
    public static String CAT_PREG_DOCS_MAX = "cat_preg_docs_max";
    public static String CAT_PREG_DOCS_MIN = "cat_preg_docs_min";
    public static String CAT_PREG_COLOR = "cat_preg_color";

}
