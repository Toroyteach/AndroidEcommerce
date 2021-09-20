package com.bellenorthe.fruity.networksync;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class  ChangePhotoRequest extends StringRequest {

    private static final String REGISTER_URL = "http://192.168.100.166/MamaM/fruitDeliver/public/api/update/photo/";
    private Map<String, String> parameters;
    public ChangePhotoRequest(String email,String image, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("email", email);
        parameters.put("image", image);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
