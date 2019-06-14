package com.ey.dgs.splashscreen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.model.SplashItem;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.usersettings.UserSettingsViewModel;

import java.util.ArrayList;


public class SplashScreenActivity extends AppCompatActivity {

    ViewPager vpSplashItems;
    SplashPagerAdapter splashPagerAdapter;
    SplashScreenViewModel splashScreenViewModel;
    ArrayList<SplashItem> splashItems = new ArrayList<>();
    private TabLayout tlDots;
    User user;
    UserSettingsViewModel userSettingsViewModel;
    private UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        user = (User) getIntent().getSerializableExtra("user");
        userSettings = (UserSettings) getIntent().getSerializableExtra("userSettings");
        initViews();
        subscribe();
    }

    private void subscribe() {
        userSettingsViewModel = ViewModelProviders.of(this).get(UserSettingsViewModel.class);
        userSettingsViewModel.setContext(this);
        splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
        splashScreenViewModel.setContext(this);
        splashScreenViewModel.getSplashItems().observe(this, splashItems -> {
            this.splashItems = splashItems;
            splashPagerAdapter = new SplashPagerAdapter(getSupportFragmentManager(), this.splashItems);
            vpSplashItems.setAdapter(splashPagerAdapter);
            tlDots = findViewById(R.id.tlDots);
            tlDots.setupWithViewPager(vpSplashItems, true);
        });
    }

    private void initViews() {
        vpSplashItems = findViewById(R.id.vpSplashItems);
    }

    public void moveToNext(View view) {
        if (vpSplashItems.getCurrentItem() >= splashItems.size() - 1) {
            userSettings.setShowSplashScreen(false);
            userSettingsViewModel.updateUserSettings(userSettings);
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
