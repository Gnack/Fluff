package com.ru.gacklash.fluff;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appodeal.ads.Appodeal;

public class PartnersActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }



        setContentView(R.layout.activity_web);

        Appodeal.hide(this, Appodeal.BANNER_BOTTOM);

        mWebView=(WebView) findViewById(R.id.mainView);
        //mWebView.setWebViewClient(new WebViewClient());
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //mainView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //"file:///android_asset/webFiles/index.html"
        mWebView.loadUrl("file:///android_asset/webFiles/partners.html");
        mWebView.setBackgroundColor(Color.TRANSPARENT);
    }

}
