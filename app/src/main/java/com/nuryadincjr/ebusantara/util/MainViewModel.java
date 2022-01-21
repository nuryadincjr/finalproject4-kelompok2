package com.nuryadincjr.ebusantara.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nuryadincjr.ebusantara.api.CitiesRepository;
import com.nuryadincjr.ebusantara.api.ScheduleRepository;
import com.nuryadincjr.ebusantara.api.TransactionsRepository;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;

import java.util.ArrayList;
import java.util.Calendar;

public class MainViewModel extends AndroidViewModel {

    private final ScheduleRepository scheduleRepository;
    private final CitiesRepository citiesRepository;
    private final TransactionsRepository transactionsRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.citiesRepository = new CitiesRepository();
        this.scheduleRepository = new ScheduleRepository();
        this.transactionsRepository = new TransactionsRepository();
    }

    public MutableLiveData<ArrayList<Cities>> getCities(String collection) {
        return scheduleRepository.getCollectionCities(collection);
    }

    public MutableLiveData<ArrayList<TransactionsReference>> getTransactions(String collection) {
        return transactionsRepository.getTransactions(collection);
    }

    public MutableLiveData<ArrayList<Cities>> getSearchCities(String collection) {
        return citiesRepository.getSearchCities(collection);
    }

    public MutableLiveData<ArrayList<ScheduleReference>> getBuses(
            String departureCity, String arrivalCity, Calendar calendar) {
        return scheduleRepository.getCollectionsBuses(departureCity, arrivalCity, calendar);
    }
}
