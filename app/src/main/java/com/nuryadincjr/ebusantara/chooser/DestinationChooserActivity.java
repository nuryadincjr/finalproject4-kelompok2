package com.nuryadincjr.ebusantara.chooser;

import static com.nuryadincjr.ebusantara.databinding.ActivityDestinationChooserBinding.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityDestinationChooserBinding;

public class DestinationChooserActivity extends AppCompatActivity {

    private ActivityDestinationChooserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvCancel.setOnClickListener(v -> onBackPressed());
    }
}