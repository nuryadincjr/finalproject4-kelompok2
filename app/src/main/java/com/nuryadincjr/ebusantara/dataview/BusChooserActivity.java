package com.nuryadincjr.ebusantara.dataview;

import static com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityBusChooserBinding;

public class BusChooserActivity extends AppCompatActivity {

    private ActivityBusChooserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_chooser);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());

    }
}