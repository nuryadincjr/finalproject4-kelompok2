package com.nuryadincjr.ebusantara.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.FragmentTicketsBinding;

public class TicketsFragment extends Fragment {

    private FragmentTicketsBinding binding;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTicketsBinding.inflate(inflater, container, false);

        binding.rvTickets.setVisibility(View.GONE);
        binding.layoutError.linearLayout.setVisibility(View.VISIBLE);
        return binding.getRoot();
    }
}