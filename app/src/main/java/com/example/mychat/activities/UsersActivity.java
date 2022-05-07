package com.example.mychat.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mychat.adapters.UserAdapter;
import com.example.mychat.constants.Constants;
import com.example.mychat.databinding.ActivityUsersBinding;
import com.example.mychat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    ActivityUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getUsers();
        setListeners();


    }

    private void setListeners()
    {
        binding.imageback.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    private void getUsers()
    {
        Loading(true);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constants.KEY_COLLECTION_USER)
                .get().addOnCompleteListener(task -> {
                    Loading(false);
                    String userId = FirebaseAuth.getInstance().getUid();
                    if(task.isSuccessful() && task.getResult() != null)
                    {
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult())
                        {
                            if(userId.equals(queryDocumentSnapshot.getId()))
                            {
                                continue;

                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.profileImage = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            users.add(user);
                        }
                        if(users.size() > 0)
                        {
                            UserAdapter userAdapter = new UserAdapter(users);
                            binding.usersRecyclerView.setAdapter(userAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            showErrorMessage();
                        }
                    }
                    else
                    {
                        showErrorMessage();
                    }
        });
    }
    private void showErrorMessage()
    {
        binding.textErrorMessage.setText(String.format("%s", "No User Available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
    private void Loading(boolean isLoading)
    {
        if(isLoading)
        {
            binding.progressBar.setVisibility(View.VISIBLE);

        }
        else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}