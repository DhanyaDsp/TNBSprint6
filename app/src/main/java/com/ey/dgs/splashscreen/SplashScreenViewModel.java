package com.ey.dgs.splashscreen;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.model.SplashItem;
import com.ey.dgs.model.User;
import com.ey.dgs.utils.AppPreferences;

import java.util.ArrayList;


public class SplashScreenViewModel extends ViewModel {

    private MutableLiveData<ArrayList<SplashItem>> splashItems = new MutableLiveData<>();

    public void setContext(Context ctx) {
        Context context = ctx;
        ArrayList<SplashItem> splashItems = new ArrayList<>();
        splashItems.add(new SplashItem(0, "Your One-Stop Lifestyle Shop", "Indulge in amazing offers with myTNB. Customise your interests and enjoy preferred lifestyle experiences at exclusive rates."));
        splashItems.add(new SplashItem(0, "Manage Consumption & Energy Insights", "Let us be your energy advisor. Share with us about your household and receive intelligent energy saving tips!"));
        splashItems.add(new SplashItem(0, "Customise Your Notifications", "Personalise your journey with myTNB. Customise your push and SMS notifications at your convenience with our smart splashItems."));
        setSplashItems(splashItems);
    }


    public MutableLiveData<ArrayList<SplashItem>> getSplashItems() {
        return splashItems;
    }

    public void setSplashItems(ArrayList<SplashItem> splashItems) {
        this.splashItems.postValue(splashItems);
    }

}
