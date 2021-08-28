package com.foodies.amatfoodies.retrofit.endpoints.endpoint_request_list;

import com.google.gson.annotations.SerializedName;


public class body {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("msg")
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public String getmsg() {
        return msg;
    }

}
