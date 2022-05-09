package com.example.mychat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mychat.R;
import com.example.mychat.constants.Constants;
import com.example.mychat.databinding.ActivityChatBinding;
import com.example.mychat.models.User;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        loadReceiverDetails();
        setListeners();
        setContentView(binding.getRoot());
    }
    private void loadReceiverDetails()
    {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }
    private void setListeners()
    {
        binding.imageback.setOnClickListener(v -> onBackPressed());
    }
}