package com.nuryadincjr.ebusantara.payment;

import static com.nuryadincjr.ebusantara.databinding.ActivityPaymentBinding.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityPaymentBinding;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;

public class PaymentActivity extends AppCompatActivity
        implements View.OnClickListener {
    private Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActivityPaymentBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactions = getIntent().getParcelableExtra("transactions");

        binding.llCreditCard.setOnClickListener(this);
        binding.llBankTransfer.setOnClickListener(this);
        binding.llRetailPayment.setOnClickListener(this);
        binding.layoutTotalPayment.tvTotal.setText(transactions.getTotalPayment());
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Activity activity = null;
        switch (v.getId()){
            case R.id.llCreditCard:
                activity = new CreditCardVerificationActivity();
                break;
            case R.id.llBankTransfer:
                activity = new BankTransferActivity();
                break;
            case R.id.llRetailPayment:
                activity = new RetailPaymentVerificationActivity();
                break;
        }

        if(activity != null) {
            ScheduleReference schedule = getIntent().getParcelableExtra("schedule");
            startActivity(new Intent(this, activity.getClass())
                    .putExtra("transactions", transactions)
                    .putExtra("schedule", schedule));
        }
    }
}