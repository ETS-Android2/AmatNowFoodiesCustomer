package com.foodies.amatfoodies;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.foodies.amatfoodies.activities.BaseActivity;

public class JavaInterface {

    private Activity activity;

    public JavaInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void gotoHomePage() {

        activity.startActivity(new Intent(activity, BaseActivity.class));
    }
}
