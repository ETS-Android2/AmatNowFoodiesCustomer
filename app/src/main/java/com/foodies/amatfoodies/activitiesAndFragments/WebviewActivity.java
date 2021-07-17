package com.foodies.amatfoodies.activitiesAndFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.foodies.amatfoodies.R;

public class WebviewActivity extends AppCompatActivity {

    WebView browser;
    ProgressDialog pd;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        browser = findViewById(R.id.myWebview);
        browser.getSettings().setJavaScriptEnabled(true);

        try{
         String Url = getIntent().getExtras().getString("URL");
            buildUrl(Url);
        }
        catch (Exception e){

        }


    }

    private void buildUrl(String Url) {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading....");
        pd.show();

        browser.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });
        browser.loadUrl(Url);
    }
}