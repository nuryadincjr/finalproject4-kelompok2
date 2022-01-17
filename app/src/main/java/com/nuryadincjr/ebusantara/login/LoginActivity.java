package com.nuryadincjr.ebusantara.login;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN;
import static com.nuryadincjr.ebusantara.databinding.ActivityLoginBinding.inflate;
import static java.lang.String.valueOf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nuryadincjr.ebusantara.MainActivity;
import com.nuryadincjr.ebusantara.R;
import com.nuryadincjr.ebusantara.databinding.ActivityLoginBinding;
import com.nuryadincjr.ebusantara.pojo.Users;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    private ActivityLoginBinding binding;
    private GoogleSignInClient signInClient;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Setup..");
        dialog.setCancelable(false);

        GoogleSignInOptions gso = new Builder(DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        signInClient = GoogleSignIn.getClient(this, gso);
        binding.btnSignUp.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            dialog.show();
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if(account != null){
                firebaseAuthWithGoogle(account.getIdToken());
            } else Log.d(TAG, "no id token");
        } catch (ApiException e) {
            dialog.dismiss();
            signInClient.signOut();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                        signInClient.signOut();
                    } else {
                        dialog.dismiss();
                        signInClient.signOut();
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(binding.getRoot(), "Authentication Failed.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document("users/"+currentUser.getUid()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().getData() != null){
                    dialog.dismiss();
                    Users users = task.getResult().toObject(Users.class);
                    startActivity(new Intent(this, MainActivity.class)
                        .putExtra("user", users));
                    finish();
                }else {
                    dialog.dismiss();
                    Users user = new Users(currentUser.getUid(), currentUser.getDisplayName(),
                            "", currentUser.getEmail(), valueOf(currentUser.getPhotoUrl()));
                    startActivity(new Intent(this, SetupActivity.class)
                            .putExtra("user", user));
                }
            }
        });
    }
}