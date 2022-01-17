package com.nuryadincjr.ebusantara.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.FragmentProfileBinding;
import com.nuryadincjr.ebusantara.login.LoginActivity;
import com.nuryadincjr.ebusantara.pojo.Users;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        Users users = getArguments().getParcelable("user");

        binding.tvName.setText(users.getName());
        binding.tvPhoneNumber.setText(users.getPhoneNumber());
        Glide.with(binding.getRoot())
                .load(users.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_brand)
                .into(binding.ivProfile);

        binding.llLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finishAffinity();
        });

        return binding.getRoot();
    }
}