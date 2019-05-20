package com.ey.dgs.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ey.dgs.R;
import com.ey.dgs.utils.FragmentUtils;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        FragmentUtils.newInstance(getSupportFragmentManager()).addFragment(FragmentUtils.INDEX_LOGIN, null, LoginFragment.class.getName(), R.id.flContainer);
    }

}
