package com.bellenorthe.fruity.prodcutscategory;

import android.content.Intent;
import android.os.Bundle;
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
import com.bellenorthe.fruity.networksync.CheckInternetConnection;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class Calendars extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private LottieAnimationView tv_no_item;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

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
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
//        final FirebaseRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder> adapter = new FirebaseRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder>(
//                GenericProductModel.class,
//                R.layout.cards_cardview_layout,
//                Cards.MovieViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                mDatabaseReference.child("Products").child("Calendar").getRef()
//        ) {
//            @Override
//            protected void populateViewHolder(final Cards.MovieViewHolder viewHolder, final GenericProductModel model, final int position) {
//                if (tv_no_item.getVisibility() == View.VISIBLE) {
//                    tv_no_item.setVisibility(View.GONE);
//                }
//                viewHolder.cardname.setText(model.getCardname());
//                viewHolder.cardprice.setText("₹ " + Float.toString(model.getCardprice()));
//                Picasso.with(Calendars.this).load(model.getCardimage()).into(viewHolder.cardimage);
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Calendars.this, IndividualProduct.class);
//                        intent.putExtra("product", getItem(position));
//                        startActivity(intent);
//                    }
//                });
//            }
//        };
//
//
//        mRecyclerView.setAdapter(adapter);
        firestoreui();

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

        adapter = new FirestoreRecyclerAdapter<GenericProductModel, Cards.MovieViewHolder>(options) {
            @NonNull
            @Override
            public Cards.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cards_cardview_layout, parent, false);
                return new Cards.MovieViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Cards.MovieViewHolder holder, int position, @NonNull GenericProductModel model) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                holder.cardname.setText(model.getCarddiscription());
                holder.cardprice.setText(model.getCardprice());
                Picasso.with(Calendars.this).load(model.getCardimage()).into(holder.cardimage);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Calendars.this, IndividualProduct.class);
                        intent.putExtra("product",getItem(position));
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void viewCart(View view) {
        startActivity(new Intent(Calendars.this, Cart.class));
        finish();
    }


    //viewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;

        View mView;

        public MovieViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cardcategory);
            cardimage = v.findViewById(R.id.cardimage);
            cardprice = v.findViewById(R.id.cardprice);
        }
    }

    public void Notifications(View view) {
        startActivity(new Intent(Calendars.this, NotificationActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
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
}