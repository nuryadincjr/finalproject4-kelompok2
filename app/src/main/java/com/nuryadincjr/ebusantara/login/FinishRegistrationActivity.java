package com.nuryadincjr.ebusantara.login;

import static com.nuryadincjr.ebusantara.databinding.ActivityFinishRegristrationBinding.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nuryadincjr.ebusantara.MainActivity;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityFinishRegristrationBinding;

public class FinishRegistrationActivity extends AppCompatActivity {

    private ActivityFinishRegristrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_regristration);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnTakeHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }
}