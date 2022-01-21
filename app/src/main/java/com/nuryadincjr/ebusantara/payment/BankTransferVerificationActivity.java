package com.nuryadincjr.ebusantara.payment;

import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.MainActivity;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityBankTransferVerificationBinding;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;

public class BankTransferVerificationActivity extends AppCompatActivity {

    private ActivityBankTransferVerificationBinding binding;
    private Transactions transactions;
    private String bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_verification);

        binding = ActivityBankTransferVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactions = getIntent().getParcelableExtra("transactions");
        bank = getIntent().getStringExtra("bank");

        binding.layoutTotalPayment.tvTotal.setText(transactions.getTotalPayment());
        binding.tvPaymentNumber.setText(transactions.getBookNo());
        binding.tvPaymentNominal.setText(transactions.getTotalPayment());
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.btnVerifyPayment.setOnClickListener(v -> onPusData());

        getBank();
    }

    private void onPusData() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Verify payment..");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("transactions").document().getId();
        ScheduleReference schedule = getIntent().getParcelableExtra("schedule");

        transactions.setId(id);
        transactions.setPaymentMethod("credit card");
        transactions.setPaymentPartner("visa");

        db.collection("seats").document(schedule.getId())
                .set(schedule.getBuses().getSeats())
                .addOnCompleteListener(updateTask ->{
                    if(updateTask.isSuccessful()){
                        db.collection("transactions")
                                .document(id).set(transactions)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        dialog.dismiss();
                                        getInstance(this).getEditor().putBoolean("isRating", true).apply();
                                        startActivity(new Intent(this, MainActivity.class)
                                                .putExtra("schedule", schedule)
                                                .putExtra("transactions", transactions)
                                                .putExtra("fragment", 1));
                                        finish();
                                    }
                                });
                    }
                });
    }

    private void getBank() {
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