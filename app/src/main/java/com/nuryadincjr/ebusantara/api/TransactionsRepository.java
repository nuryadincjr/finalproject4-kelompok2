package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_TRANSACTIONS;
import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Schedule;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Transactions;
import com.nuryadincjr.ebusantara.pojo.TransactionsReference;

import java.util.ArrayList;

public class TransactionsRepository {
    private final CollectionReference collection;
    private final FirebaseFirestore db;

    public TransactionsRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_TRANSACTIONS);
    }

    public MutableLiveData<ArrayList<TransactionsReference>> getTransactions(String value) {
        MutableLiveData<ArrayList<TransactionsReference>> transactionsMutableLiveData = new MutableLiveData<>();
        ArrayList<TransactionsReference> transactionsArrayList = new ArrayList<>();

        collection.whereEqualTo("uid", value)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().size()!=0) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Transactions transactions = document.toObject(Transactions.class);
                    db.document("schedule/"+transactions.getSchedule())
                            .get().addOnCompleteListener(scheduleTask->{
                       if(task.isSuccessful()){
                           Schedule schedule = scheduleTask.getResult().toObject(Schedule.class);
                           db.document(schedule.getDeparture().getPath()).get()
                                   .addOnCompleteListener(departureTask->{
                              if(departureTask.isSuccessful()){
                                  Cities departureCity = departureTask.getResult().toObject(Cities.class);
                                  db.document(schedule.getArrival().getPath()).get()
                                          .addOnCompleteListener(arrivalTask->{
                                      if(arrivalTask.isSuccessful()){
                                          getDocuments(transactionsMutableLiveData, transactionsArrayList,
                                                  transactions, schedule, departureCity, arrivalTask);
                                      }
                                  });
                              }
                           });
                       }
                    });
                }
            } else transactionsMutableLiveData.setValue(null);
        });
        return transactionsMutableLiveData;
    }

    private void getDocuments(MutableLiveData<ArrayList<TransactionsReference>> arrayListMutableLiveData,
                              ArrayList<TransactionsReference> transactionsArrayList, Transactions transactions,
                              Schedule schedule, Cities departureCity, Task<DocumentSnapshot> arrivalTask) {
        Cities arrivalCity = arrivalTask.getResult().toObject(Cities.class);
        db.document(schedule.getBus().getPath()).get()
                .addOnCompleteListener(busTask->{
            if(busTask.isSuccessful()){
                Buses bus = busTask.getResult().toObject(Buses.class);
                ScheduleReference reference = new ScheduleReference();
                reference.setId(schedule.getId());
                reference.setBuses(bus);
                reference.setDeparture(departureCity);
                reference.setArrival(arrivalCity);
                reference.setDepartureTime(schedule.getDepartureTime());
                reference.setArrivalTime(schedule.getArrivalTime());

                TransactionsReference transactionsReference = new TransactionsReference();
                transactionsReference.setReference(reference);
                transactionsReference.setTransactions(transactions);

                transactionsArrayList.add(transactionsReference);
                arrayListMutableLiveData.postValue(transactionsArrayList);
            }
        });
    }
}
