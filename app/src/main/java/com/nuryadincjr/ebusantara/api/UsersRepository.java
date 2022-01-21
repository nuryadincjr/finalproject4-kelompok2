package com.nuryadincjr.ebusantara.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.util.concurrent.atomic.AtomicReference;

public class UsersRepository {
    private static final String COLLECTION_USER = "users";
    private final CollectionReference collectionReference;

    public UsersRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_USER);
    }

    public Task<Void> insertUser(Users user) {
        return collectionReference.document(user.getUid()).set(user);
    }

    public Users getUser(String uid) {
        AtomicReference<Users> user = new AtomicReference<>(new Users());
        collectionReference.document(uid)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user.set(task.getResult().toObject(Users.class));
            }
        });
        return user.get();
    }
}
