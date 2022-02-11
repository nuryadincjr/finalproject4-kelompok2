package com.nuryadincjr.ebusantara.fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.nuryadincjr.ebusantara.util.Constant.getUsers;
import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.adapters.TicketsAdapter;
import com.nuryadincjr.ebusantara.api.ReviewsRepository;
import com.nuryadincjr.ebusantara.databinding.FragmentTicketsBinding;
import com.nuryadincjr.ebusantara.dataview.TicketDetailsActivity;
import com.nuryadincjr.ebusantara.interfaces.ItemClickListener;
import com.nuryadincjr.ebusantara.pojo.Reviewers;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;
import com.nuryadincjr.ebusantara.pojo.Users;
import com.nuryadincjr.ebusantara.util.MainViewModel;

import java.util.ArrayList;

public class TicketsFragment extends Fragment {
    private FragmentTicketsBinding binding;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTicketsBinding.inflate(inflater, container, false);
        Users users = getUsers(getContext());

        ScheduleReference schedule = getArguments().getParcelable("schedule");
        Transactions transactions = getArguments().getParcelable("transactions");
        boolean isRating = getInstance(getContext())
                .getPreferences()
                .getBoolean("isRating", false);
        if(isRating) getPopup(schedule, transactions, users);

        getData(users);
        return binding.getRoot();
    }

    private void getData(Users users) {
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getTransactions(users.getUid()).observe(getViewLifecycleOwner(), ticket -> {
            if(ticket != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sort(ticket, (o1, o2) -> o2.getTransactions().getDate()
                            .compareTo(o1.getTransactions().getDate()));
                }

                TicketsAdapter citiesAdapter = new TicketsAdapter(ticket);
                binding.rvTickets.setVisibility(VISIBLE);
                binding.layoutError.linearLayout.setVisibility(GONE);
                binding.rvTickets.showShimmerAdapter();
                binding.rvTickets.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvTickets.setAdapter(citiesAdapter);

                onListener(citiesAdapter, ticket, users);
            }else {
                binding.rvTickets.setVisibility(GONE);
                binding.layoutError.linearLayout.setVisibility(VISIBLE);
            }
        });
    }

    @SuppressLint("InflateParams")
    public void getPopup(ScheduleReference schedule, Transactions transactions, Users users) {
        View layoutRating = getLayoutInflater().inflate(R.layout.layout_rating, null);
        TextView poName = layoutRating.findViewById(R.id.tvPOName);
        TextView busNo = layoutRating.findViewById(R.id.tvBusNo);
        TextView departureDate = layoutRating.findViewById(R.id.tvDepartureDate);
        TextView status = layoutRating.findViewById(R.id.tvStatus);
        TextView maxLine = layoutRating.findViewById(R.id.tvMaxLine);
        EditText content = layoutRating.findViewById(R.id.etContent);
        CheckedTextView star1 = layoutRating.findViewById(R.id.ctvStar1);
        CheckedTextView star2 = layoutRating.findViewById(R.id.ctvStar2);
        CheckedTextView star3 = layoutRating.findViewById(R.id.ctvStar3);
        CheckedTextView star4 = layoutRating.findViewById(R.id.ctvStar4);
        CheckedTextView star5 = layoutRating.findViewById(R.id.ctvStar5);
        String id = schedule.getBuses().getId();

        poName.setText(schedule.getBuses().getPoName());
        busNo.setText(schedule.getBuses().getBusNo());
        departureDate.setText(schedule.getDepartureTime());
        status.setText(transactions.getStatus());

        ReviewsRepository repository = new ReviewsRepository();
        repository.getReviewers(id, users).observe(getViewLifecycleOwner(), reviewers -> {
            if(reviewers==null) {
                onShowPopup(transactions, users, layoutRating, maxLine,
                        content, star1, star2, star3, star4, star5, id, repository);
            }

            getInstance(getContext()).getEditor()
                    .putBoolean("isRating", false)
                    .apply();
        });
    }

    private void onShowPopup(Transactions transactions, Users users, View inflatedView,
                             TextView maxLine, EditText content, CheckedTextView star1,
                             CheckedTextView star2, CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, String id, ReviewsRepository repository) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(inflatedView);
        builder.setCancelable(false);

        star1.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                !star1.isChecked(), false, false, false, false));
        star2.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, false, false, false));
        star3.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, false, false));
        star4.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, false));
        star5.setOnClickListener(v -> getSelected(star1, star2, star3, star4, star5,
                true, true, true, true, true));

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count<=500){
                    String indicator = s.length()+"/500";
                    maxLine.setText(indicator);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton("Rate This Trip", (dialog, which) -> {
            int rating = 0;
            if(star1.isChecked() && star2.isChecked() && star3.isChecked() &&
                    star4.isChecked() && star5.isChecked()) rating =5;
            else if(star1.isChecked() && star2.isChecked() &&
                    star3.isChecked() && star4.isChecked()) rating =4;
            else if(star1.isChecked() && star2.isChecked() && star3.isChecked()) rating =3;
            else if(star1.isChecked() && star2.isChecked()) rating =2;
            else if(star1.isChecked()) rating =1;

            int finalRating = rating;
            Reviewers postReview = new Reviewers(users.getUid(), transactions.getDate(),
                    content.getText().toString(), valueOf(finalRating));

            repository.updateReview(id, postReview);
        });

        builder.show();
    }

    private void getSelected(CheckedTextView star1, CheckedTextView star2,
                             CheckedTextView star3, CheckedTextView star4,
                             CheckedTextView star5, boolean isChecked1, boolean isChecked2,
                             boolean isChecked3, boolean isChecked4, boolean isChecked5) {
        star1.setChecked(isChecked1);
        star2.setChecked(isChecked2);
        star3.setChecked(isChecked3);
        star4.setChecked(isChecked4);
        star5.setChecked(isChecked5);
    }

    private void onListener(TicketsAdapter productsAdapter,
                            ArrayList<TransactionsReference> ticket, Users users) {
        productsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
               startActivity(new Intent(getContext(), TicketDetailsActivity.class)
                       .putExtra("transaction", ticket.get(position))
                       .putExtra("user", users));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
}