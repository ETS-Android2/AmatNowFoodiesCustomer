package com.foodies.amatfoodies.activitiesAndFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.activities.BaseActivity;
import com.foodies.amatfoodies.adapters.RestSpecialityAdapter;
import com.foodies.amatfoodies.constants.AllConstants;
import com.foodies.amatfoodies.constants.ApiRequest;
import com.foodies.amatfoodies.constants.Callback;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.googleMapWork.MapsActivity;
import com.foodies.amatfoodies.models.SpecialityModel;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import org.firezenk.bubbleemitter.BubbleEmitterView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String lat, lon;
    CamomileSpinner progressBar;
    RecyclerView recyclerView;
    RestSpecialityAdapter recyclerViewadapter;
    ArrayList<SpecialityModel> specialityArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        sharedPreferences = getSharedPreferences(PreferenceClass.user, MODE_PRIVATE);

        lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");

        if (lat.isEmpty() || lon.isEmpty()) {
            lat = "31.4904023";
            lon = "74.2906989";
        }

        String getCurrentLocationAddress = sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, "");
        TextView location = findViewById(R.id.current_location);
        LinearLayout anything_screen = findViewById(R.id.anything);
        LinearLayout delivery_screen = findViewById(R.id.delivery);
        BubbleEmitterView bubbleEmitterView = findViewById(R.id.bubbemitterview);
        Button sell_or_buy = findViewById(R.id.sellORbuy);
        ImageView view6 = findViewById(R.id.imageView6);
        ImageView view7 = findViewById(R.id.imageView7);
        ImageView view8 = findViewById(R.id.imageView8);
        AutoCompleteTextView searchRl = findViewById(R.id.search_div);

        progressBar = findViewById(R.id.progressFetching);
        recyclerView = findViewById(R.id.specialtiesRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        specialityArray = new ArrayList();

        if (checkLogInSession()) {
            sell_or_buy.setText("My Account");
        } else {
            sell_or_buy.setText("Login");
        }

        searchRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(HomeScreen.this, MapsActivity.class));
                Intent nextScreen = new Intent(HomeScreen.this, BaseActivity.class);
                nextScreen.putExtra("hasRestaurant", "hasRestaurant");
                startActivity(nextScreen);
            }
        });

        delivery_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(HomeScreen.this, DeliveryActivity.class);
                startActivity(nextScreen);
            }
        });

        sell_or_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(HomeScreen.this, BaseActivity.class);
                nextScreen.putExtra("hasAccount", "account");
                startActivity(nextScreen);
            }
        });

        location.setText(getCurrentLocationAddress.toString());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });

        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });

        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceClass.getSharedPreference(getApplicationContext()).edit().putString(PreferenceClass.SEARCH, null).commit();
                startActivity(new Intent(getApplicationContext(), BaseActivity.class));
            }
        });

        view8.setOnClickListener(view -> {
            Intent nextScreen = new Intent(HomeScreen.this, BaseActivity.class);
            nextScreen.putExtra("hasAccount", "account");
            startActivity(nextScreen);
        });

        /*//if the text on the search bar is clicked
        locationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(HomeScreen.this, MapsActivity.class);
                nextScreen.putExtra("search", "search");
                startActivity(nextScreen);

            }
        });*/

        anything_screen.setOnClickListener(view -> {
            Intent nextScreen = new Intent(HomeScreen.this, BaseActivity.class);
            nextScreen.putExtra("search", "any");
            startActivity(nextScreen);
        });


        emitbubble(bubbleEmitterView);

        getRestSpecialityList();

    }

    public void emitbubble(BubbleEmitterView bubbleEmitterView) {
        new Handler().postDelayed(new Runnable() {
            public void run() {

                int size = (int) ((Math.random() * (200 - 100)) + 100);
                long time = (long) ((Math.random() * (700 - 1200)) + 100);

                bubbleEmitterView.setColorResources(R.color.badge_color, R.color.accent, R.color.ampm_text_color);
                bubbleEmitterView.setColors(getResources().getColor(R.color.default_circle_indicator_stroke_color), getResources().getColor(R.color.badge_color), getResources().getColor(R.color.bpBlue));
                bubbleEmitterView.emitBubble(size);
                emitbubble(bubbleEmitterView);

            }
        }, 700);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private boolean checkLogInSession() {
        return sharedPreferences.getBoolean(PreferenceClass.IS_LOGIN, false);
    }

    public void getRestSpecialityList() {
        progressBar.start();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("long", lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(AllConstants.tag, lat + " " + lon);


        ApiRequest.callApi(this, Config.SHOW_REST_SPECIALITY_LIST, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                progressBar.stop();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonResponse = new JSONObject(resp);

                    Log.d("JSONPost", jsonResponse.toString());

                    int code_id = Integer.parseInt(jsonResponse.optString("code"));

                    if (code_id == 200) {

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject jsonObject = jsonarray.getJSONObject(i);
                            JSONObject resJsonObject1 = jsonObject.getJSONObject("Restaurant");

                            SpecialityModel specialityModel = new SpecialityModel();

                            specialityModel.setName(resJsonObject1.optString("speciality"));
                            specialityModel.setId(resJsonObject1.optString("id"));

                            specialityArray.add(specialityModel);
                        }

                        if (specialityArray != null && !specialityArray.isEmpty()) {
                            recyclerViewadapter = new RestSpecialityAdapter(specialityArray, HomeScreen.this);
                            recyclerView.setAdapter(recyclerViewadapter);
                            recyclerViewadapter.notifyDataSetChanged();
                        }

                        recyclerViewadapter.setOnItemClickListner((view, position) -> {
                            Intent nextScreen = new Intent(HomeScreen.this, BaseActivity.class);
                            nextScreen.putExtra("search", specialityArray.get(position).getName());
                            startActivity(nextScreen);
                        });

                    }


                } catch (JSONException e) {
                    e.getMessage();

                }


            }
        });


    }
}

