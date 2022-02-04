package com.nuryadincjr.ebusantara.payment;

import static com.nuryadincjr.ebusantara.databinding.ActivityBankTransferBinding.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityBankTransferBinding;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;

public class BankTransferActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        ActivityBankTransferBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Transactions transactions = getIntent().getParcelableExtra("transactions");

        binding.layoutTotalPayment.tvTotal.setText(transactions.getTotalPayment());
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.llBankBni.setOnClickListener(this);
        binding.llBankCimb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = "";
        if(v.getId()==R.id.llBankBni){
            item = "BNI";
        }else if(v.getId()==R.id.llBankCimb){
            item = "CIMB";
        }

        if(!item.equals("")) {
            Transactions transactions = getIntent().getParcelableExtra("transactions");
            ScheduleReference schedule = getIntent().getParcelableExtra("schedule");
            startActivity(new Intent(this,
                    BankTransferVerificationActivity.class)
                    .putExtra("bank", item)
                    .putExtra("transactions", transactions)
                    .putExtra("schedule", schedule));
        }
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