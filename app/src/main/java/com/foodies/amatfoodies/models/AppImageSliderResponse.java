package com.foodies.amatfoodies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppImageSliderResponse {

    @SerializedName("code")
    private int code = 0;
    @SerializedName("msg")
    private ArrayList<AppImageSlider> msg = new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<AppImageSlider> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<AppImageSlider> msg) {
        this.msg = msg;
    }
}
