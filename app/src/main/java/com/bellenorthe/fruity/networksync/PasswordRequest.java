package com.bellenorthe.fruity.networksync;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class PasswordRequest extends StringRequest {

    private static final String LOGIN_URL = "http://192.168.100.166/MamaM/fruitDeliver/public/api/passwordupdate/";
    private Map<String, String> parameters;

    public PasswordRequest(String email, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("email", email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
