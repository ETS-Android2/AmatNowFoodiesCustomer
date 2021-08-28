package com.foodies.amatfoodies.activitiesAndFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.foodies.amatfoodies.JavaInterface;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.constants.ApiRequest;
import com.foodies.amatfoodies.constants.Callback;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.models.AddressListModel;
import com.foodies.amatfoodies.models.ModelDeliveryDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebviewDeliveryActivity extends AppCompatActivity {
    WebView browser;
    String currency, delivery, distance, pickup, price_per_km,  subTotal, symbol, tax, total, instructions, delivery_time;
    ModelDeliveryDetails data;
    SharedPreferences sharedPreferences;
    int method;
    ProgressDialog pd;
    /*
    i.putExtra("currency", deliveryDetails.currency);
        i.putExtra("delivery", deliveryDetails.delivery);
        i.putExtra("distance", deliveryDetails.distance);
        i.putExtra("pickUp", deliveryDetails.pickUp);
        i.putExtra("price_per_km", deliveryDetails.price_per_km);
        i.putExtra("subTotal", deliveryDetails.subTotal);
        i.putExtra("symbol", deliveryDetails.symbol);
        i.putExtra("tax", deliveryDetails.tax);
        i.putExtra("total", deliveryDetails.total);
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_delivery);
        sharedPreferences = getApplicationContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        browser = findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        currency = getIntent().getExtras().getString("currency");
        delivery = getIntent().getExtras().getString("delivery");
        distance = getIntent().getExtras().getString("distance");
        pickup = getIntent().getExtras().getString("pickUp");
        price_per_km = getIntent().getExtras().getString("price_per_km");
        subTotal = getIntent().getExtras().getString("subTotal");
        symbol = getIntent().getExtras().getString("symbol");
        tax = getIntent().getExtras().getString("tax");
        total = getIntent().getExtras().getString("total");
        delivery_time = getIntent().getExtras().getString("delivery_time");
        instructions = getIntent().getExtras().getString("instructions");
        data = new ModelDeliveryDetails();
        method = getIntent().getExtras().getInt("payment_method");
        data.setCurrency(currency);
        data.setDelivery(delivery);
        data.setDistance(distance);
        data.setPickUp(pickup);
        data.setPrice_per_km(price_per_km);
        data.setSubTotal(subTotal);
        data.setTax(tax);
        data.setTotal(total);


        buildUrl(data, instructions, delivery_time);
    }

    private void buildUrl(ModelDeliveryDetails data, String instructions, String delivery_time) {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading....");
        pd.show();
        getAddressID();
    }

    private void getAddressID() {
        JSONObject addressJsonObject = new JSONObject();
        try {

            addressJsonObject.put("user_id", sharedPreferences.getString(PreferenceClass.pre_user_id, ""));

            addressJsonObject.put("restaurant_id", null);
            addressJsonObject.put("sub_total", null);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.callApi(getApplicationContext(), Config.GET_DELIVERY_ADDRESES, addressJsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                try {
                    JSONObject jsonResponse = new JSONObject(resp);

                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        JSONObject addressJson;
                        JSONObject jsonObjAdd = new JSONObject();

                        for (int i=0;i<jsonarray.length();i++){

                            addressJson = jsonarray.getJSONObject(i);
                            jsonObjAdd = addressJson.getJSONObject("Address");

                            AddressListModel addressListModel = new AddressListModel();
                            addressListModel.setApartment(jsonObjAdd.optString("apartment"));
                            addressListModel.setCity(jsonObjAdd.optString("city"));
                            addressListModel.setState(jsonObjAdd.optString("state"));
                            addressListModel.setStreet(jsonObjAdd.optString("street"));
                            addressListModel.setAddress_id(jsonObjAdd.optString("id"));
                            addressListModel.setDelivery_fee(jsonObjAdd.optString("delivery_fee"));
                            addressListModel.setInstruction(jsonObjAdd.optString("instructions"));
                            sharedPreferences.edit().putString(PreferenceClass.ADDRESS_ID, jsonObjAdd.optString("id")).commit();
                        }
                        String test = "https://amatnow.com/utilityPayment.php?user_id="+sharedPreferences.getString(PreferenceClass.pre_user_id, "")+"&price="+data.getTotal()+"&tax=7&address_id="+jsonObjAdd.optString("id")+"&instruction="+instructions+"&type=delivery&device=mobile&delivery_fee=0&payment_id="+method+"&menu_item=[{%22menu_item_name%22%20:%20%22Products%20Delivery%22,%20%22menu_item_quantity%22%20:%201,%20%22menu_item_price%22%20:%201000}]&restaurants_id=41&currency="+data.getCurrency()+"&deliverytime="+delivery_time.replaceAll("\\s", "%20");
                        Log.d("WebviewDeliveryActivity", "XbuildUrl "+test);
                        JavaInterface javaInterface = new JavaInterface(WebviewDeliveryActivity.this);
                        browser.addJavascriptInterface(javaInterface,"OpenNewScreen");
                        browser.setWebViewClient(new WebViewClient() {
                            public void onPageFinished(WebView view, String url) {
                                pd.dismiss();
                            }
                        });
                        browser.loadUrl(test);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DeliveryActivity.class);
        i.putExtra("back", "back");
        startActivity(i);
    }
}