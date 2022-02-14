package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_TRANSACTIONS;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Schedule;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;

import java.util.ArrayList;

public class TransactionsRepository {
    private final CollectionReference collection;
    private final ArrayList<TransactionsReference> transactionsArrayList;
    private final MutableLiveData<ArrayList<TransactionsReference>> transactionsMutableLiveData;

    public TransactionsRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_TRANSACTIONS);
        transactionsArrayList = new ArrayList<>();
        transactionsMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<TransactionsReference>> getTransactions(String value) {
        collection.whereEqualTo("uid", value)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().size()!=0) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Transactions transactions = snapshot.toObject(Transactions.class);
                    getSchedule(transactions);
                }
            } else transactionsMutableLiveData.setValue(null);
        });
        return transactionsMutableLiveData;
    }

    private void getSchedule(Transactions transactions) {
        new ScheduleRepository().getSchedule("schedule/"+ transactions.getSchedule())
                .observeForever(schedule -> getDepartureCity(transactions, schedule));
    }

    private void getDepartureCity(Transactions transactions, Schedule schedule) {
        new CitiesRepository().getCity(schedule.getDeparture().getPath())
                .observeForever(cities -> getArrivalCity(transactions, schedule, cities));
    }

    private void getArrivalCity(Transactions transactions,
                                Schedule schedule, Cities departureCity) {
        new CitiesRepository().getCity(schedule.getArrival().getPath())
                .observeForever(cities -> getBuses(transactions, schedule, departureCity, cities));
    }

    private void getBuses(Transactions transactions, Schedule schedule,
                          Cities departureCity, Cities arrivalCity) {
        new BusesRepository().getBuses(schedule.getBus().getPath()).observeForever(buses ->{
            ScheduleReference reference = new ScheduleReference();
            reference.setId(schedule.getId());
            reference.setBuses(buses);
            reference.setDeparture(departureCity);
            reference.setArrival(arrivalCity);
            reference.setDepartureTime(schedule.getDepartureTime());
            reference.setArrivalTime(schedule.getArrivalTime());

            TransactionsReference transactionsReference = new TransactionsReference();
            transactionsReference.setReference(reference);
            transactionsReference.setTransactions(transactions);

            transactionsArrayList.add(transactionsReference);
            transactionsMutableLiveData.postValue(transactionsArrayList);
        });
    }
}
