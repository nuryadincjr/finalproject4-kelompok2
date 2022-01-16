package com.nuryadincjr.ebusantara.login;

import static com.nuryadincjr.ebusantara.databinding.ActivitySplashScreenBinding.inflate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.BuildConfig;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.CitiesAdapter;
import com.nuryadincjr.ebusantara.databinding.ActivitySplashScreenBinding;
import com.nuryadincjr.ebusantara.models.MainViewModel;
import com.nuryadincjr.ebusantara.models.Seats;

import java.util.Arrays;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String versionName = "Version "+BuildConfig.VERSION_NAME;
        binding.tvVersion.setText(versionName);
        transition();
    }

    private void transition() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}