package com.ey.dgs.lifestyle;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.ey.dgs.R;
import com.ey.dgs.model.Lifecycle;
import com.ey.dgs.model.SplashItem;

import java.util.ArrayList;

public class LifestyleViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Lifecycle>> lifecycles = new MutableLiveData<>();

    public void setContext(Context ctx) {
        Context context = ctx;
        ArrayList<Lifecycle> lifecycles = new ArrayList<>();
        setLifecycles(lifecycles);
    }


    public MutableLiveData<ArrayList<Lifecycle>> getLifecycles() {
        return lifecycles;
    }

    public void setLifecycles(ArrayList<Lifecycle> lifecycles) {
        this.lifecycles.postValue(lifecycles);
    }
}
