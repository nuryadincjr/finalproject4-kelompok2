package com.nuryadincjr.ebusantara.fragment;

import static android.app.Activity.RESULT_OK;
import static com.nuryadincjr.ebusantara.databinding.FragmentSearchBinding.inflate;
import static com.nuryadincjr.ebusantara.util.Constant.toUpperCase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.chooser.DatePickerActivity;
import com.nuryadincjr.ebusantara.chooser.DestinationChooserActivity;
import com.nuryadincjr.ebusantara.databinding.FragmentSearchBinding;
import com.nuryadincjr.ebusantara.dataview.BusChooserActivity;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchFragment extends Fragment
        implements View.OnClickListener {
    private FragmentSearchBinding binding;
    private SlidingUpPanelLayout sliding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar calendar;
    private SimpleDateFormat format;

    public SearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        sliding = requireActivity().findViewById(R.id.sliding_layout);
        format = new SimpleDateFormat("dd MMM");
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);

        binding.llDeparture.setOnClickListener(this);
        binding.llArrival.setOnClickListener(this);
        binding.llPassengers.setOnClickListener(this);
        binding.llDate.setOnClickListener(this);
        binding.btnSearchBus.setOnClickListener(this);

        if(savedInstanceState != null) {
            onStateData(savedInstanceState);
        }

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("departure_city", departureCity);
        outState.putParcelable("arrival_city", arrivalCity);
        outState.putSerializable("date", calendar);

        outState.putString("passengers", binding.tvPassengers.getText()
                .toString().replace(" ppl", ""));
        super.onSaveInstanceState(outState);
    }

    private void onStateData(Bundle savedInstanceState) {
        departureCity = savedInstanceState.getParcelable("departure_city");
        arrivalCity = savedInstanceState.getParcelable("arrival_city");
        calendar = (Calendar) savedInstanceState.getSerializable("date");
        String passengers = savedInstanceState.getString("passengers");

        if(departureCity!=null) binding.tvDeparture.setText(departureCity.getCity());
        if(arrivalCity!=null)binding.tvArrival.setText(arrivalCity.getCity());
        if(calendar!=null) binding.tvDate.setText(format.format(calendar.getTime()));

        binding.tvPassengers.setText(passengers);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), DestinationChooserActivity.class);
        switch (v.getId()){
            case R.id.llDeparture:
                intent.putExtra("hint", binding.tvDeparture.getText().toString());
                startActivityForResult(intent, 1);
                break;
            case R.id.llArrival:
                intent.putExtra("hint", binding.tvArrival.getText().toString());
                startActivityForResult(intent, 2);
                break;
            case R.id.llPassengers:
                sliding = requireActivity().findViewById(R.id.sliding_layout);
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.llDate:
                startActivityForResult(new Intent(getContext(),
                        DatePickerActivity.class), 3);
                break;
            case R.id.btnSearchBus:
                onStartActivity();
                break;
        }
    }

    private void onStartActivity() {
        boolean isDeparture = binding.tvDeparture.getText().equals(getString(R.string.str_try_departure));
        boolean isArrival = binding.tvArrival.getText().equals(getString(R.string.str_try_arrival));
        boolean isDate = binding.tvDate.getText().equals(getString(R.string.str_select_date));

        if(!isDeparture && !isArrival && !isDate){
            startActivity(new Intent(getContext(), BusChooserActivity.class)
                    .putExtra("departure_city", departureCity)
                    .putExtra("arrival_city", arrivalCity)
                    .putExtra("date", calendar)
                    .putExtra("passengers", binding.tvPassengers.getText()
                            .toString().replace(" ppl", "")));

        }else {
            getBoolean(isDate, "Please select a date");
            getBoolean(isDeparture, "Please select the departure city");
            getBoolean(isArrival, "Please select the arrival city");
            getBoolean(isDeparture && isArrival && isDate,
                    "please fill in the data completely");
        }
    }

    private void getBoolean(boolean isDeparture, String message) {
        if (isDeparture) {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (data != null && resultCode == RESULT_OK) {
                    departureCity = data.getParcelableExtra("city");
                    binding.tvDeparture.setText(toUpperCase(departureCity.getCity()));
                }
                break;
            case 2:
                if (data != null && resultCode == RESULT_OK) {
                    arrivalCity = data.getParcelableExtra("city");
                    binding.tvArrival.setText(toUpperCase(arrivalCity.getCity()));
                }
                break;
            case 3:
                if (data != null && resultCode == RESULT_OK) {
                    calendar = (Calendar) data.getSerializableExtra("date");
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM");
                    binding.tvDate.setText(format.format(calendar.getTime()));
                }
                break;
        }
    }
}