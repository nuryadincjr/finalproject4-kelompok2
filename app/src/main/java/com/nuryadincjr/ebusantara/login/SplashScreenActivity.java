package com.nuryadincjr.ebusantara.login;

import static com.nuryadincjr.ebusantara.databinding.ActivitySplashScreenBinding.inflate;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.BuildConfig;
import com.nuryadincjr.ebusantara.MainActivity;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivitySplashScreenBinding;
import com.nuryadincjr.ebusantara.pojo.Users;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        String versionName = "Version "+BuildConfig.VERSION_NAME;
        binding.tvVersion.setText(versionName);
        transition();
    }

    private void isLogin(){
        if(currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.document("users/"+currentUser.getUid()).get().addOnCompleteListener(task -> {
                if(task.getResult().getData() != null){
                    Users users = task.getResult().toObject(Users.class);
                    startActivity(new Intent(this, MainActivity.class)
                            .putExtra("user", users));
                }else {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
    private void transition() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> isLogin(), 3000);
    }
}