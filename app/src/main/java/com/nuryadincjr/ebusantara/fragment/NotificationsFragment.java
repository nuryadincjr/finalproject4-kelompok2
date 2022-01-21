package com.nuryadincjr.ebusantara.fragment;

import static com.nuryadincjr.ebusantara.databinding.FragmentNotificationsBinding.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuryadincjr.ebusantara.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       FragmentNotificationsBinding binding = inflate(inflater, container, false);

        binding.rvNotification.setVisibility(View.GONE);
        binding.layoutInfo.linearLayout.setVisibility(View.VISIBLE);
        return binding.getRoot();
    }
}