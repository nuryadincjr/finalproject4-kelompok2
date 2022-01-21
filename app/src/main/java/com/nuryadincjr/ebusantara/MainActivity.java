package com.nuryadincjr.ebusantara;

import static com.nuryadincjr.ebusantara.databinding.ActivityMainBinding.inflate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.nuryadincjr.ebusantara.databinding.ActivityMainBinding;
import com.nuryadincjr.ebusantara.fragment.NotificationsFragment;
import com.nuryadincjr.ebusantara.fragment.ProfileFragment;
import com.nuryadincjr.ebusantara.fragment.SearchFragment;
import com.nuryadincjr.ebusantara.fragment.TicketsFragment;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private Fragment fragment;
    private ScheduleReference schedule;
    private Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(this);
        schedule = getIntent().getParcelableExtra("schedule");
        transactions = getIntent().getParcelableExtra("transactions");
        int fragment = getIntent().getIntExtra("fragment", 0);

        if(savedInstanceState == null) this.fragment = new SearchFragment();
        if(fragment==1) {
            this.fragment = new TicketsFragment();
            View view = binding.bottomNavigationView.findViewById(R.id.itemMyTicket);
            view.performClick();
        }
        getFragmentPage(this.fragment);

        binding.layoutSlidingUp.btnSelectedDate.setOnClickListener(v -> {
            TextView tvPassengers = findViewById(R.id.tvPassengers);
            String passenger = binding.layoutSlidingUp.tvPassenger.getText() +" ppl";
            tvPassengers.setText(passenger);
            binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });

        binding.getRoot().setFadeOnClickListener(view ->
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));


        binding.layoutSlidingUp.btnCancel.setOnClickListener(v ->
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));

        binding.layoutSlidingUp.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.layoutSlidingUp.tvPassenger.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSearch:
                fragment = new SearchFragment();
                break;
            case R.id.itemMyTicket:
                fragment = new TicketsFragment();
                break;
            case R.id.itemNotifications:
                fragment = new NotificationsFragment();
                break;
            case R.id.itemProfile:
                fragment = new ProfileFragment();
                break;
        }

        getFragmentPage(fragment);
        return true;
    }

    public void getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("schedule", schedule);
            bundle.putParcelable("transactions", transactions);
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit();
        }
    }
}