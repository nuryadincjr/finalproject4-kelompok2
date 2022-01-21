package com.nuryadincjr.ebusantara.payment;

import static com.nuryadincjr.ebusantara.databinding.ActivityRetailPaymentVerificationBinding.inflate;
import static com.nuryadincjr.ebusantara.util.Constant.getQrCode;
import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.MainActivity;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityRetailPaymentVerificationBinding;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;

public class RetailPaymentVerificationActivity extends AppCompatActivity {
    private Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_payment_verification);

        ActivityRetailPaymentVerificationBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactions = getIntent().getParcelableExtra("transactions");

        binding.layoutTotalPayment.tvTotal.setText(transactions.getTotalPayment());
        binding.tvPaymentNumber.setText(transactions.getBookNo());

        binding.tvPaymentNumber.setOnClickListener(this::onShowPopup);
        binding.btnVerifyPayment.setOnClickListener(v -> onPusData());
        binding.appbar.ivBackArrow.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("InflateParams")
    public void onShowPopup(View view) {
        View inflatedView = getLayoutInflater().inflate(R.layout.layout_qrcode, null);
        ImageView imageView = inflatedView.findViewById(R.id.ivQrCode);

        imageView.setImageBitmap(getQrCode(transactions.getBookNo()));
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(inflatedView).show();
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

        db.collection("seats")
                .document(schedule.getId())
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