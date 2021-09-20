package com.bellenorthe.fruity.prodcutscategory;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bellenorthe.fruity.Cart;
import com.bellenorthe.fruity.IndividualProduct;
import com.bellenorthe.fruity.NotificationActivity;
import com.bellenorthe.fruity.R;
import com.bellenorthe.fruity.models.GenericProductModel;
import com.bellenorthe.fruity.models.GenericProductModelAdapter;
import com.bellenorthe.fruity.networksync.CheckInternetConnection;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Cards extends AppCompatActivity {


    private static final String TAG = "";
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private LottieAnimationView tv_no_item;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    //firebase firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cardsref, carditemref;
    private DocumentReference cardref;
    private GenericProductModelAdapter prodorderAdapter;
    private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //Initializing our Recyclerview
        mRecyclerView = findViewById(R.id.my_recycler_view);
        tv_no_item = findViewById(R.id.tv_no_cards);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //firestoredb(); //firebase cloud storage
        firestoreui();
        testfirebase();

    }

    public void testfirebase(){

    }

    public void firestoreui(){
        Query query = FirebaseFirestore.getInstance()
                .collection("Products")
                .document("Cards")
                .collection("item")
                .whereEqualTo("status","active");

        FirestoreRecyclerOptions<GenericProductModel> options = new FirestoreRecyclerOptions.Builder<GenericProductModel>()
                .setQuery(query, GenericProductModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<GenericProductModel, MovieViewHolder>(options) {
            @NonNull
            @Override
            public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_cardview_layout, parent, false);
                return new MovieViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull GenericProductModel model) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }

                Log.d(TAG, "onBindViewHolder: "+model.getCarddiscription()+" "+model.getCardname());

                holder.cardname.setText(model.getCarddiscription());
                holder.cardprice.setText(model.getCardprice());
                Picasso.with(Cards.this).load(model.getCardimage()).into(holder.cardimage);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Cards.this, IndividualProduct.class);
                        intent.putExtra("product",getItem(position));
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

//    public void firestoredb(){
//        cardsref = db.collection("Products");
//        cardref = cardsref.document("Cards");
//        carditemref = cardref.collection("item");
//
//        Query query = carditemref.whereEqualTo("status","active");
//        FirestoreRecyclerOptions<GenericProductModel> options = new FirestoreRecyclerOptions.Builder<GenericProductModel>().setQuery(query, GenericProductModel.class).build();
//        prodorderAdapter = new GenericProductModelAdapter(options, Cards.this);
//        mRecyclerView = findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(prodorderAdapter);
//        Log.d(TAG, "view holder.");
//
//    }
//
//    private void runfirestoredb() {
//        // FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
//        final FirebaseRecyclerAdapter<GenericProductModel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<GenericProductModel, MovieViewHolder>(
//                GenericProductModel.class,
//                R.layout.cards_cardview_layout,
//                MovieViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                mDatabaseReference.child("Products").child("Cards").getRef()
//        ) {
//            @Override
//            protected void populateViewHolder(final MovieViewHolder viewHolder, final GenericProductModel model, final int position) {
//                if(tv_no_item.getVisibility()== View.VISIBLE){
//                    tv_no_item.setVisibility(View.GONE);
//                }
//                viewHolder.cardname.setText(model.getCardname());
//                viewHolder.cardprice.setText("₹ "+Float.toString(model.getCardprice()));
//                Picasso.with(Cards.this).load(model.getCardimage()).into(viewHolder.cardimage);
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Cards.this,IndividualProduct.class);
//                        intent.putExtra("product",getItem(position));
//                        startActivity(intent);
//                    }
//                });
//            }
//        };
//
//        mRecyclerView.setAdapter(adapter);
//
//    }

    public void viewCart(View view) {
        startActivity(new Intent(Cards.this,Cart.class));
        finish();
    }


    //viewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;

        View mView;
        public MovieViewHolder(View v) {
            super(v);
            mView =v;
            cardname = v.findViewById(R.id.cardcategory);
            cardimage = v.findViewById(R.id.cardimage);
            cardprice = v.findViewById(R.id.cardprice);
        }
    }

    public void Notifications(View view) {
        startActivity(new Intent(Cards.this,NotificationActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
}
