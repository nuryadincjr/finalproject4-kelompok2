package com.nuryadincjr.ebusantara.login;

import static com.nuryadincjr.ebusantara.databinding.ActivitySetupBinding.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivitySetupBinding;

public class SetupActivity extends AppCompatActivity {

    private ActivitySetupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSaveNumber.setOnClickListener(v ->
                startActivity(new Intent(this, FinishRegistrationActivity.class)));
    }
}