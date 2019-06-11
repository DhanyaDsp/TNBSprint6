package com.ey.dgs.splashscreen;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.databinding.FragmentSplashItemBinding;
import com.ey.dgs.model.SplashItem;

import java.io.Serializable;

public class SplashItemFragment extends Fragment {
    private static final String SPLASH_ITEM = "splashItem";
    SplashItem splashItem;
    FragmentSplashItemBinding splashItemBinding;

    public SplashItemFragment() {
    }

    public static SplashItemFragment newInstance(SplashItem splashItem) {
        SplashItemFragment fragment = new SplashItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(SPLASH_ITEM, (Serializable) splashItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splashItem = (SplashItem) getArguments().getSerializable(SPLASH_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        splashItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_item, container, false);
        splashItemBinding.setSplashItem(splashItem);
        return splashItemBinding.getRoot();
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(AppCompatImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
