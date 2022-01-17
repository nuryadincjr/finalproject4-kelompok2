package com.nuryadincjr.ebusantara.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityBankTransferVerificationBinding;

public class BankTransferVerificationActivity extends AppCompatActivity {

    private ActivityBankTransferVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_verification);

        binding = ActivityBankTransferVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String total = getIntent().getStringExtra("total");
        binding.layoutTotalPayment.tvTotal.setText(total);
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());

        getBank();
    }

    private void getBank() {
        String bank = getIntent().getStringExtra("bank");
        String bankName = bank;
        int bankLogo = 0;
        if(bank.equals("BNI")){
            bankName = getString(R.string.str_bni_mobile_banking);
            bankLogo = R.drawable.ic_bni;

        }else if(bank.equals("CIMB")){
            bankName = getString(R.string.str_cimb);
            bankLogo = R.drawable.ic_cimb_niaga;
        }
        binding.tvBankName.setText(bankName);
        binding.ivBankLogo.setImageResource(bankLogo);
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