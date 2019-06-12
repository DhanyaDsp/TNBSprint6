package com.ey.dgs.authentication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.api_response.LoginResponse;
import com.ey.dgs.dashboard.DashboardViewModel;
import com.ey.dgs.dashboard.questions.QuestionActivity;
import com.ey.dgs.databinding.LoginFragmentBinding;
import com.ey.dgs.model.SplashItem;
import com.ey.dgs.model.User;
import com.ey.dgs.splashscreen.SplashScreenActivity;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.webservice.APICallback;
import com.ey.dgs.webservice.ApiClient;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginFragment extends Fragment implements APICallback {

    private LoginViewModel loginViewModel;
    private DashboardViewModel dashboardViewModel;
    LoginFragmentBinding loginFragmentBinding;
    User user = new User();
    ArrayList<User> users = new ArrayList<>();
    AppPreferences appPreferences;
    public boolean isLoading;
    View loader;
    private boolean isLoginClicked;
    private Observer<User> getUserReceiver;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        loginFragmentBinding.setFragment(this);
        loginFragmentBinding.setUser(user);
        initViews();
        initSetup();
        subscribe();
        getData();
        return loginFragmentBinding.getRoot();
    }

    private void initViews() {
        loader = loginFragmentBinding.getRoot().findViewById(R.id.loader);
    }

    private void initSetup() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setContext(getActivity());

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        appPreferences = new AppPreferences(getActivity());
    }

    private void getData() {
        if (appPreferences.isLoginned()) {
            loginViewModel.getUserDetail(appPreferences.getUser_id());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void subscribe() {
        getUserReceiver = usr -> {
            if (usr != null) {
                user = usr;
                if (!usr.isRememberMe()) {
                    user.setEmail(null);
                } else {
                }
                loginFragmentBinding.setUser(user);
            }
        };
        loginViewModel.getUserDetail().observeForever(getUserReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        //subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        //loginViewModel.getOfferItems().removeObserver(getUserReceiver);
    }

    public void login(View view) {
        isLoginClicked = true;
        if (isValidated(user)) {
            onProgress(0, true);
            new ApiClient().login(user, this);
        } else {
        }
    }

    private boolean isValidated(User user) {
        if (TextUtils.isEmpty(user.getEmail())) {
            Utils.showToast(getActivity(), "Please enter email");
            return false;
        } else if (TextUtils.isEmpty(user.getPassword())) {
            Utils.showToast(getActivity(), "Please enter password");
            return false;
        } else {
            return true;
        }
    }

    private void moveToHomePage() {
        getActivity().finish();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("UserName", user.getEmail());
        getActivity().startActivity(intent);
    }

    private void moveToSplashScreen() {
        getActivity().finish();
        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
        intent.putExtra("UserName", user.getEmail());
        intent.putExtra("user", (Serializable) user);
        getActivity().startActivity(intent);
    }

    @Override
    public void onSuccess(int requestCode, Object obj, int code) {
        onProgress(requestCode, false);
        LoginResponse loginResponse = (LoginResponse) obj;
        if (loginResponse.isSuccess()) {
            if (appPreferences.isLoginned()) {
                loginViewModel.update(user);
            } else {
                if (user.isRememberMe()) {
                    user.setUserId(1);
                    loginViewModel.addUserToLocalDB(user);
                    appPreferences.setLoginned(true);
                    appPreferences.setUser_id(user.getUserId());
                }
            }
            appPreferences.setAuthToken(loginResponse.getToken());
            if (user.isSplashScreenSeen()) {
                moveToHomePage();
            } else {
                moveToSplashScreen();
            }
        }
    }

    @Override
    public void onFailure(int requestCode, Object obj, int code) {
        onProgress(requestCode, false);
        Utils.showToast(getActivity(), ((LoginResponse) obj).getMessage());
    }

    @Override
    public void onProgress(int requestCode, boolean isLoading) {
        if (isLoading) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }
}
