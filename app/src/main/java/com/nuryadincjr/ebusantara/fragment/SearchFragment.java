package com.nuryadincjr.ebusantara.fragment;

import static android.app.Activity.RESULT_OK;
import static com.nuryadincjr.ebusantara.databinding.FragmentSearchBinding.inflate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.chooser.DatePickerActivity;
import com.nuryadincjr.ebusantara.chooser.DestinationChooserActivity;
import com.nuryadincjr.ebusantara.databinding.FragmentSearchBinding;
import com.nuryadincjr.ebusantara.dataview.BusChooserActivity;
import com.nuryadincjr.ebusantara.models.Cities;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchFragment extends Fragment
        implements View.OnClickListener {

    private FragmentSearchBinding binding;
    private SlidingUpPanelLayout sliding;
    private Cities departureCity;
    private Cities arrivalCity;
    private Calendar date;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sliding = requireActivity().findViewById(R.id.sliding_layout);
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

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        onGetListener(v);
    }

    @SuppressLint("NonConstantResourceId")
    private void onGetListener(View v) {
        Intent intent = new Intent(getContext(), DestinationChooserActivity.class);
        switch (v.getId()){
            case R.id.llDeparture:
                startActivityForResult(intent, 1);
                break;
            case R.id.llArrival:
                startActivityForResult(intent, 2);
                break;
            case R.id.llPassengers:
                sliding.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.llDate:
                startActivityForResult(new Intent(getContext(), DatePickerActivity.class), 3);
                break;
            case R.id.btnSearchBus:
                startActivity(new Intent(getContext(), BusChooserActivity.class)
                        .putExtra("departureCity", departureCity)
                        .putExtra("arrivalCity", arrivalCity)
                        .putExtra("date", date)
                        .putExtra("passengers", binding.tvPassengers.getText()
                                .toString().replace(" ppl", "")));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if (data != null && resultCode == RESULT_OK) {
                    departureCity = data.getParcelableExtra("city");
                    binding.tvDeparture.setText(departureCity.getCity());
                }
                break;
            case 2:
                if (data != null && resultCode == RESULT_OK) {
                    arrivalCity = data.getParcelableExtra("city");
                    binding.tvArrival.setText(arrivalCity.getCity());
                }
                break;
            case 3:
                if (data != null && resultCode == RESULT_OK) {
                    date = (Calendar) data.getSerializableExtra("date");
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM");
                    binding.tvDate.setText(format.format(date.getTime()));
                }
                break;
        }
    }
}