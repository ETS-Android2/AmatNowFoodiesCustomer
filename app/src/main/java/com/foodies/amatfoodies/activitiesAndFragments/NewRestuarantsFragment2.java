package com.foodies.amatfoodies.activitiesAndFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.adapters.AdapterAllRestaurantData;
import com.foodies.amatfoodies.adapters.AdapterAllRestaurantData2;
import com.foodies.amatfoodies.adapters.AdapterPromoted;
import com.foodies.amatfoodies.adapters.AppImageSliderAdapter;
import com.foodies.amatfoodies.adapters.CountryListAdapter;
import com.foodies.amatfoodies.adapters.Offer_adapter;
import com.foodies.amatfoodies.adapters.RestSpecialityAdapter;
import com.foodies.amatfoodies.constants.AllConstants;
import com.foodies.amatfoodies.constants.ApiRequest;
import com.foodies.amatfoodies.constants.Callback;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.constants.DataParser;
import com.foodies.amatfoodies.constants.FragmentCallback;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.databinding.LayoutRestaurantFragmentBinding;
import com.foodies.amatfoodies.googleMapWork.MapsActivity;
import com.foodies.amatfoodies.models.AppImageSlider;
import com.foodies.amatfoodies.models.AppImageSliderResponse;
import com.foodies.amatfoodies.models.RestaurantsModel;
import com.foodies.amatfoodies.models.SpecialityModel;
import com.foodies.amatfoodies.models.SpecialtyRestuarantsModel;
import com.foodies.amatfoodies.utils.TabLayoutUtils;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import view.GetTargetView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class NewRestuarantsFragment2 extends RootFragment implements GetTargetView {

    LayoutRestaurantFragmentBinding binding;
    String currentLoc;
    SharedPreferences sharedPreferences;
    String lat, lon, userId;
    Handler handler;
    int page = 0;
    int delay = 4000;
    String deviceToken;
    Offer_adapter offer_adapter;
    String search, hasRestaurant;
    public static String specialtyFIlter;
    private ArrayList<RestaurantsModel> promotedDataList;
    AdapterAllRestaurantData2 adapterAllRestaurantData;
    ArrayList<String> specialties, specialtyList;
    ArrayList<SpecialtyRestuarantsModel> specialtyRestuarantsModels;
    AdapterPromoted adapterPromoted;

    public NewRestuarantsFragment2() {
        // Required empty public constructor
    }

    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.layout_restaurant_fragment, container, false);

        TabLayoutUtils.enableTabs(PagerMainActivity.tabLayout, true);

        sharedPreferences = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);

        currentLoc = sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, "");
        lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");
        userId = sharedPreferences.getString(PreferenceClass.pre_user_id, "");

        promotedDataList = new ArrayList<>();
        specialties = new ArrayList<>();
        specialtyList = new ArrayList<>();
        specialtyRestuarantsModels = new ArrayList<>();

        adapterPromoted = new AdapterPromoted(NewRestuarantsFragment2.this, getContext(), promotedDataList);
        binding.promotedRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.promotedRv.setAdapter(adapterPromoted);

        if (lat.isEmpty() || lon.isEmpty()) {
            lat = "31.4904023";
            lon = "74.2906989";
        }

        if (getArguments() != null && getArguments().getString("hasRestaurant") != null) {
            hasRestaurant = getArguments().getString("hasRestaurant");
            Fragment restaurantMenuItemsFragment = new RestaurantSpecialityFrag(new FragmentCallback() {
                @Override
                public void onResponce(Bundle bundle) {

                    if (bundle != null) {
                        specialtyFIlter = bundle.getString("speciality");
                        if (specialtyFIlter.equals("Anything")) {
                            getrestaurantlist("any");
                            getRestSpecialityList();
                        } else {
                            specialtyList.clear();
                            specialtyList.add(specialtyFIlter);
                            getrestaurantlist(specialtyFIlter);
                            getRestaurantListFilter(specialtyList);
                        }
                    }
                }

            });

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.newsFragment, restaurantMenuItemsFragment, "ParentFragment").commit();
        }

        binding.edtSearch.setOnClickListener(view -> {
            Fragment restaurantMenuItemsFragment = new RestaurantSpecialityFrag(new FragmentCallback() {
                @Override
                public void onResponce(Bundle bundle) {

                    if (bundle != null) {
                        specialtyFIlter = bundle.getString("speciality");
                        if (specialtyFIlter.equals("Anything")) {
                            getrestaurantlist("any");
                            getRestSpecialityList();
                        } else {
                            specialtyList.clear();
                            specialtyList.add(specialtyFIlter);
                            getrestaurantlist(specialtyFIlter);
                            getRestaurantListFilter(specialtyList);
                        }
                    }
                }
            });

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.newsFragment, restaurantMenuItemsFragment, "ParentFragment").commit();
        });

        try {
            search = getArguments().getString("search");
        } catch (Exception e) {

        }

        if (currentLoc.isEmpty()) {
            binding.toolbarDashboard.setTitle("Kalma Chowk Lahore");
        } else {
            binding.toolbarDashboard.setTitle(currentLoc);
        }

        if (search != null && search.equals("any")) {
            getrestaurantlist("any");
            getRestSpecialityList();
        } else if (specialtyFIlter != null) {
            specialtyList.clear();
            specialtyList.add(specialtyFIlter);
            getrestaurantlist(specialtyFIlter);
            getRestaurantListFilter(specialtyList);
        } else if (search != null) {
            specialtyList.clear();
            specialtyList.add(search);
            getrestaurantlist(search);
            getRestaurantListFilter(specialtyList);
        } else {
            getRestSpecialityList();
        }

        binding.map.setOnClickListener(view -> {
            openRestaurentOnMap();
        });
        binding.toolbarDashboard.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), MapsActivity.class);
            startActivity(i);
        });

        binding.toolbarDashboard.setNavigationOnClickListener(view -> {
            Intent i = new Intent(getActivity(), HomeScreen.class);
            startActivity(i);
        });

        binding.viewAll.setOnClickListener(view -> {
            Fragment restaurantMenuItemsFragment = new RestaurantSpecialityFrag(new FragmentCallback() {
                @Override
                public void onResponce(Bundle bundle) {

                    if (bundle != null) {
                        specialtyFIlter = bundle.getString("speciality");
                        if (specialtyFIlter.equals("Anything")) {
                            getrestaurantlist("any");
                            getRestSpecialityList();
                        } else {
                            specialtyList.clear();
                            specialtyList.add(specialtyFIlter);
                            getrestaurantlist(specialtyFIlter);
                            getRestaurantListFilter(specialtyList);
                        }
                    }
                }
            });

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.newsFragment, restaurantMenuItemsFragment, "ParentFragment").commit();
        });

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        ArrayList<String> stringofnames = new ArrayList<>();
        stringofnames.add("Open an online store with us.");
        stringofnames.add("Become a Delivery Guy. Join Now");
        stringofnames.add("Who we are? Get to know us more");
        stringofnames.add("We'll love to assist you");
        stringofnames.add("Knowledge base & Help Desk");

        ArrayList<String> buttonNames = new ArrayList<>();
        buttonNames.add("Get Started");
        buttonNames.add("Now Ninja");
        buttonNames.add("About Us");
        buttonNames.add("Contact support");
        buttonNames.add("Help Desk");

        offer_adapter = new Offer_adapter(getContext(), stringofnames, buttonNames);

        binding.rcvNearbyLaundry.setLayoutManager(horizontalLayoutManager);
        binding.rcvNearbyLaundry.setAdapter(offer_adapter);


        binding.anything.setOnClickListener(view -> {
            getrestaurantlist("any");
            getRestSpecialityList();
        });

        binding.africanMeals.setOnClickListener(view -> {
            specialtyList.clear();
            specialtyList.add("African Cuisine");
            getrestaurantlist("African Cuisine");
            getRestaurantListFilter(specialtyList);
        });

        binding.grocery.setOnClickListener(view -> {
            specialtyList.clear();
            specialtyList.add("African Market");
            getrestaurantlist("African Market");
            /*
            specialtyList.add("Food and Groceries");
            getrestaurantlist("Food and Groceries");
             */
            getRestaurantListFilter(specialtyList);
        });

        binding.deliveryServices.setOnClickListener(view -> {
            Intent nextScreen = new Intent(getContext(), DeliveryActivity.class);
            startActivity(nextScreen);
        });

        saveAddressRequest();
        getImageSliders();
        getNotifications();

        return binding.getRoot();
    }

    public void openRestaurentOnMap() {
        Fragment restaurentsOnMap_a = new RestaurentsOnMap_F();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.newsFragment, restaurentsOnMap_a, "parent").commit();
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    public void saveAddressRequest() {
        String latitude = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        String longitude = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");

        String user_id = sharedPreferences.getString(PreferenceClass.pre_user_id, "");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("default", "1");
            jsonObject.put("street", sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, ""));
            jsonObject.put("apartment", "0");
            jsonObject.put("city", sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, ""));
            jsonObject.put("state", "state");
            jsonObject.put("country", "0");
            jsonObject.put("zip", "0");
            jsonObject.put("instruction", "");
            jsonObject.put("lat", "" + latitude);
            jsonObject.put("long", "" + longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(getApplicationContext(), Config.ADD_DELIVERY_ADDRESS, jsonObject, resp -> {

            try {
                JSONObject jsonResponse = new JSONObject(resp);

                int code_id = Integer.parseInt(jsonResponse.optString("code"));

                if (code_id == 200) {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }


    public void getImageSliders() {

        JSONObject jsonObject = new JSONObject();

        ApiRequest.callNewApi(getContext(), Config.getImageSliders, jsonObject, resp -> {

            try {
                AppImageSliderResponse appImageSliderResponse = new Gson().fromJson(resp, AppImageSliderResponse.class);

                //Log.d("ErrrrorJoor", appImageSliderResponse.getMsg().get(0).getImage());

                int code_id = appImageSliderResponse.getCode();

                JSONObject obj = new JSONObject(resp);

                if (code_id == 200) {

                    ArrayList<AppImageSlider> sliders = new ArrayList<>();
                    JSONArray array = obj.getJSONArray("msg");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj2 = array.getJSONObject(i);
                        sliders.add(new Gson().fromJson(obj2.getJSONObject("AppSlider").toString(), AppImageSlider.class));
                    }
                    Collections.shuffle(sliders);
                    setUpViewPager(sliders);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void getNotifications() {

        JSONObject jsonObject = new JSONObject();
        try {
            deviceToken = (deviceToken != null) ? deviceToken : Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObject.put("device_id", (deviceToken != null) ? deviceToken : "");
            //jsonObject.put("device_id", "184");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.callNewApi(getContext(), Config.getImageNotification, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                try {

                    JSONObject obj = new JSONObject(resp);
                    int code = obj.optInt("code");

                    if (code == 200) {
                        viewNotification(obj.optJSONObject("msg"));
                    }

                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void viewNotification(JSONObject msg) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.notification_view_dialog);
        ImageView imageView = dialog.findViewById(R.id.imageNotify);
        ImageButton imageButton = dialog.findViewById(R.id.btnCancelNotify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        RequestOptions options = RequestOptions.fitCenterTransform()
                .transform(new RoundedCorners(5))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.getWidth(), imageView.getHeight());

        Glide.with(requireContext()).asBitmap().thumbnail(0.03f).apply(options).load(Config.imgBaseURL + msg.optString("image"))
                .placeholder(R.drawable.image_placeholder).into(imageView);


        imageButton.setOnClickListener(v -> dialog.dismiss());

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();

    }

    private void setUpViewPager(ArrayList<AppImageSlider> sliders) {

        AppImageSliderAdapter slider = new AppImageSliderAdapter(sliders, requireContext(), this);

        binding.sliderImages.setAdapter(slider);
        if (sliders.size() <= 1)
            binding.slidersDotsView.setVisibility(GONE);
        else {
            binding.slidersDotsView.setVisibility(VISIBLE);
            binding.slidersDotsView.setNoOfPages(sliders.size());
        }

        binding.sliderImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.slidersDotsView.onPageChange(position);
            }
        });

        handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (slider.getItemCount() == page)
                    page = 0;
                else
                    page++;
                binding.sliderImages.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };

        runnable.run();
    }

    @Override
    public void checkSlider(AppImageSlider slider) {
        if (slider.getType().equals("1")) {
            specialtyList.clear();
            specialtyList.add(slider.getTarget());
            getrestaurantlist(slider.getTarget());
            getRestaurantListFilter(specialtyList);
        } else if (slider.getType().equals("2")) {
            Intent nextScreen = new Intent(getContext(), WebviewActivity.class);
            nextScreen.putExtra("URL", slider.getTarget());
            startActivity(nextScreen);
        } else {
            Intent nextScreen = new Intent(getContext(), FeaturedViewerActivity.class);
            nextScreen.putExtra("data", slider);
            startActivity(nextScreen);
        }
    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (MapsActivity.SAVE_LOCATION) {

            lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
            lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");

            Address locationAddress;

            locationAddress = getAddress(Double.parseDouble(lat), Double.parseDouble(lon));
            if (locationAddress != null) {

                String city = "";
                if (locationAddress.getLocality() != null && !locationAddress.getLocality().equals("null"))
                    city = "" + locationAddress.getLocality();

                String country = "";
                if (locationAddress.getCountryName() != null && !locationAddress.getCountryName().equals("null"))
                    country = "" + locationAddress.getCountryName();


                String address = city + " " + country;

                binding.toolbarDashboard.setTitle(address);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PreferenceClass.CURRENT_LOCATION_ADDRESS, address).apply();
                MapsActivity.SAVE_LOCATION = false;
            }
        }
    }

    public void getrestaurantlist(String specialty) {
        promotedDataList.clear();
        adapterPromoted.notifyDataSetChanged();
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", Double.parseDouble(lat));
            jsonObject.put("long", Double.parseDouble(lon));
            jsonObject.put("current_time", formattedDate);
            jsonObject.put("user_id", userId.isEmpty() ? null : userId);


        } catch (Exception e) {
            Toast.makeText(getContext(), "Error occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ApiRequest.callApi(getContext(), Config.SHOW_RESTAURANTS, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                try {
                    JSONObject jsonResponse = new JSONObject(resp);
                    int code_id = Integer.parseInt(jsonResponse.optString("code"));
                    if (code_id == 200) {

                        ArrayList<RestaurantsModel> tem_list = new ArrayList<>();

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        JSONArray promoted = json.optJSONArray("promoted");

                        if (promoted != null) {
                            for (int i = 0; i < promoted.length(); i++) {

                                JSONObject json1 = promoted.getJSONObject(i);
                                RestaurantsModel model = DataParser.Pasrse_Restaurent(json1);

                                if (specialty.equalsIgnoreCase("any")) {
                                    promotedDataList.add(model);
                                } else if (specialty.equalsIgnoreCase(model.speciality)) {
                                    promotedDataList.add(model);
                                }
                            }
                        }

                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject json1 = jsonarray.getJSONObject(i);

                            tem_list.add(DataParser.Pasrse_Restaurent(json1));
                        }

                        if (promotedDataList.isEmpty()) {
                            binding.promotedRv.setVisibility(GONE);
                            binding.promotedTv.setVisibility(VISIBLE);
                        } else {
                            binding.promotedRv.setVisibility(VISIBLE);
                            binding.promotedTv.setVisibility(GONE);
                            Collections.shuffle(promotedDataList);
                            adapterPromoted.notifyDataSetChanged();
                        }
                    } else {
                        JSONObject json = new JSONObject(jsonResponse.toString());
                    }

                } catch (JSONException e) {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRestaurantListFilter(ArrayList<String> specialtyList) {
        binding.collapsingToolbar.setVisibility(GONE);
        binding.nestedScroolView.setVisibility(GONE);
        binding.loadingRelLayout.setVisibility(VISIBLE);
        binding.restaurantProgress.start();
        specialtyRestuarantsModels.clear();
        getRestaurantListAgainstSpeciality(specialtyList);
    }

    public void getRestaurantListAgainstSpeciality(ArrayList<String> specialtyList) {

        ArrayList<RestaurantsModel> models = new ArrayList<>();
        String name = specialtyList.get(0);
        String lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        String lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("lat", Double.parseDouble(lat));
            jsonObject.put("long", Double.parseDouble(lon));
            jsonObject.put("user_id", userId.isEmpty() ? null : userId);
            jsonObject.put("speciality", name);

            specialtyList.remove(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.callApi(getContext(), Config.SHOW_REST_AGAINST_SPECIALITY, jsonObject, resp -> {

            try {
                JSONObject jsonResponse = new JSONObject(resp);

                int code_id = Integer.parseInt(jsonResponse.optString("code"));
                if (code_id == 200) {
                    JSONObject json = new JSONObject(jsonResponse.toString());
                    JSONArray jsonarray = json.getJSONArray("msg");

                    if (jsonarray.length() <= 0) {
                        setLoaderVisibilityGone();
                    } else {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject json1 = jsonarray.getJSONObject(i);
                            RestaurantsModel model = DataParser.Pasrse_Restaurent(json1);

                            if (!models.contains(model))
                                models.add(model);

                            if (i == jsonarray.length() - 1) {

                                if (!models.isEmpty()) {
                                    Collections.shuffle(models);
                                    specialtyRestuarantsModels.add(new SpecialtyRestuarantsModel(name, models));
                                }

                                if (specialtyList.size() == 0) {
                                    setLoaderVisibilityGone();

                                    if (!specialtyRestuarantsModels.isEmpty()) {

                                        Collections.shuffle(specialtyRestuarantsModels);

                                        adapterAllRestaurantData = new AdapterAllRestaurantData2(NewRestuarantsFragment2.this, specialtyRestuarantsModels, getContext());
                                        binding.restaurantRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                        binding.restaurantRv.setAdapter(adapterAllRestaurantData);
                                        adapterAllRestaurantData.notifyDataSetChanged();

                                    }

                                } else {
                                    getRestaurantListAgainstSpeciality(specialtyList);
                                }

                            }
                        }
                    }
                } else {
                    setLoaderVisibilityGone();
                }
            } catch (JSONException e) {
                setLoaderVisibilityGone();
                e.printStackTrace();
                System.out.println(" the error " + e.toString());
                Toast.makeText(getApplicationContext(), "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoaderVisibilityGone() {
        binding.collapsingToolbar.setVisibility(VISIBLE);
        binding.nestedScroolView.setVisibility(VISIBLE);
        binding.loadingRelLayout.setVisibility(GONE);
        binding.restaurantProgress.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getActivity(), HomeScreen.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getRestSpecialityList() {

        specialtyList.clear();
        binding.collapsingToolbar.setVisibility(GONE);
        binding.nestedScroolView.setVisibility(GONE);
        binding.loadingRelLayout.setVisibility(VISIBLE);
        binding.restaurantProgress.start();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("long", lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(AllConstants.tag, lat + " " + lon);


        ApiRequest.callApi(getContext(), Config.SHOW_REST_SPECIALITY_LIST, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

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

                            specialtyList.add(specialityModel.getName());

                            if (i == jsonarray.length() - 1) {
                                Collections.shuffle(specialtyList);
                                getRestaurantListFilter(specialtyList);
                            }

                        }


                    }


                } catch (JSONException e) {
                    e.getMessage();
                    setLoaderVisibilityGone();
                }


            }
        });


    }
}