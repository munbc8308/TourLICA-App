package com.application.tourlica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.application.tourlica.ui.log_in.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    private String tour_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tour_information = getIntent().getStringExtra("TOURDATA");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow();
        }
    }
}