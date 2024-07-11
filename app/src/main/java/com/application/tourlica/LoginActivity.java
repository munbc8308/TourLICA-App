package com.application.tourlica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.application.tourlica.ui.log_in.LoginFragment;
import com.application.tourlica.ui.log_in.SignupDetailFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.log_in_activity, fragment).commit(); // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    class CustomPagerAdapter extends FragmentStatePagerAdapter {
        private int PAGENUMBER = 3;

        public CustomPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                case 1:
                case 3:
                default:
                    return SignupDetailFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return PAGENUMBER;
        }
    }

}