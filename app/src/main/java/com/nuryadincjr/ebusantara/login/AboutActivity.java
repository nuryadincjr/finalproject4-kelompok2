package com.nuryadincjr.ebusantara.login;

import static android.content.Intent.*;
import static com.nuryadincjr.ebusantara.databinding.ActivityAboutBinding.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActivityAboutBinding binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getPortfolio(binding.tvPortofolo1, getString(R.string.str_portofoliourl1));
        getPortfolio(binding.tvPortofolo2, getString(R.string.str_portofoliourl2));
        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
    }

    private void getPortfolio(TextView view, String url) {
        view.setOnClickListener(v -> startActivity(new Intent(ACTION_VIEW)
                .setData(Uri.parse(url))));
    }
}