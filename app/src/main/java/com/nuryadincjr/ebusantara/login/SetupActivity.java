package com.nuryadincjr.ebusantara.login;

import static android.content.ContentValues.TAG;
import static com.nuryadincjr.ebusantara.databinding.ActivitySetupBinding.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.api.UsersRepository;
import com.nuryadincjr.ebusantara.databinding.ActivitySetupBinding;
import com.nuryadincjr.ebusantara.pojo.Users;

public class SetupActivity extends AppCompatActivity {

    private ActivitySetupBinding binding;
    private ProgressDialog dialog;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);

        binding.btnSaveNumber.setOnClickListener(v -> finishSetup());
    }

    private void finishSetup() {
        String phoneNumber = String.valueOf(binding.etPhoneNumber.getText());
        if(!phoneNumber.isEmpty()){
            dialog.setMessage("currently preparing your profile..");
            dialog.setCancelable(false);
            dialog.show();

            user = getIntent().getParcelableExtra("user");
            user.setPhoneNumber(phoneNumber);

            new UsersRepository().insertUser(user).addOnSuccessListener(documentReference -> {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);
                Toast.makeText(getApplicationContext(), "Success.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, FinishRegistrationActivity.class));

                finishAffinity();
            }).addOnFailureListener(e -> {
                Log.w(TAG, "Error adding document", e);
                Toast.makeText(getApplicationContext(), "Error adding document.", Toast.LENGTH_SHORT).show();
            });
            dialog.dismiss();
        }
    }
}
