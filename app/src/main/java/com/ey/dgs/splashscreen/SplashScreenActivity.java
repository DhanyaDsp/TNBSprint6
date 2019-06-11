package com.ey.dgs.splashscreen;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.model.SplashItem;

import java.util.ArrayList;


public class SplashScreenActivity extends AppCompatActivity {

    ViewPager vpSplashItems;
    SplashPagerAdapter splashPagerAdapter;
    SplashScreenViewModel splashScreenViewModel;
    ArrayList<SplashItem> splashItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initViews();
        subscribe();
    }

    private void subscribe() {
        splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
        splashScreenViewModel.setContext(this);
        splashScreenViewModel.getSplashItems().observe(this, splashItems -> {
            this.splashItems = splashItems;
            splashPagerAdapter = new SplashPagerAdapter(getSupportFragmentManager(), this.splashItems);
            vpSplashItems.setAdapter(splashPagerAdapter);
        });
    }

    private void initViews() {
        vpSplashItems = findViewById(R.id.vpSplashItems);
    }

    public void moveToNext(View view) {
        if (vpSplashItems.getCurrentItem() >= splashItems.size() - 1) {
            moveToHomePage();
        } else {
            vpSplashItems.setCurrentItem(vpSplashItems.getCurrentItem() + 1);
        }
    }

    private void moveToHomePage() {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("UserName", getIntent().getStringExtra("UserName"));
        startActivity(intent);
    }
}
