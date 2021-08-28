package com.foodies.amatfoodies.retrofit.endpoints.endpoint_request_list;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiDelivery {
    @GET("api_proxy_request_fm.php")
    Call<body> getRequestList(@Query("customDelivery") Boolean customDelivery, @Query("fro") String fro, @Query("to") String to);
}
