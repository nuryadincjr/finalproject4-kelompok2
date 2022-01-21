package com.nuryadincjr.ebusantara.api;

import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Cities;

import java.util.ArrayList;

public class CitiesRepository {
    private static final String COLLECTION_CITIES = "cities";
    private final CollectionReference collectionReference;

    public CitiesRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_CITIES);
    }

    public MutableLiveData<ArrayList<Cities>> getSearchCities(String value) {
        ArrayList<Cities> citiesArrayList = new ArrayList<>();
        final MutableLiveData<ArrayList<Cities>> productsMutableLiveData = new MutableLiveData<>();

        collectionReference
                .whereGreaterThanOrEqualTo("city", value)
                .whereLessThanOrEqualTo("city",value+"~")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                    Cities cities = document.toObject(Cities.class);
                    citiesArrayList.add(cities);
                }
                productsMutableLiveData.postValue(citiesArrayList);
            } else productsMutableLiveData.setValue(null);
        });
        return productsMutableLiveData;
    }
}
