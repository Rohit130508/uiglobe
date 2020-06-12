package com.hotspot.user.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.hotspot.user.app.R;
import com.hotspot.user.app.ui.dashboard.DashboardFragment;
import com.hotspot.user.app.ui.dashboard.DashboardViewModel;
import com.hotspot.user.app.userprofile.My_Account;

import java.util.ArrayList;

public class UserProfile extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_userprofile, container, false);

        startActivity(new Intent(getActivity(), My_Account.class));
        return root;
    }


    }
