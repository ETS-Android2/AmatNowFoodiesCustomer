package com.foodies.amatfoodies.activitiesAndFragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.constants.FragmentCallback;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Checkout_F extends RootFragment {

    View view;
    Context context;

    ProgressBar progressBar;
    WebView webView;
    String url="www.google.com";
    public Checkout_F() {

    }

    FragmentCallback fragment_callback;
    public Checkout_F(FragmentCallback fragment_callback) {
        this.fragment_callback=fragment_callback;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_checkout, container, false);
        context=getContext();

        Bundle bundle=getArguments();
        if(bundle!=null){
            url=bundle.getString("url");
        }


        view.findViewById(R.id.back_icon).setOnClickListener(v -> getActivity().onBackPressed());


        webView=view.findViewById(R.id.webview);
        progressBar =view.findViewById(R.id.progress_bar);
        webView.getSettings().setJavaScriptEnabled(true);
      //  JavaInterface jsInterface = new JavaInterface(getActivity());
        webView.addJavascriptInterface(this, "OpenNewScreen");

      //  webView.setInitialScale(1);
      //  webView.getSettings().setLoadWithOverviewMode(true);
      //  webView.getSettings().setUseWideViewPort(true);
      //  webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
      //  webView.setScrollbarFadingEnabled(false);

        webView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {
                if(progress>=80){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                if(url.contains("paymentSuccess")){
                    startTimer();
                    System.out.println("payment success");
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });


        return view;
    }


    Handler max_handler;
    Runnable max_runable;
    public void startTimer(){
        max_handler=new Handler();
        max_runable=new Runnable() {
            @Override
            public void run() {

                getActivity().onBackPressed();

            }
        };
        max_handler.postDelayed(max_runable,10000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(max_handler!=null && max_runable!=null){

            if(fragment_callback!=null)
                fragment_callback.onResponce(new Bundle());

            max_handler.removeCallbacks(max_runable);
        }

    }

    @JavascriptInterface
    public void gotoHomePage(){
        startActivity(new Intent(getContext(), MainActivity.class));
    }

}
