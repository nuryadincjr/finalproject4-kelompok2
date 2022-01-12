package com.nuryadincjr.ebusantara.chooser;

import static com.nuryadincjr.ebusantara.databinding.ActivityDatePickerBinding.inflate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityDatePickerBinding;

import java.text.DateFormat;
import java.util.Calendar;

public class DatePickerActivity extends AppCompatActivity {

    private ActivityDatePickerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
            binding.tvSelectedDate.setText(dateFormat.format(calendar.getTime()));
        });

    }
}