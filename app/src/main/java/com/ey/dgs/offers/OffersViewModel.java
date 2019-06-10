package com.ey.dgs.offers;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.model.Offer;
import com.ey.dgs.model.SplashItem;

import java.util.ArrayList;


public class OffersViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Offer>> offerItems = new MutableLiveData<>();

    public void setContext(Context ctx) {
        Context context = ctx;
        ArrayList<Offer> offers = new ArrayList<>();
        offers.add(new Offer());
        offers.add(new Offer());
        offers.add(new Offer());
        offers.add(new Offer());
        setOfferItems(offers);
    }


    public MutableLiveData<ArrayList<Offer>> getOfferItems() {
        return offerItems;
    }

    public void setOfferItems(ArrayList<Offer> offerItems) {
        this.offerItems.postValue(offerItems);
    }

}
