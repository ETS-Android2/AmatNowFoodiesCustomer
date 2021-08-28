package com.foodies.amatfoodies.activitiesAndFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.foodies.amatfoodies.R;

public class Dialogue {

    public void showDialog(Activity activity ) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_layout);



        Button sell = (Button) dialog.findViewById(R.id.sell);
        Button buy = (Button) dialog.findViewById(R.id.buy);
        Button dismiss = (Button) dialog.findViewById(R.id.dismiss);

        sell.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, WebViewSellActivity.class);
                activity.startActivity(i);
                //Perfome Action
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Perfome Action
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}