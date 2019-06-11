package com.ey.dgs.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.model.Offer;

import java.util.ArrayList;


public class OffersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Offer> offers;
    private Activity context;
    Fragment fragment;

    public OffersAdapter(Activity context, Fragment fragment, ArrayList<Offer> offers) {
        this.offers = offers;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new OfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        final Offer offer = this.offers.get(i);
        OfferViewHolder offerViewHolder = (OfferViewHolder) holder;
        //offerViewHolder.tvTitle.setText(offer.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.offers.size();
    }


    public class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView tvTitle;

        private OfferViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSettingMenuName);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
