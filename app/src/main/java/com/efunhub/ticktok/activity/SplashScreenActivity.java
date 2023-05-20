package com.efunhub.ticktok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.efunhub.ticktok.R;
import com.efunhub.ticktok.application.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashScreen();
    }

    private void splashScreen() {
        if (SessionManager.onGetAutoCustomerId().isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
            //startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

        }
    }

}
