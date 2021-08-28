package com.foodies.amatfoodies.activitiesAndFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.activities.BaseActivity;

public class GetStartedActivity extends AppCompatActivity {

    //Web View for get started Screen
    WebView getStartedWebView;
    TextView skipTextView, loginTextView;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Load Gif inside WebView View for Splash Screen
        getStartedWebView = (WebView) findViewById(R.id.getStartedWebViewGif);

        loginTextView = findViewById(R.id.goLogin);
        signUpButton = findViewById(R.id.goSignUp);
        skipTextView = findViewById(R.id.goSkip);

        // WebView getStartedWebView = new WebView(this);
        getStartedWebView.loadUrl("file:///android_asset/getstarted.gif");
        //setContentView(getStartedWebView);
/*
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logomotionsmall);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
//                startNextActivity(); //Here you have to define what happens when the video finish playing. (You should put it on replay if the activity is still active)
            }
        });

        videoView.start();
        */

        // End Web View for get Started Screen

        //Button Clicks

      /*  skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            Intent nextScreen = new Intent(GetStartedActivity.this, BaseActivity.class);
            nextScreen.putExtra("search", "any");
            startActivity(nextScreen);
        }*/

        // Register the onClick listener with the implementation above

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something when the corky is clicked
                Intent mainScreen = new Intent(GetStartedActivity.this, BaseActivity.class);
                mainScreen.putExtra("search", "any");
                startActivity(mainScreen);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(GetStartedActivity.this, BaseActivity.class);
                nextScreen.putExtra("hasAccount", "account");
                startActivity(nextScreen);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(GetStartedActivity.this, BaseActivity.class);
                nextScreen.putExtra("hasAccount", "account");
                startActivity(nextScreen);
            }
        });


        // End Button Clicks
    }
}