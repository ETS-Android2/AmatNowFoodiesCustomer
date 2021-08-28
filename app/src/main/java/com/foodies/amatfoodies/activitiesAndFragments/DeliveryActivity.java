package com.foodies.amatfoodies.activitiesAndFragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.activities.BaseActivity;
import com.foodies.amatfoodies.constants.DataParser;
import com.foodies.amatfoodies.constants.FragmentCallback;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.databinding.ActivityDeliveryBinding;
import com.foodies.amatfoodies.models.AddressListModel;
import com.foodies.amatfoodies.models.ModelDeliveryDetails;
import com.foodies.amatfoodies.utils.slidinguppanel.SlidingUpPanelLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DeliveryActivity extends AppCompatActivity {
    ActivityDeliveryBinding binding;
    private CountDownTimer countDownTimer;
    private Long splashScreenTimer = 2500L;
    private Long timerInterval = 500L;
    Boolean isLoggedIn;
    SharedPreferences sharedPreferences;
    Runnable run;
    Boolean isSearchDone = false;
    ModelDeliveryDetails deliveryDetails;
    String instructions;
    String back = "front";
    String delivery_time;

    String street;
    String city;
    String state;
    String apartment;
    String addressId;
    int address_id;
    Boolean onAddress = false;
    Boolean onPayment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(PreferenceClass.IS_LOGIN, false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery);
        AndroidNetworking.initialize(getApplicationContext());

        Places.initialize(getApplicationContext(), getApplication().getResources().getString(R.string.key_for_places));
        try {
            back = getIntent().getExtras().getString("back");
        } catch (Exception e) {

        }
        if (!back.equals("back")) {
            countDownTimer = new CountDownTimer(splashScreenTimer, timerInterval) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (isLoggedIn) {
                        binding.splashScreenRelLayout.setVisibility(GONE);
                        binding.drawerLayout.setVisibility(VISIBLE);
                    } else {
                        binding.loginContainer.setVisibility(VISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("forDelivery", "yes");
                        Fragment restaurantMenuItemsFragment = new LoginActivity();
                        restaurantMenuItemsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.add(R.id.login_container, restaurantMenuItemsFragment, "ParentFragment").commit();
                    }
                }
            }.start();
        } else {
            binding.splashScreenRelLayout.setVisibility(GONE);
            binding.drawerLayout.setVisibility(VISIBLE);
        }

        binding.homeContent.edtWhereTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearchDone = false;
                binding.homeContent.rlLocation.setVisibility(VISIBLE);
                binding.homeContent.rlMap.setDragView(binding.homeContent.rlLocation);
                binding.homeContent.rlMap.setScrollableView(binding.homeContent.rvLocation);
                binding.homeContent.rlMap.setClipPanel(false);
                binding.homeContent.rlMap.setTouchEnabled(true);
                binding.homeContent.rlMap.setPanelHeight((binding.homeContent.rvLocation.getHeight() / 3) * 3);
                binding.homeContent.rlMap.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                binding.homeContent.ivMenu.setVisibility(VISIBLE);
                binding.homeContent.llBottom.setVisibility(GONE);
                run = () -> {
                    if (!isSearchDone) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(binding.homeContent.constrainHome);
                        constraintSet.clear(
                                R.id.rlSearch,
                                ConstraintSet.BOTTOM
                        );
                        TransitionManager.beginDelayedTransition(binding.homeContent.constrainHome);
                        constraintSet.applyTo(binding.homeContent.constrainHome);
                        TransitionManager.beginDelayedTransition(binding.homeContent.constrainHome);
                        constraintSet.applyTo(binding.homeContent.constrainHome);
                        binding.homeContent.ivClearPickup.setVisibility(VISIBLE);
                    }
                    //binding.homeContent.edtDestinationLocation.requestFocus();
                    new Handler().postDelayed(run, 100);
                };
                run.run();
            }
        });

        binding.homeContent.ivBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSearchLayout();
                String whereTo = binding.homeContent.edtSourceLocation.getText().toString();
                String destination = binding.homeContent.edtDestinationLocation.getText().toString();
                delivery_time = binding.homeContent.pickUpTime.getText().toString();
                if (!whereTo.isEmpty() && !destination.isEmpty() && !delivery_time.isEmpty()) {
                    sendDeliveryInfo(whereTo, address_id, delivery_time);
                } else if (whereTo.isEmpty()) {
                    Toast.makeText(DeliveryActivity.this, "Please enter a pickup location", Toast.LENGTH_SHORT).show();
                } else if (destination.isEmpty()) {
                    Toast.makeText(DeliveryActivity.this, "Please enter a destination", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeliveryActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.confirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.instructions.getText().toString().isEmpty()) {
                    Toast.makeText(DeliveryActivity.this, "Please Specify instructions upon delivery", Toast.LENGTH_SHORT).show();
                } else {
                    instructions = binding.instructions.getText().toString();
                    startWebViewActivity();
                }
            }
        });

        binding.homeContent.edtSourceLocation.setFocusable(false);
        binding.homeContent.edtDestinationLocation.setFocusable(false);
        binding.homeContent.edtSourceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*List<Place.Field> fieldList = Arrays.asList(
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                        Place.Field.NAME
                );
                Intent i = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getApplicationContext());
                startActivityForResult(i, 101);*/
                onAddress = true;
                onPayment = false;
                binding.addressListContainer.setVisibility(VISIBLE);
                openAddressList(true);
            }
        });

        binding.homeContent.edtDestinationLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressList(false);
                /*List<Place.Field> fieldList = Arrays.asList(
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                        Place.Field.NAME
                );
                Intent i = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getApplicationContext());
                startActivityForResult(i, 102);*/
                binding.addressListContainer.setVisibility(VISIBLE);
                onAddress = true;
                onPayment = false;
            }
        });
        binding.homeContent.pickUpTime.setFocusable(false);
        binding.homeContent.pickUpTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDateAndTimeDialog();
            }
        });
        binding.homeContent.btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whereTo = binding.homeContent.edtSourceLocation.getText().toString();
                String destination = binding.homeContent.edtDestinationLocation.getText().toString();
                delivery_time = binding.homeContent.pickUpTime.getText().toString();
                if (!whereTo.isEmpty() && !destination.isEmpty() & !delivery_time.isEmpty()) {
                    Toast.makeText(DeliveryActivity.this, "Processing request, please wait", Toast.LENGTH_SHORT).show();
                    sendDeliveryInfo(whereTo, address_id, delivery_time);
                } else if (whereTo.isEmpty()) {
                    Toast.makeText(DeliveryActivity.this, "Please enter a pickup location", Toast.LENGTH_SHORT).show();
                } else if (delivery_time.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter delivery date and time", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeliveryActivity.this, "Please enter a destination", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    AddressListModel addressListModel;

    public void openAddressList(Boolean forPickUp) {
        Fragment restaurantMenuItemsFragment = new AddressListFragment(new FragmentCallback() {
            @Override
            public void onResponce(Bundle bundle) {
                if (bundle != null) {
                    addressListModel = (AddressListModel) bundle.getSerializable("data");

                    street = addressListModel.getStreet();
                    city = addressListModel.getCity();
                    state = addressListModel.getState();
                    apartment = addressListModel.getApartment();
                    addressId = addressListModel.getAddress_id();
                    if (forPickUp) {
                        binding.homeContent.edtSourceLocation.setText(state + " , " + city + " , " + street + ", " + apartment);
                    } else {
                        binding.homeContent.edtDestinationLocation.setText(state + " , " + city + " , " + street + ", " + apartment);
                        address_id = Integer.parseInt(addressId);
                    }
                }
                onAddress = false;
                binding.addressListContainer.setVisibility(GONE);
            }
        });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("grand_total", "0");
        bundle.putString("rest_id", "47");
        bundle.putString("forDelivery", "true");
        restaurantMenuItemsFragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.add(R.id.address_list_container, restaurantMenuItemsFragment, "parent").commit();
    }

    String time;
    String dateTime;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateAndTimeDialog() {
        Calendar calender = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            calender.set(Calendar.YEAR, i);
            calender.set(Calendar.MONTH, i1);
            calender.set(Calendar.DAY_OF_MONTH, i2);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            time = simpleDateFormat.format(calender.getTime());
            showTimeDialog();
        };
        new DatePickerDialog(DeliveryActivity.this, dateSetListener, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog() {
        Calendar calender = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timePickerDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calender.set(Calendar.HOUR_OF_DAY, i);
                calender.set(Calendar.MINUTE, i1);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:MM");
                dateTime = time + " " + simpleDateFormat.format(calender.getTime());
                binding.homeContent.pickUpTime.setText(dateTime);
            }
        };
        new TimePickerDialog(DeliveryActivity.this, timePickerDialog, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.homeContent.edtSourceLocation.setText(place.getAddress());
        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.homeContent.edtDestinationLocation.setText(place.getAddress());
        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startWebViewActivity() {
        Intent i = new Intent(this, WebviewDeliveryActivity.class);
        i.putExtra("currency", deliveryDetails.currency);
        i.putExtra("delivery", deliveryDetails.delivery);
        i.putExtra("distance", deliveryDetails.distance);
        i.putExtra("pickUp", deliveryDetails.pickUp);
        i.putExtra("price_per_km", deliveryDetails.price_per_km);
        i.putExtra("subTotal", deliveryDetails.subTotal);
        i.putExtra("symbol", deliveryDetails.symbol);
        i.putExtra("tax", deliveryDetails.tax);
        i.putExtra("total", deliveryDetails.total);
        i.putExtra("instructions", instructions);
        i.putExtra("delivery_time", delivery_time);
        if (binding.cardStripe.isChecked()) {
            i.putExtra("payment_method", 11);
        } else if (binding.cardOnDelivery.isChecked()) {
            i.putExtra("payment_method", 4);
        } else if (binding.flutterwave.isChecked()) {
            i.putExtra("payment_method", 2);
        } else if (binding.cashOnDelivery.isChecked()) {
            i.putExtra("payment_method", 3);
        }
        startActivity(i);
    }

    private void sendDeliveryInfo(String from, int destination, String time) {
        String user_id = sharedPreferences.getString(PreferenceClass.pre_user_id, "");
        String url = "https://amatnow.com/api_proxy_request_fm.php?customDelivery=true&fro=" + from + "&to=" + destination + "&user_id=" + user_id;
        String finalUrl = url.replaceAll("\\s", "+");
        /*
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(DeliveryActivity.this, "response "+response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(DeliveryActivity.this, "error "+ anError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjReq = new StringRequest(
                Request.Method.GET,
                finalUrl,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONObject obj = jsonObject.getJSONObject("msg");
                            System.out.println("response" + obj);
                            deliveryDetails = DataParser.Pasrse_DeliveryDetails(obj);
                        }
                        setPaymentDetails();
                    } catch (Exception e) {
                    }
                },
                error -> Toast.makeText(DeliveryActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
        );
        mRequestQueue.add(jsonObjReq);
    }

    private void setPaymentDetails() {
        String total = deliveryDetails.symbol + " " + deliveryDetails.total;
        binding.Pickup.setText(deliveryDetails.pickUp);
        binding.delivery.setText(deliveryDetails.delivery);
        binding.distance.setText(deliveryDetails.distance);
        binding.priceKm.setText(deliveryDetails.price_per_km);
        binding.subTotal.setText(deliveryDetails.subTotal);
        binding.tax.setText(deliveryDetails.tax);
        binding.currency.setText(deliveryDetails.currency);
        binding.total.setText(total);
        binding.paymentLayout.setVisibility(VISIBLE);
        onPayment = true;
    }

    private void hideSearchLayout() {
        isSearchDone = true;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.homeContent.constrainHome);
        constraintSet.connect(
                R.id.rlSearch,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        );
        TransitionManager.beginDelayedTransition(binding.homeContent.constrainHome);
        //Come back and get edWhereTo and edFrom
        constraintSet.applyTo(binding.homeContent.constrainHome);
        binding.homeContent.rlLocation.setVisibility(GONE);
        binding.homeContent.llBottom.setVisibility(VISIBLE);
    }

    private void startLoginActivty() {
        Intent i = new Intent(DeliveryActivity.this, HomeScreen.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (onAddress) {
            binding.addressListContainer.setVisibility(GONE);
            onAddress = false;
        }
        if (onPayment) {
            binding.paymentLayout.setVisibility(GONE);
            onPayment = false;
            onAddress = true;
        } else {
            Intent i = new Intent(DeliveryActivity.this, BaseActivity.class);
            startActivity(i);
        }
    }
}