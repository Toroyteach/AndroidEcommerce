package com.bellenorthe.fruity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bellenorthe.fruity.models.PlacedOrderModel;
import com.bellenorthe.fruity.models.SingleProductModel;
import com.bellenorthe.fruity.networksync.CheckInternetConnection;
import com.bellenorthe.fruity.networksync.LoginRequest;
import com.bellenorthe.fruity.networksync.OrderRequest;
import com.bellenorthe.fruity.usersession.UserSession;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetails extends AppCompatActivity {

    private static final Object TAG = "";
    @BindView(R.id.delivery_date)
    TextView deliveryDate;
    @BindView(R.id.no_of_items)
    TextView noOfItems;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.main_activity_multi_line_radio_group)
    MultiLineRadioGroup mainActivityMultiLineRadioGroup;
    @BindView(R.id.ordername)
    MaterialEditText ordername;
    @BindView(R.id.orderemail)
    MaterialEditText orderemail;
    @BindView(R.id.ordernumber)
    MaterialEditText ordernumber;
    @BindView(R.id.orderaddress)
    MaterialEditText orderaddress;
    @BindView(R.id.orderpincode)
    MaterialEditText orderpincode;

    private ArrayList<SingleProductModel> cartcollect;
    private String payment_mode="COD",order_reference_id;
    private String placed_user_name,getPlaced_user_email,getPlaced_user_mobile_no,uid;
    private UserSession session;
    private RequestQueue requestQueue;
    private HashMap<String,String> user;
    private DatabaseReference mDatabaseReference;
    private String currdatetime;
    private static final String URL = "http://192.168.100.166/MamaM/fruitDeliver/public/api/orders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestQueue = Volley.newRequestQueue(OrderDetails.this);//Creating the RequestQueue

        //SharedPreference for Cart Value
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        productdetails();

    }

    private void productdetails() {

        Bundle bundle = getIntent().getExtras();

        //setting total price
        assert bundle != null;
        totalAmount.setText(Objects.requireNonNull(bundle.get("totalprice")).toString());

        //setting number of products
        noOfItems.setText(Objects.requireNonNull(bundle.get("totalproducts")).toString());

        cartcollect = (ArrayList<SingleProductModel>) bundle.get("cartproducts");

        //delivery date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7);  // number of days to add
        String tomorrow = (formattedDate.format(c.getTime()));
        deliveryDate.setText(tomorrow);

        mainActivityMultiLineRadioGroup.setOnCheckedChangeListener((MultiLineRadioGroup.OnCheckedChangeListener) (group, button) -> payment_mode=button.getText().toString());

        user = session.getUserDetails();

        placed_user_name=user.get(UserSession.KEY_NAME);
        getPlaced_user_email=user.get(UserSession.KEY_EMAIL);
        getPlaced_user_mobile_no=user.get(UserSession.KEY_MOBiLE);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        currdatetime = sdf.format(new Date());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void PlaceOrder(View view) {
        Bundle bundle = getIntent().getExtras();
        String total = Objects.requireNonNull(bundle.get("totalprice")).toString();
        String itemscount = Objects.requireNonNull(bundle.get("totalproducts")).toString();

        if (validateFields(view)) {

            order_reference_id = getordernumber();

            //adding user details to the database under orders table
            mDatabaseReference.child("orders").child(getPlaced_user_mobile_no).child(currdatetime).push().setValue(getProductObject());

            //adding products to the order
            for(SingleProductModel model:cartcollect){
                mDatabaseReference.child("orders").child(getPlaced_user_mobile_no).child(currdatetime).child("items").push().setValue(model);
            }

            user = session.getUserDetails();
            makeorder(user.get(UserSession.KEY_UID), total, itemscount, "paypal", user.get(UserSession.KEY_NAME), user.get(UserSession.KEY_NAME), "address", "city", "postcode", user.get(UserSession.KEY_MOBiLE));

            mDatabaseReference.child("cart").child(getPlaced_user_mobile_no).removeValue();
            session.setCartValue(0);

            Intent intent = new Intent(OrderDetails.this, OrderPlaced.class);
            intent.putExtra("orderid",order_reference_id);
            startActivity(intent);
            finish();
        }
    }

    public void makeorder(String userid, String grandtotal, String itemcount, String paymentmethod, String firstname, String lastname, String address, String city, String postcode, String phonenumber){

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("user_id", userid);
            object.put("grandtotal", grandtotal);
            object.put("itemcount", itemcount);
            object.put("paymentmethod", paymentmethod);
            object.put("firstname", firstname);
            object.put("lastname", lastname);
            object.put("mobile", phonenumber);
            object.put("address", address);
            object.put("city", city);
            object.put("postcode", postcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(Login_screen.this,"String Response : "+ response.toString(),Toast.LENGTH_LONG).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.getBoolean("success")) {
                                //toast message to say successful
                                Log.d((String) TAG, "orders set successfully: ");

                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //loading.dismiss();
                        }
//                        resultTextView.setText("String Response : "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(OrderDetails.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    private boolean validateFields(View view) {

        if (Objects.requireNonNull(ordername.getText()).toString().length() == 0 || Objects.requireNonNull(orderemail.getText()).toString().length() == 0 || Objects.requireNonNull(ordernumber.getText()).toString().length() == 0 || Objects.requireNonNull(orderaddress.getText()).toString().length() == 0 ||
                Objects.requireNonNull(orderpincode.getText()).toString().length() == 0) {
            Snackbar.make(view, "Kindly Fill all the fields", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return false;
        } else if (orderemail.getText().toString().length() < 4 || orderemail.getText().toString().length() > 30) {
            orderemail.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!orderemail.getText().toString().matches("^[A-za-z0-9.@]+")) {
            orderemail.setError("Only . and @ characters allowed");
            return false;
        } else if (!orderemail.getText().toString().contains("@") || !orderemail.getText().toString().contains(".")) {
            orderemail.setError("Email must contain @ and .");
            return false;
        } else if (ordernumber.getText().toString().length() < 4 || ordernumber.getText().toString().length() > 12) {
            ordernumber.setError("Number Must consist of 10 characters");
            return false;
        } else if (orderpincode.getText().toString().length() < 6 || ordernumber.getText().toString().length() > 8){
            orderpincode.setError("Pincode must be of 6 digits");
            return false;
        }

        return true;
    }

    public PlacedOrderModel getProductObject() {
        return new PlacedOrderModel(order_reference_id,noOfItems.getText().toString(),totalAmount.getText().toString(),deliveryDate.getText().toString(),payment_mode, Objects.requireNonNull(ordername.getText()).toString(), Objects.requireNonNull(orderemail.getText()).toString(), Objects.requireNonNull(ordernumber.getText()).toString(), Objects.requireNonNull(orderaddress.getText()).toString(), Objects.requireNonNull(orderpincode.getText()).toString(),placed_user_name,getPlaced_user_email,getPlaced_user_mobile_no);
    }

    public String getordernumber() {

        return currdatetime.replaceAll("-","");
    }
}
