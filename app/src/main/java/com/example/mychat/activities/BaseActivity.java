package com.example.mychat.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mychat.constants.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;
    String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                .document(preferences.getString(Constants.KEY_USER_ID, ""));

    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }



    @Override
    protected void onResume() {
        super.onResume();

        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }
}
