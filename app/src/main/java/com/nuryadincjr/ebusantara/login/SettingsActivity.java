package com.nuryadincjr.ebusantara.login;

import static com.nuryadincjr.ebusantara.databinding.ActivitySettingsBinding.inflate;
import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActivitySettingsBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        binding.llLogout.setOnClickListener(v -> {
            auth.signOut();
            getInstance(this).getEditor().putString("uid", "").apply();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.llAbout.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
    }
}