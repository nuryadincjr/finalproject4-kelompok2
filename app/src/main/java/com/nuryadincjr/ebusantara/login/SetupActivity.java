package com.nuryadincjr.ebusantara.login;

import static com.nuryadincjr.ebusantara.databinding.ActivitySetupBinding.inflate;
import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.api.UsersRepository;
import com.nuryadincjr.ebusantara.databinding.ActivitySetupBinding;
import com.nuryadincjr.ebusantara.pojo.Users;

public class SetupActivity extends AppCompatActivity {
    private ActivitySetupBinding binding;
    private ProgressDialog dialog;

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

            Users user = getIntent().getParcelableExtra("user");
            user.setPhoneNumber(phoneNumber);

            new UsersRepository().insertUser(user).addOnSuccessListener(documentReference -> {
              getInstance(this).getEditor()
                        .putString("uid", user.getUid())
                        .putString("name", user.getName())
                        .putString("email", user.getEmail())
                        .putString("photo", user.getPhotoUrl())
                        .putString("phone", user.getPhoneNumber()).apply();

                startActivity(new Intent(this, FinishRegistrationActivity.class)
                        .putExtra("user", user));
                dialog.dismiss();
                finishAffinity();
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Snackbar.make(binding.getRoot(),
                        "Error adding document.",
                        Snackbar.LENGTH_SHORT).show();
            });

        }else{
            dialog.dismiss();
            Snackbar.make(binding.getRoot(),
                    "Please enter your phone number",
                    Snackbar.LENGTH_SHORT).show();
        }
    }
}
