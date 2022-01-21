package com.nuryadincjr.ebusantara.fragment;

import static com.nuryadincjr.ebusantara.databinding.FragmentProfileBinding.inflate;
import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.FragmentProfileBinding;
import com.nuryadincjr.ebusantara.login.SettingsActivity;
import com.nuryadincjr.ebusantara.pojo.Users;

public class ProfileFragment extends Fragment {

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
        FragmentProfileBinding binding = inflate(inflater, container, false);

        SharedPreferences preference = getInstance(getContext()).getPreferences();
        String uid = preference.getString("uid", "");
        String name = preference.getString("name", "");
        String email = preference.getString("email", "");
        String phone = preference.getString("phone", "");
        String photo = preference.getString("photo", "");
        Users users = new Users(uid, name, phone, email, photo);

        binding.tvName.setText(users.getName());
        binding.tvPhoneNumber.setText(users.getPhoneNumber());
        Glide.with(binding.getRoot())
                .load(users.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_brand)
                .into(binding.ivProfile);

        binding.ivSetting.setOnClickListener(v ->
                startActivity(new Intent(getContext(), SettingsActivity.class)));
        return binding.getRoot();
    }
}