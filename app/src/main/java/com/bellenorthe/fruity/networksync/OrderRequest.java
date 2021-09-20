package com.bellenorthe.fruity.networksync;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OrderRequest  extends StringRequest {
    public static final String TAG = "MyTag";

    private static final String REGISTER_URL = "http://192.168.100.166/MamaM/fruitDeliver/public/api/orders";
    private Map<String, String> parameters;
    public OrderRequest(String userid, String grandtotal, String itemcount, String paymentmethod, String firstname, String lastname, String address, String city, String postcode, String phonenumber, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id", userid);
        parameters.put("grandtotal", grandtotal);
        parameters.put("itemcount", itemcount);
        parameters.put("paymentmethod", paymentmethod);
        parameters.put("firstname", firstname);
        parameters.put("lastname", lastname);
        parameters.put("mobile", phonenumber);
        parameters.put("address", address);
        parameters.put("city", city);
        parameters.put("postcode", postcode);
    }
    @Override
    protected Map<String, String> getParams() {
        Log.d(TAG, "OrderRequest: returned");
        return parameters;
    }
}