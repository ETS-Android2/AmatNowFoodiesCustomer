package com.foodies.amatfoodies.activitiesAndFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.models.ModelDeliveryDetails;

public class WebViewSellActivity extends AppCompatActivity {
    WebView browser;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_sell);
        browser = findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        buildUrl();
    }

    private void buildUrl() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading....");
        pd.show();

        browser.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });
        browser.loadUrl("https://panel.amatnow.com/stores/");
    }
}