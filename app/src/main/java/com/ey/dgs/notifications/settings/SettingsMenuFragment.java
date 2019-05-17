package com.ey.dgs.notifications.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountAdapter;
import com.ey.dgs.adapters.SettingsMenuAdapter;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.model.SettingMenuItem;

import java.util.ArrayList;

public class SettingsMenuFragment extends Fragment {

    private View view;
    private RecyclerView rvSettings;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<SettingMenuItem> settingMenuItems = new ArrayList<>();
    private SettingsMenuAdapter settingsMenuAdapter;
    private OnFragmentInteractionListener mListener;

    public SettingsMenuFragment() {
    }

    public static SettingsMenuFragment newInstance() {
        return new SettingsMenuFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_menu, container, false);
        initViews();
        setData();
        return view;
    }

    private void initViews() {
        if (mListener != null) {
            mListener.onFragmentInteraction("More");
        }
        rvSettings = view.findViewById(R.id.rvSettings);
        rvSettings.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvSettings.setLayoutManager(rvLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvSettings.addItemDecoration(itemDecorator);
        rvSettings.setItemAnimator(new DefaultItemAnimator());
        settingsMenuAdapter = new SettingsMenuAdapter(getActivity(), this, settingMenuItems);
        rvSettings.setAdapter(settingsMenuAdapter);
    }

    private void setData() {
        settingMenuItems.add(new SettingMenuItem("My Account"));
        settingMenuItems.add(new SettingMenuItem("Notifications"));
        settingsMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void moveToNotificationTogglePage(SettingMenuItem settingMenuItem) {
        ((NotificationSettingsActivity) getActivity()).moveToNotificationTogglePage();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

}
