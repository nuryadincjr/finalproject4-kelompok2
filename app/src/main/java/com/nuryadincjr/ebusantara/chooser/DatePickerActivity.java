package com.nuryadincjr.ebusantara.chooser;

import static com.nuryadincjr.ebusantara.databinding.ActivityDatePickerBinding.inflate;

import android.content.Intent;
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

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        binding.tvSelectedDate.setText(dateFormat.format(calendar.getTime()));

        binding.ivBackArrow.setOnClickListener(v -> onBackPressed());
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            binding.tvSelectedDate.setText(dateFormat.format(calendar.getTime()));
        });

        binding.btnSelectedDate.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("date", calendar);
            setResult(RESULT_OK, intent);
            onBackPressed();
        });

    }


}