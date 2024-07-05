package com.application.tourlica.ui.log_in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.tourlica.databinding.FragmentSignupBinding;


public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding = null;
    public static SignupFragment newInstance() {
        return new SignupFragment();
    }
    @Override
    public void onCreate(@NonNull
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
