package com.example.mychat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.EditText;

import com.example.mychat.R;
import com.example.mychat.adapter.adapter;
import com.example.mychat.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    TabLayout tablayout;
    ViewPager viewpager;
    EditText _phone, _email, _password, _cpass;
    ActivityMainBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager.requestFocus();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        tablayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);

        tablayout.addTab(tablayout.newTab().setText("Login"));
        tablayout.addTab(tablayout.newTab().setText("Sign Up"));
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        _phone = (EditText) findViewById(R.id.phone);
        _email = (EditText) findViewById(R.id.email);
        _password = (EditText) findViewById(R.id.pass);
        _cpass = (EditText) findViewById(R.id.confirmpass);

        final adapter adapter1 = new adapter(getSupportFragmentManager(), this, tablayout.getTabCount());
        viewpager.setAdapter(adapter1);

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));




    }
   
}