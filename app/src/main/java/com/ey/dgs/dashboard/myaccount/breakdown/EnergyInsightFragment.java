package com.ey.dgs.dashboard.myaccount.breakdown;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;

public class EnergyInsightFragment extends Fragment {

    private EnergyInsightViewModel mViewModel;

    public static EnergyInsightFragment newInstance() {
        return new EnergyInsightFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.energy_insight_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EnergyInsightViewModel.class);
        // TODO: Use the ViewModel
    }

}
