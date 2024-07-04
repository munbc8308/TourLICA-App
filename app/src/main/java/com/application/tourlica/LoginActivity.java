package com.application.tourlica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.application.tourlica.ui.log_in.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Bundle bundle = new Bundle();
        bundle.putString("TOURDATA", getIntent().getStringExtra("TOURDATA"));
        bundle.putString("LOCATION", getIntent().getStringExtra("LOCATION"));
        if (savedInstanceState == null) {
            LoginFragment fragment = LoginFragment.newInstance();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.log_in_activity, fragment)
                    .commitNow();
        }
    }
}