package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_BUSES;
import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_CITIES;
import static java.util.Objects.requireNonNull;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.Cities;

import java.util.ArrayList;

public class BusesRepository {
    private final CollectionReference collection;
    private final FirebaseFirestore db;

    public BusesRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_BUSES);
    }

    public MutableLiveData<Buses> getBuses(String reference) {
        MutableLiveData<Buses> listMutableLiveData = new MutableLiveData<>();
        db.document(reference)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Buses buses = task.getResult().toObject(Buses.class);
                listMutableLiveData.setValue(buses);
            } else listMutableLiveData.setValue(null);
        });
        return listMutableLiveData;
    }
}
