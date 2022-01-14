package com.nuryadincjr.ebusantara.api;

import static android.content.ContentValues.TAG;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nuryadincjr.ebusantara.models.Cities;
import com.nuryadincjr.ebusantara.models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository {

    private FirebaseFirestore db;

    public ScheduleRepository() {
         db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<ArrayList<Schedule>> getCollectionsBuses(String collection, String departureCity, String arrivalCity) {
        ArrayList<Schedule> Schedule = new ArrayList<>();
        final MutableLiveData<ArrayList<Schedule>> ScheduleMutableLiveData = new MutableLiveData<>();

        CollectionReference collectionReference = db.collection(collection);

        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Schedule data = snapshot.toObject(Schedule.class);
                    db.document("cities/"+data.getDeparture()).get().addOnCompleteListener(departureTask -> {
                       if(departureTask.isSuccessful()){
                           Cities departureCities = departureTask.getResult().toObject(Cities.class);
                           if(departureCities != null){
                               if(departureCities.getCity().equals(departureCity)){
                                   db.document("cities/"+data.getArrival()).get().addOnCompleteListener(arrivalTask -> {
                                       if(arrivalTask.isSuccessful()){
                                           Cities arrivalCities = arrivalTask.getResult().toObject(Cities.class);
                                           if(arrivalCities != null){
                                               if(arrivalCities.getCity().equals(arrivalCity)){
                                                   Log.d("XXX", snapshot.getId() + " => " + snapshot.getData());
                                                   Schedule.add(data);
                                                   ScheduleMutableLiveData.postValue(Schedule);
                                               }
                                           }
                                       }
                                   });
                               }
                           }
                       }
                    });

                }
            } else {
                ScheduleMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return ScheduleMutableLiveData;
    }

    public MutableLiveData<ArrayList<Cities>> getCollectionCities(String document) {
        ArrayList<Cities> Schedule = new ArrayList<>();
        final MutableLiveData<ArrayList<Cities>> ScheduleMutableLiveData = new MutableLiveData<>();

        CollectionReference collectionReference = db.collection(document);

        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Cities data = snapshot.toObject(Cities.class);

                    Schedule.add(data);
                    Log.d(TAG, snapshot.getId() + " => " + snapshot.getData());
                }
                ScheduleMutableLiveData.postValue(Schedule);
            }else{
                ScheduleMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return ScheduleMutableLiveData;
    }
}
