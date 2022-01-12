package com.nuryadincjr.ebusantara.fragment;

import static com.nuryadincjr.ebusantara.databinding.FragmentSearchBinding.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.chooser.DatePickerActivity;
import com.nuryadincjr.ebusantara.chooser.DestinationChooserActivity;
import com.nuryadincjr.ebusantara.databinding.FragmentSearchBinding;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class SearchFragment extends Fragment
        implements View.OnClickListener {

    private FragmentSearchBinding binding;
    private SlidingUpPanelLayout sliding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sliding = requireActivity().findViewById(R.id.sliding_layout);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);

        binding.llDeparture.setOnClickListener(this);
        binding.llArrival.setOnClickListener(this);
        binding.llPassengers.setOnClickListener(this);
        binding.llDate.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        onGetListener(v);
    }

    @SuppressLint("NonConstantResourceId")
    private void onGetListener(View v) {
        Activity activity = null;
        switch (v.getId()){
            case R.id.etDeparture:
            case R.id.etArrival:
                activity = new DestinationChooserActivity();
                break;
            case R.id.llPassengers:
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.llDate:
                activity = new DatePickerActivity();
                break;
        }

        if(activity != null){
            startActivity(new Intent(getContext(), activity.getClass()));
        }
    }
}