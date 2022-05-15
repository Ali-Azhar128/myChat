package com.example.mychat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mychat.R;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(){
            public void run() {
                try {
                    sleep(2000);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(splashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();

    }
}