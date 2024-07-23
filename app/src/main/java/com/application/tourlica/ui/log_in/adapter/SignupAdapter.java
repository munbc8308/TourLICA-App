package com.application.tourlica.ui.log_in.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.application.tourlica.ui.log_in.SignupPagerFragment;

public class SignupAdapter extends FragmentStateAdapter {
    public SignupAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int).
        Fragment fragment = new SignupPagerFragment();
        Bundle args = new Bundle();
        // The object is just an integer.
        args.putInt(SignupPagerFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
