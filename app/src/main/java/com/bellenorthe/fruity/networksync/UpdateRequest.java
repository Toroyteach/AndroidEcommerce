package com.bellenorthe.fruity.networksync;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class UpdateRequest extends StringRequest {

    private static final String REGISTER_URL = "http://192.168.100.166/MamaM/fruitDeliver/public/api/update/";
    private Map<String, String> parameters;
    public UpdateRequest(String name, String mobile, String email, String UID, String newemail, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("newemail", newemail);
        parameters.put("mobile", mobile);
        parameters.put("email", email);
        parameters.put("uid", UID);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
