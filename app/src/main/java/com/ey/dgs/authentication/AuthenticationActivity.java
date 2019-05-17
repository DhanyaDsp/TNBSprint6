package com.ey.dgs.authentication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ey.dgs.R;
import com.ey.dgs.utils.FragmentUtils;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        FragmentUtils.newInstance(getSupportFragmentManager()).setFragment(FragmentUtils.INDEX_LOGIN, null, LoginFragment.class.getName(), R.id.flContainer);
    }

}
