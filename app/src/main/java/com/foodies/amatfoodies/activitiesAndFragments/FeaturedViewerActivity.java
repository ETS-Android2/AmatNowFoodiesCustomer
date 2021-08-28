package com.foodies.amatfoodies.activitiesAndFragments;

import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.models.AppImageSlider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class FeaturedViewerActivity extends AppCompatActivity {

    private AppImageSlider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_featured_viewer);

        TextView scrollingText = findViewById(R.id.scrollingText);
        ImageView bgImage = findViewById(R.id.bgImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ActionBar bar = getSupportActionBar();
            bar.setTitle("");
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.theme3_ic_arrow_back);
            bar.setDisplayShowHomeEnabled(true);
        }

        if (getIntent().getParcelableExtra("data") != null)
            slider = getIntent().getParcelableExtra("data");

        if (slider != null) {

            Glide.with(this).asBitmap().thumbnail(0.03f).load(Config.imgBaseURL + slider.getImage())
                    .placeholder(R.drawable.image_placeholder).into(bgImage);


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                scrollingText.setText(Html.fromHtml(slider.getBody()));
            else
                scrollingText.setText(Html.fromHtml(slider.getBody(), Html.FROM_HTML_MODE_LEGACY));


        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}