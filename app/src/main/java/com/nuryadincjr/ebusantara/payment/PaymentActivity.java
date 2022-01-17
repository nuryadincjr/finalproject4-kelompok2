package com.nuryadincjr.ebusantara.payment;

import static java.lang.Double.parseDouble;
import static java.text.NumberFormat.getCurrencyInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityPaymentBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutCompleteBookingBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutTotalPaymentBinding;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Seats;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPaymentBinding binding;
    private LayoutTotalPaymentBinding view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        view = binding.layoutTotalPayment;
        String total = getIntent().getStringExtra("total");
        view.tvTotal.setText(total);
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.tvSelectCreditCard.setOnClickListener(this);
        binding.tvSelectBankTransfer.setOnClickListener(this);
        binding.tvSelectRetailPayment.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Activity activity = null;
        switch (v.getId()){
            case R.id.tvSelectCreditCard:
                activity = new CreditCardVerificationActivity();
                break;
            case R.id.tvSelectBankTransfer:
                activity = new BankTransferActivity();
                break;
            case R.id.tvSelectRetailPayment:
                activity = new RetailPaymentVerificationActivity();
                break;
        }
        if(activity != null) {
            startActivity(new Intent(this, activity.getClass())
                    .putExtra("total", view.tvTotal.getText()));
        }
    }
}