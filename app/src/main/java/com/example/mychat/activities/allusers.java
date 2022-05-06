package com.example.mychat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mychat.R;
import com.example.mychat.databinding.ActivityAllusersBinding;

public class allusers extends AppCompatActivity {
    ActivityAllusersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllusersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
    public void setListeners()
    {
       // binding.imageSignOut.setOnClickListener(view -> signOut);
        binding.fabNewChat.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));

    }
}