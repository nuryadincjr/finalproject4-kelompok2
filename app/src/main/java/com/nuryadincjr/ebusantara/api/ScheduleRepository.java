package com.nuryadincjr.ebusantara.api;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.models.Buses;
import com.nuryadincjr.ebusantara.models.Cities;
import com.nuryadincjr.ebusantara.models.Schedule;
import com.nuryadincjr.ebusantara.models.ScheduleReference;
import com.nuryadincjr.ebusantara.models.Seats;
import com.nuryadincjr.ebusantara.models.Users;

import java.util.ArrayList;

public class ScheduleRepository {

    private final FirebaseFirestore db;

    public ScheduleRepository() {
         db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<ArrayList<ScheduleReference>> getCollectionsBuses(
            String collection, String departureCity, String arrivalCity) {

        MutableLiveData<ArrayList<ScheduleReference>> scheduleMutableLiveData = new MutableLiveData<>();
        ArrayList<ScheduleReference> scheduleReferences = new ArrayList<>();

        CollectionReference collectionReference = db.collection(collection);
        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Schedule data = snapshot.toObject(Schedule.class);
                    db.document(data.getDeparture().getPath()).get().addOnCompleteListener(departureTask -> {
                       if(departureTask.isSuccessful()){
                           Cities departureCities = departureTask.getResult().toObject(Cities.class);
                           if(departureCities != null){
                               if(departureCities.getCity().equals(departureCity)){
                                   db.document(data.getArrival().getPath()).get().addOnCompleteListener(arrivalTask -> {
                                       if(arrivalTask.isSuccessful()){
                                           Cities arrivalCities = arrivalTask.getResult().toObject(Cities.class);
                                           if(arrivalCities != null){
                                               if(arrivalCities.getCity().equals(arrivalCity)){
                                                   db.document(data.getBus().getPath()).get().addOnCompleteListener(busTask -> {
                                                      if(busTask.isSuccessful()){
                                                          Buses buses = busTask.getResult().toObject(Buses.class);
                                                          ScheduleReference scheduleReference = new ScheduleReference();
                                                          if(buses != null){
                                                              scheduleReference.setBuses(buses);
                                                              scheduleReference.setId(data.getId());
                                                              scheduleReference.setDeparture(departureCities);
                                                              scheduleReference.setArrival(arrivalCities);
                                                              scheduleReference.setDepartureTime(data.getDepartureTime());
                                                              scheduleReference.setArrivalTime(data.getArrivalTime());
                                                          }
                                                          scheduleReferences.add(scheduleReference);
                                                          scheduleMutableLiveData.postValue(scheduleReferences);
                                                      }
                                                   });
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
                scheduleMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return scheduleMutableLiveData;
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

    public MutableLiveData<ArrayList<Users>> getUsers(String document) {
        ArrayList<Users> users = new ArrayList<>();
        final MutableLiveData<ArrayList<Users>> ScheduleMutableLiveData = new MutableLiveData<>();

        CollectionReference collectionReference = db.collection(document);

        collectionReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Users data = snapshot.toObject(Users.class);

                    users.add(data);
                    Log.d(TAG, snapshot.getId() + " => " + snapshot.getData());
                }
                ScheduleMutableLiveData.postValue(users);
            }else{
                ScheduleMutableLiveData.setValue(null);
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        return ScheduleMutableLiveData;
    }

}
