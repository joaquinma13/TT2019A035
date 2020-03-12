package com.example.joaquin.tt_des_v_100.Api.Class;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

public class Permission {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private String[] globalPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
    };

    private Activity act;
    private Context context;

    public Permission(Activity act) {
        this.act = act;
    }

    public Permission(Context context) {
        this.context = context;
    }

    public boolean checkPermissions(boolean click_enter) {

        int countPermissions = 0;
        /*
          Hacer un recorrido por los permisos globales para revisar que permisos se han otorgado,
          la variable countPermissions se incrementa si no tenemos otorgado el permiso.
         */
        for (String globalPermission : globalPermissions) {
            if (ContextCompat.checkSelfPermission(act, globalPermission) != PackageManager.PERMISSION_GRANTED) {
                countPermissions++;
            }
        }

        /*
         * Arreglo de Strings del tamaño de permisos que no han sido otorgados.
         */
        String permissions[] = new String[countPermissions];
        countPermissions = 0;

        /*
         * Llenado del arreglo de Strings con los permisos que no fueron ®otorgados para
         * posteriormente pedirlos al usuario.
         */

        boolean uiPermission = true;
        for (String globalPermission : globalPermissions) {
            if (ContextCompat.checkSelfPermission(act, globalPermission) != PackageManager.PERMISSION_GRANTED) {
                permissions[countPermissions++] = globalPermission;
                uiPermission &= ActivityCompat.shouldShowRequestPermissionRationale(act, globalPermission);
            }
        }

        if (countPermissions > 0) {
            ActivityCompat.requestPermissions(act, permissions, 1);

            /*if (!uiPermission && click_enter) {
                final Snackbar snackBar = Snackbar.make(act.findViewById(android.R.id.content), "Faltan permisos", Snackbar.LENGTH_INDEFINITE);
                snackBar.setAction("Aceptar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", act.getPackageName(), null);
                        intent.setData(uri);
                        act.startActivity(intent);

                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            }*/

            return false;
        }
        return true;
    }

    public boolean isNotificationServiceEnabled() {
        System.out.println("isNotificationServiceEnabled");
        String pkgName = act.getPackageName();
        final String flat = Settings.Secure.getString(act.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkPermissionIMEI() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            final CustomAlert alert = new CustomAlert(context);
            alert.setTypeWarning(
                    "ATENCIÓN",
                    "Para IMEI se requiere permiso",
                    "Cancelar",
                    "Activar");
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
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            });
            alert.setCancelable(false);
            alert.show();
        } else {
            return true;
        }
        return false;
    }

    public boolean checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            final CustomAlert alert = new CustomAlert(act);
            alert.setTypeWarning("Atención", "Para poder hacer uso de la cámara es necesario permitir permiso",
                    "Cancelar", "Aceptar");

            alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.close();
                }
            });
            alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, 4);
                }
            });
            alert.show();
        } else {
            return true;
        }
        return false;
    }

    public boolean checkPermissionWrite() {
        if ((ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            final CustomAlert alert = new CustomAlert(act);
            alert.setTypeWarning("Atención", "Para poder guardar la foto se necesita permiso (La foto no quedará almacena en el dispositivo)",
                    "Cancelar", "Permitir");
            alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.close();
                }
            });
            alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
                }
            });
            alert.show();

        } else {
            return true;
        }
        return false;
    }

    public boolean checkPermissionCalendar() {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            final CustomAlert alert = new CustomAlert(act);
            alert.setTypeWarning(
                    "ATENCIÓN",
                    "Para agendar se requiere permiso",
                    "Cancelar",
                    "Activar");
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
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", act.getPackageName(), null);
                    intent.setData(uri);
                    act.startActivity(intent);
                }
            });
            alert.setCancelable(false);
            alert.show();
        } else {
            return true;
        }
        return false;
    }
}
