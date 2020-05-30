package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.joaquin.tt_des_v_100.Api.Class.CustomAlert;
import com.example.joaquin.tt_des_v_100.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class ActEditorBitmap extends AppCompatActivity {

    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private String tipo_vista = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editor_bitmap);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getIntent().getExtras() != null) {
            tipo_vista = getIntent().getExtras().getString("vista");
        }

        //intent.putExtra("vista", "ActCam");

        findViewById(R.id.colorActual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                colorPicker("Selecciona un color", Color.parseColor("#FFFFFF"), 122, 122);
            }
        });


        findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.undo();
            }
        });

        findViewById(R.id.redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoEditor.redo();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipo_vista.equals("ActCam"))
                    ActCam.cropImageView.setImageBitmap(null);
                else
                    ActCam.cropImageView.setImageBitmap(null);


                mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
                    @Override
                    public void onBitmapReady(Bitmap saveBitmap) {
                        if (tipo_vista.equals("ActCam"))
                            ActCam.cropImageView.setImageBitmap(saveBitmap);
                        else
                            ActCam.cropImageView.setImageBitmap(saveBitmap);

                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });

        mPhotoEditorView = findViewById(R.id.photoEditorView);


        if (tipo_vista.equals("ActCam"))
            mPhotoEditorView.getSource().setImageBitmap(ActCam.cropImageView.getCroppedImage());
        else
            mPhotoEditorView.getSource().setImageBitmap(ActCam.cropImageView.getCroppedImage());

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView).build();

        mPhotoEditor.setBrushDrawingMode(true);
    }

    private void colorPicker(String title, int color, final int idColor, final int idFrame) {
        final CustomAlert alert = new CustomAlert(this);
        alert.setTypeView(
                ContextCompat.getDrawable(this, R.drawable.palette),
                title,
                "CANCELAR",
                "OK"
        );

        View view = getLayoutInflater().inflate(R.layout.alert_color, null);

        final ColorPicker picker = view.findViewById(R.id.picker);
        SVBar svBar = view.findViewById(R.id.svbar);

        picker.addSVBar(svBar);
        picker.setColor(color);
        picker.getColor();
        picker.setOldCenterColor(picker.getColor());
        picker.setShowOldCenterColor(false);

        alert.getBtnLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.close();
            }
        });

        alert.getBtnRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Color select", "Color: " + picker.getColor());
                String hexColor = String.format("#%06X", (0xFFFFFF & picker.getColor()));
                Log.i("Color select", "hexColor: " + hexColor);


                mPhotoEditor.setBrushColor(Color.parseColor(hexColor));

                Drawable background = findViewById(R.id.colorActual).getBackground();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(hexColor));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(Color.parseColor(hexColor));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(Color.parseColor(hexColor));
                }
                alert.close();
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        alert.addView(view);
        alert.ajustar(height);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

