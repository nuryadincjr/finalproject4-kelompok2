package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_CITIES;
import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Cities;
import com.nuryadincjr.ebusantara.pojo.Schedule;

import java.util.ArrayList;

public class CitiesRepository {
    private final CollectionReference collection;
    private final FirebaseFirestore db;

    public CitiesRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_CITIES);
    }

    public MutableLiveData<ArrayList<Cities>> getCities() {
        ArrayList<Cities> Schedule = new ArrayList<>();
        final MutableLiveData<ArrayList<Cities>> ScheduleMutableLiveData = new MutableLiveData<>();

        collection.orderBy("city")
                .limit(1000)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot  snapshot : task.getResult()) {
                    Cities data = snapshot.toObject(Cities.class);
                    Schedule.add(data);
                }
                ScheduleMutableLiveData.postValue(Schedule);
            }else ScheduleMutableLiveData.setValue(null);
        });
        return ScheduleMutableLiveData;
    }

    public MutableLiveData<ArrayList<Cities>> getCities(String field, String value) {
        ArrayList<Cities> citiesArrayList = new ArrayList<>();
        final MutableLiveData<ArrayList<Cities>> citiesListMutableLiveData = new MutableLiveData<>();

        collection.orderBy("city")
                .limit(1000)
                .whereGreaterThanOrEqualTo(field, value)
                .whereLessThanOrEqualTo(field,value+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Cities cities = document.toObject(Cities.class);
                    citiesArrayList.add(cities);
                }
                citiesListMutableLiveData.postValue(citiesArrayList);
            } else citiesListMutableLiveData.setValue(null);
        });
        return citiesListMutableLiveData;
    }

    public MutableLiveData<Cities> getCity(String reference) {
        MutableLiveData<Cities> listMutableLiveData = new MutableLiveData<>();
        db.document(reference)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Cities city = task.getResult().toObject(Cities.class);
                listMutableLiveData.setValue(city);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }
}
