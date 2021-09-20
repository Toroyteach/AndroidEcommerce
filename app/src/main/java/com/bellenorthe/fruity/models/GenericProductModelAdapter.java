package com.bellenorthe.fruity.models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bellenorthe.fruity.IndividualProduct;
import com.bellenorthe.fruity.R;
import com.bellenorthe.fruity.prodcutscategory.Cards;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class GenericProductModelAdapter extends FirestoreRecyclerAdapter<GenericProductModel, GenericProductModelAdapter.MovieViewHolder> {
    private static final String TAG = "OrderAdapter";
    Dialog popupDialog;
    @SuppressLint("StaticFieldLeak")
    private static Activity ctx;

    private GenericProductModel[] listdata;

    // RecyclerView recyclerView;
    public GenericProductModelAdapter(FirestoreRecyclerOptions<GenericProductModel> options, Activity ctx) {
        super(options);
        GenericProductModelAdapter.ctx = ctx;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_cardview_layout, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull GenericProductModel model) {
        holder.cardname.setText(model.getCardname());
        holder.cardprice.setText(model.getCardprice());
        Picasso.with(ctx).load(model.getCardimage()).into(holder.cardimage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, IndividualProduct.class);
                intent.putExtra("product",getItem(position));
                //startActivity(intent);
            }
        });
    }

    //sub class for view holder
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
    // END_INCLUDE
}
