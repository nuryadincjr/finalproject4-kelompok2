package com.nuryadincjr.ebusantara.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityRetailPaymentVerificationBinding;

public class RetailPaymentVerificationActivity extends AppCompatActivity {

    private ActivityRetailPaymentVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_payment_verification);

        binding = ActivityRetailPaymentVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String total = getIntent().getStringExtra("total");
        binding.layoutTotalPayment.tvTotal.setText(total);
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}