package com.nuryadincjr.ebusantara;

import static com.nuryadincjr.ebusantara.databinding.ActivityMainBinding.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.nuryadincjr.ebusantara.databinding.ActivityMainBinding;
import com.nuryadincjr.ebusantara.fragment.NotificationsFragment;
import com.nuryadincjr.ebusantara.fragment.ProfileFragment;
import com.nuryadincjr.ebusantara.fragment.SearchFragment;
import com.nuryadincjr.ebusantara.fragment.TicketsFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(this);

        if(savedInstanceState == null){
            getFragmentPage(new SearchFragment());
        }


        binding.getRoot().setFadeOnClickListener(view ->
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));

        binding.layoutSlidingUp.btnCancel.setOnClickListener(v ->
                binding.getRoot().setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit();
        }
    }
}