package com.nuryadincjr.ebusantara.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityBankTransferBinding;
import com.nuryadincjr.ebusantara.databinding.LayoutTotalPaymentBinding;

public class BankTransferActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityBankTransferBinding binding;
    private LayoutTotalPaymentBinding view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        binding = ActivityBankTransferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        view = binding.layoutTotalPayment;
        String total = getIntent().getStringExtra("total");
        view.tvTotal.setText(total);
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.tvSelectBNI.setOnClickListener(this);
        binding.tvSelectCIMB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = "";
        if(v.getId()==R.id.tvSelectBNI){
            item = "BNI";
        }else if(v.getId()==R.id.tvSelectCIMB){
            item = "CIMB";
        }
        if(!item.equals("")) {
            startActivity(new Intent(this,
                    BankTransferVerificationActivity.class)
                    .putExtra("bank", item)
                    .putExtra("total", view.tvTotal.getText()));
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