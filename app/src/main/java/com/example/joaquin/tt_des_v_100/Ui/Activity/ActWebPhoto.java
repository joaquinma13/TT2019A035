package com.example.joaquin.tt_des_v_100.Ui.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.joaquin.tt_des_v_100.R;

public class ActWebPhoto extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_web_photo);

        String baseURL = getString(R.string.pref_default_base_url) + getIntent().getStringExtra("endPoint");

        System.out.println("URL: " +  baseURL);

        webview = findViewById(R.id.webview);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setSupportZoom(true);
        webview.loadUrl("http://192.168.100.6/servicesTT/Pictures/idlgmovhcj_16:06:34.jpg");
        webview.setWebViewClient(new WebViewClient());


        /*webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl("http://192.168.100.6/servicesTT/Pictures/ij5lwboqmp_15:33:43.jpg");
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                System.out.println("error");
            }

        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("Me voy jajajaja");
        finish();
    }
}
