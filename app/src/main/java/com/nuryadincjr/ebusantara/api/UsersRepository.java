package com.nuryadincjr.ebusantara.api;

import static com.nuryadincjr.ebusantara.util.Constant.COLLECTION_USER;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.pojo.Users;

public class UsersRepository {
    private final CollectionReference collection;

    public UsersRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collection = db.collection(COLLECTION_USER);
    }

    public Task<Void> insertUser(Users user) {
        return collection.document(user.getUid()).set(user);
    }
}
