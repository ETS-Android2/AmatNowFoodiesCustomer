package com.foodies.amatfoodies.retrofit.endpoints.endpoint_request_list;

import com.google.gson.annotations.SerializedName;

public class deliveryresponse {
    @SerializedName("pickup")
    private String pickup;
    @SerializedName("delivery")
    private String delivery;
    @SerializedName("distance")
    private String distance;
    @SerializedName("price_per_km")
    private String price_per_km;
    @SerializedName("subtotal")
    private String subtotal;
    @SerializedName("tax")
    private String tax;
    @SerializedName("currency")
    private String currency;
    @SerializedName("total")
    private String total;
    @SerializedName("symbol")
    private String symbol;

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice_per_km() {
        return price_per_km;
    }

    public void setPrice_per_km(String price_per_km) {
        this.price_per_km = price_per_km;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
