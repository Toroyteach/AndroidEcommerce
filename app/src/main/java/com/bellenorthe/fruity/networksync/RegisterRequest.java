package com.bellenorthe.fruity.networksync;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RegisterRequest  extends StringRequest {
    public static final String TAG = "MyTag";

    private static final String REGISTER_URL = "http://192.168.100.166/Ecommerce/freshgoodsapp/public/api/register";
    private Map<String, String> parameters;
    public RegisterRequest(String firstname, String lastname, String password, String mobile, String email, String photo, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("firstname", firstname);
        parameters.put("lastname", lastname);
        parameters.put("password", password);
        parameters.put("phonenumber", mobile);
        parameters.put("email", email);
        parameters.put("profileimage", photo);
    }
    @Override
    protected Map<String, String> getParams() {
        Log.d(TAG, "RegisterRequest: returned");
        return parameters;
    }
}
